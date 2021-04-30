import java.io.*;
import java.util.*;
class Reader1{
    public ArrayList<ArrayList<String>> fieldsFromFile=new ArrayList<ArrayList<String>>();
    public ArrayList<ArrayList<String>> methodsFromFile=new ArrayList<ArrayList<String>>();
    public ArrayList<ArrayList<String>> fieldsFromScanner=new ArrayList<ArrayList<String>>();
    public ArrayList<ArrayList<String>> methodsFromScanner=new ArrayList<ArrayList<String>>();
    public void scanScanner() throws IOException {
        ArrayList<String> a = new ArrayList<String>();
        ArrayList<String> b = new ArrayList<String>();
        Scanner in=new Scanner(System.in);
        String S=null;
        int cntopen = 0;
        int cntclose = 0;
        do{
            S=in.nextLine();
            if (S.contains("class")) {
                a.add(S);
                b.add(S);
                if (S.contains("{")) {
                    cntopen++;
                }
                if (S.contains("}")) {
                    cntclose++;
                }
                while(!S.contains("{")&&cntopen==0)
                S=in.nextLine();
                if(cntopen==0)cntopen++;
                S=in.nextLine();
            }
            if ( (cntclose - cntopen) == -1&& S != null && S.contains(";")) {
                b.add(S);
            }
            else if (!S.contains(";") && S != null && S.matches(".*\\(.*\\).*") && (cntclose - cntopen) == -1) {
                a.add(S);
            }
            if (S.contains("{")) {
                cntopen++;
            }
            else if (S.contains("}")) {
                cntclose++;
            }
        } while(cntclose!=cntopen);
        fieldsFromScanner.add(b);
        methodsFromScanner.add(a);
    }
    public void scanfields(File1 f) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(f.str)));
        String S = null;
        ArrayList<String> a = new ArrayList<String>();
        ArrayList<String> b;
        while ((S = reader.readLine()) != null) {
            if (S.contains("class")) {
                a.add(S);
                int cntopen = 0;
                int cntclose = 0;
                do {
                    if (S.contains("{")) {
                        cntopen++;
                    }
                    if (S.contains("}")) {
                        cntclose++;
                    }
                    S = reader.readLine();
                    if ((!S.matches(".*[\\(\\)\\{\\}].*") || S.matches(".*=.*")) && S != null && S.contains(";")) {
                        a.add(S);
                    } else {
                        int cntopen1 = 0;
                        int cntclose1 = 0;
                        if (S.contains("{")) {
                            cntopen1++;
                        }
                        if (S.contains("}")) {
                            cntclose1++;
                        }
                        while (cntopen1 != cntclose1) {
                            S = reader.readLine();
                            if (S.contains("{")) {
                                cntopen1++;
                            }
                            if (S.contains("}")) {
                                cntclose1++;
                            }
                        }
                    }
                }
                while (cntopen != cntclose);
                b = (ArrayList<String>) a.clone();
                fieldsFromFile.add(b);
                a.clear();
            }
        }
        for(ArrayList<String> k: fieldsFromFile)
        {
            if(k.get(0).contains("extends"))
            {
                for(ArrayList<String> t:fieldsFromFile)
                {
                    if(t.get(0).contains(k.get(0).replaceAll(".*extends ","").replaceAll("implements.*$|throws.*$|extend.*",""))&&t.get(0)!=k.get(0)&&!t.get(0).contains(k.get(0).replaceAll(".*extends ","extends").replaceAll("implements.*$|throws.*$","")))
                    {
                        for(int i=1;i<t.size();i++)
                        {
                            k.add(t.get(i));
                        }
                        break;
                    }
                }
            }
        }
    }
    public void scanmethods(File1 f) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(f.str)));
        String S = null;
        ArrayList<String> a = new ArrayList<String>();
        ArrayList<String> b = new ArrayList<String>();
        while ((S = reader.readLine()) != null) {
            if (S.contains("class")) {
                a.add(S);
                int cntopen = 0;
                int cntclose = 0;
                if (S.contains("{")) {
                    cntopen++;
                }
                if (S.contains("}")) {
                    cntclose++;
                }
                do {
                    S = reader.readLine();
                    if (S.contains("{")) {
                        cntopen++;
                    }
                    if (S.contains("}")) {
                        cntclose++;
                    }
                    if (!S.contains(";") && S != null && S.matches(".*\\(.*\\).*") && (cntclose - cntopen) == -1) {
                        a.add(S);
                    }
                } while (cntopen != cntclose);
                b = (ArrayList<String>) a.clone();
                methodsFromFile.add(b);
                a.clear();
            }
        }
        for(ArrayList<String> k: methodsFromFile)
        {
            if(k.get(0).contains("extends"))
            {
                for(ArrayList<String> t:methodsFromFile)
                {
                    if(t.get(0).contains(k.get(0).replaceAll(".*extends ","").replaceAll("implements.*$|throws.*$|extend.*",""))&&t.get(0)!=k.get(0)&&!t.get(0).contains(k.get(0).replaceAll(".*extends ","extends").replaceAll("implements.*$|throws.*$","")))
                    {
                        for(int i=1;i<t.size();i++)
                        {
                            k.add(t.get(i));
                        }
                        break;
                    }
                }
            }
        }
    }
}
class Generate1{
    public ArrayList<ArrayList<String>> genmethodsGetFromFile=new ArrayList<ArrayList<String>>();
    public ArrayList<ArrayList<String>> genmethodsSetFromFile=new ArrayList<ArrayList<String>>();
    public ArrayList<ArrayList<String>> genmethodsSetFromScanner=new ArrayList<ArrayList<String>>();
    public ArrayList<ArrayList<String>> genmethodsGetFromScanner=new ArrayList<ArrayList<String>>();
    public ArrayList<String> genmethodsEqualsFromFile=new ArrayList<String>();
    public ArrayList<String> genmethodsHashFromFile=new ArrayList<String>();
    public ArrayList<String> genmethodsEqualsFromScanner=new ArrayList<String>();
    public ArrayList<String> genmethodsHashFromScanner=new ArrayList<String>();
    public void generateHashFromScanner(Reader1 r) throws Exception
    {
        String S;
        String name;
        for (ArrayList<String> t : r.fieldsFromScanner) {
            S=t.get(0).replaceAll(".*class ","").replaceAll("throws.*|implements.*|extend.*"," ")+"public int hashcode() {\nint result=17;\n";
            for(int j=1;j<t.size();j++) {
                name=t.get(j).replaceAll("public|private|protected|static", "").replaceAll("^.* ", "").replaceAll("=.*|;", "");
                if (t.get(j).contains("[]"))
                    S += "result=37*result + Arrays.hashCode("+name+");\n";
                else if (t.get(j).contains("long"))
                    S += "result=37*result + (int)" + name+";\n";
                else if (t.get(j).contains("short"))
                    S += "result=37*result + (int)" + name+ ";\n";
                else if (t.get(j).contains("char"))
                    S += "result=37*result + (int)" + name + ";\n";
                else if (t.get(j).contains("boolean"))
                    S += "result=37*result + (" + name+ "? 1 : 0);\n";
                else if (t.get(j).contains("float"))
                    S += "result=37*result + Float.floatToIntBits(" + name+ ");\n";
                else if (t.get(j).contains("double"))
                    S += " long longBits = Double.doubleToLongBits(" +name + ");\nresult=37*result + (int)(longBits);\n";
                else if (t.get(j).contains("int"))
                    S += "result=37*result + "+name+";\n";
                else if (t.get(j).contains("byte"))
                    S += "result = 37 * result + (int)"+ name+";\n";
                else S+="result=result+Objects.hash("+name+");\n";
            }
            S+="return result;\n}";
            genmethodsHashFromScanner.add(S); }
    }

    public void generateEqualsFromScanner(Reader1 r) throws Exception
    {
        String S;
        for (ArrayList<String> t : r.fieldsFromScanner) {
            S = t.get(0).replaceAll(".*class ","").replaceAll("throws.*|implements.*|extend.*"," ")+"public boolean equals"+"( Object obj) {\n" +
                    "if (this == obj)\n" +
                    "return true;\n" +
                    "if (obj == null)\n" +
                    "return false;\n"+
                    "if (getClass() != obj.getClass())\n"+
                    "return false;\n"+t.get(0).replaceAll(".*class ","").replaceAll("implements.*$|throws.*$|extend.*","")+
                    " other=("+t.get(0).replaceAll(".*class ","").replaceAll("implements.*$|throws.*$|extend.*","")+")obj;\n"+
                    "if(";
            for(int j=1;j<t.size();j++) {
                S+="this."+ t.get(j).replaceAll("public|private|protected|static", "").replaceAll("^.* ", "").replaceAll("=.*|;", "")+
                        "!=other."+ t.get(j).replaceAll("public|private|protected|static", "").replaceAll("^.* ", "").replaceAll("=.*|;", "");
                if(t.size()-j!=1) S+="||";
            }
            S+=")\nreturn false;\nreturn true;\n}";
            genmethodsEqualsFromScanner.add(S); }
    }
    public void generateGetFromScanner(Reader1 r) throws IOException {
        String S;
        ArrayList<String> help = new ArrayList<String>();
        ArrayList<String> help2;
        for (ArrayList<String> t : r.fieldsFromScanner) {
            for (String j : t) {
                if (!j.contains("class")) {
                    S = "public" + j.replaceAll(".*public|.*private|.*protected", "").replaceAll("[^ ]*;|[^ ]*=.*;", "") + " get" +
                            j.replaceAll("public|private|protected|static", "").replaceAll("^.* ", "").replaceAll("=.*|;", "")
                            + "()\n{\nreturn " + j.replaceAll("public|private|protected", "").replaceAll("^.* ", "").replaceAll("=.*|;", "") + ";\n}";
                    help.add(S);
                }
            }
            help2 = (ArrayList<String>) help.clone();
            genmethodsGetFromScanner.add(help2);
            help.clear();
        }
    }
    public void generateSetFromScanner(Reader1 r) throws IOException {
        String S;
        ArrayList<String> help = new ArrayList<String>();
        ArrayList<String> help2;
        for (ArrayList<String> t : r.fieldsFromScanner) {
            for (String j : t) {
                if (!j.contains("class")) {
                    S = "public " + "void"
                            + " set" + j.replaceAll("public|private|protected", "").replaceAll("^.* ", "").replaceAll("=.*|;", "")
                            + "(" + j.replaceAll(".*public|.*private|.*protected", "").replaceAll("[^ ]*;|[^ ]*=.*;", "") + "qwe)" + "\n{\n"
                            + j.replaceAll("public|private|protected", "").replaceAll("^.* ", "").replaceAll("=.*|;", "") + "=qwe" + ";\n}";
                    help.add(S);
                }
            }
            help2 = (ArrayList<String>) help.clone();
            genmethodsSetFromScanner.add(help2);
            help.clear();
        }
    }
    public void generateSetFromFile(Reader1 r) throws IOException {
        String S;
        ArrayList<String> help = new ArrayList<String>();
        ArrayList<String> help2;
        for (ArrayList<String> t : r.fieldsFromFile) {
            for (String j : t) {
                if (!j.contains("class")) {
                    S = "public " + "void"
                            + " set" + j.replaceAll("public|private|protected", "").replaceAll("^.* ", "").replaceAll("=.*|;", "")
                            + "(" + j.replaceAll(".*public|.*private|.*protected", "").replaceAll("[^ ]*;|[^ ]*=.*;", "") + "qwe)" + "\n{\n"
                            + j.replaceAll("public|private|protected", "").replaceAll("^.* ", "").replaceAll("=.*|;", "") + "=qwe" + ";\n}";
                    help.add(S);
                }
            }
            help2 = (ArrayList<String>) help.clone();
            genmethodsSetFromFile.add(help2);
            help.clear();
        }
    }
    public void generateGetFromFile(Reader1 r) throws IOException {
        String S;
        ArrayList<String> help = new ArrayList<String>();
        ArrayList<String> help2;
        for (ArrayList<String> t : r.fieldsFromFile) {
            for (String j : t) {
                if (!j.contains("class")) {
                    S = "public " + j.replaceAll(".*public|.*private|.*protected", "").replaceAll("[^ ]*;|[^ ]*=.*;", "") + " get" +
                            j.replaceAll("public|private|protected|static", "").replaceAll("^.* ", "").replaceAll("=.*|;", "")
                            + "()\n{\nreturn " + j.replaceAll("public|private|protected", "").replaceAll("^.* ", "").replaceAll("=.*|;", "") + ";\n}";
                    help.add(S);
                }
            }
            help2 = (ArrayList<String>) help.clone();
            genmethodsGetFromFile.add(help2);
            help.clear();
        }
    }
    public void generateEqualsFromFile(Reader1 r) throws Exception
    {
        String S;
        String type;
        for (ArrayList<String> t : r.fieldsFromFile) {
            S = t.get(0).replaceAll(".*class ","").replaceAll("throws.*|implements.*|extend.*"," ")+"public boolean equals"+"( Object obj) {\n" +
                    "if (this == obj)\n" +
                    "return true;\n" +
                    "if (obj == null)\n" +
                    "return false;\n"+
                    "if (getClass() != obj.getClass())\n"+
                    "return false;\n"+t.get(0).replaceAll(".*class ","").replaceAll("implements.*$|throws.*$|extend.*","")+
                    " other=("+t.get(0).replaceAll(".*class ","").replaceAll("implements.*$|throws.*$|extend.*","")+")obj;\n"+
                    "if(";
            for(int j=1;j<t.size();j++) {
                type=t.get(j);
                if(type.contains("boolean")||type.contains("int")||type.contains("float")||type.contains("double")||type.contains("char")||type.contains("byte")||type.contains("long")||type.contains("short"))
                S+="this."+ t.get(j).replaceAll("public|private|protected|static", "").replaceAll("^.* ", "").replaceAll("=.*|;", "")+
                        "!=other."+ t.get(j).replaceAll("public|private|protected|static", "").replaceAll("^.* ", "").replaceAll("=.*|;", "");
                else S+="!this."+t.get(j).replaceAll("public|private|protected|static", "").replaceAll("^.* ", "").replaceAll("=.*|;", "")+".equals(other."
                        +t.get(j).replaceAll("public|private|protected|static", "").replaceAll("^.* ", "").replaceAll("=.*|;", "")+")";
                if(t.size()-j!=1) S+="||";
            }
            S+=")\nreturn false;\nreturn true;\n}";
            genmethodsEqualsFromFile.add(S); }
    }
    public void generateHashFromFile(Reader1 r) throws Exception
    {
        String S;
        String name;
        for (ArrayList<String> t : r.fieldsFromFile) {
            S=t.get(0).replaceAll(".*class ","").replaceAll("throws.*|implements.*|extend.*"," ")+"public int hashcode() {\nint result=17;\n";
            for(int j=1;j<t.size();j++) {
                name=t.get(j).replaceAll("public|private|protected|static", "").replaceAll("^.* ", "").replaceAll("=.*|;", "");
                if (t.get(j).contains("[]"))
                    S += "result=37*result + Arrays.hashCode("+name+");\n";
                else if (t.get(j).contains("long"))
                    S += "result=37*result + (int)" + name+";\n";
                else if (t.get(j).contains("short"))
                    S += "result=37*result + (int)" + name+ ";\n";
                else if (t.get(j).contains("char"))
                    S += "result=37*result + (int)" + name + ";\n";
                else if (t.get(j).contains("boolean"))
                    S += "result=37*result + (" + name+ "? 1 : 0);\n";
                else if (t.get(j).contains("float"))
                    S += "result=37*result + Float.floatToIntBits(" + name+ ");\n";
                else if (t.get(j).contains("double"))
                    S += " long longBits = Double.doubleToLongBits(" +name + ");\nresult=37*result + (int)(longBits);\n";
                else if (t.get(j).contains("int"))
                    S += "result=37*result + "+name+";\n";
                else if (t.get(j).contains("byte"))
                    S += "result = 37 * result + (int)"+ name+";\n";
                else S+="result=result+Objects.hash("+name+");\n";
            }
            S+="return result;\n}";
            genmethodsHashFromFile.add(S); }
    }
}
class File1 {
    public String str;
    File1(String st) {
        str = st;
    }
    public void changeset(Generate1 g) throws IOException {
        String S;
        BufferedReader r = new BufferedReader(new FileReader(new File(str)));
        BufferedWriter w = new BufferedWriter(new FileWriter(new File("src\\text.txt")));
        while ((S = r.readLine()) != null) {
            w.write(S+"\n");
        }
        r.close();
        w.close();
        try (BufferedReader reader = new BufferedReader(new FileReader(new File("src\\text.txt")));
             BufferedWriter writer = new BufferedWriter(new FileWriter(new File(str)))) {
            int cntopen = 0;
            int ind1 = 0;
            int ind2 = 0;
            int cntclose = 0;
            S = null;
            while ((S = reader.readLine()) != null) {
                if (!S.contains("set")) {
                    writer.write(S + "\n");
                } else {
                    if (S.contains("{")) {
                        cntopen++;
                    }
                    for (ArrayList<String> i : g.genmethodsSetFromFile) {
                        for (String j : i) {
                            cntopen = 0;
                            cntclose = 0;
                            if (j.contains(S.replaceAll("\\(.*\\)", "").substring(S.replaceAll("\\(.*", "").lastIndexOf(" ") + 1))) {
                                ind1 = g.genmethodsSetFromFile.indexOf(i);
                                ind2 = i.indexOf(j);
                            }
                        }
                    }
                    do {
                        S = reader.readLine();
                        if (S.contains("{")) {
                            cntopen++;
                        }
                        if (S.contains("}")) {
                            cntclose++;
                        }
                    } while (cntopen != cntclose);
                    writer.write(g.genmethodsSetFromFile.get(ind1).get(ind2) + "\n");
                }
            }
        }
    }
    public void changeEquals(Generate1 g) throws IOException {
        String S;
        BufferedReader r = new BufferedReader(new FileReader(new File(str)));
        BufferedWriter w = new BufferedWriter(new FileWriter(new File("src\\text.txt")));
        while ((S = r.readLine()) != null) {
            w.write(S+"\n");
        }
        r.close();
        w.close();
        try (BufferedReader reader = new BufferedReader(new FileReader(new File("src\\text.txt")));
             BufferedWriter writer = new BufferedWriter(new FileWriter(new File(str)))) {
            int cntopen = 0;
            int ind1 = 0;
            int ind2 = 0;
            int cntclose = 0;
            String str=null;
            S = null;
            while ((S = reader.readLine()) != null) {
                if (S.contains("class")) str=S.replaceAll(".*class ","").replaceAll("implements.*|throws.*|extends.*","");
                if (!S.contains("equals")) {
                    writer.write(S + "\n");
                } else if(S.contains("boolean equals")){
                    if (S.contains("{")) {
                        cntopen++;
                    }
                    for (String i : g.genmethodsEqualsFromFile) {
                            cntopen = 0;
                            cntclose = 0;
                            if (i.contains(str)) {
                                ind1 = g.genmethodsEqualsFromFile.indexOf(i);
                        }
                    }
                    do {
                        S = reader.readLine();
                        if (S.contains("{")) {
                            cntopen++;
                        }
                        if (S.contains("}")) {
                            cntclose++;
                        }
                    } while (cntopen != cntclose);
                    writer.write(g.genmethodsEqualsFromFile.get(ind1).replaceAll(".*public","public") + "\n");
                }
            }
        }
    }
    public void changeHash(Reader1 r, Generate1 g) throws IOException {
        String S;
        BufferedReader r1 = new BufferedReader(new FileReader(new File(str)));
        BufferedWriter w = new BufferedWriter(new FileWriter(new File("src\\text.txt")));
        while ((S = r1.readLine()) != null) {
            w.write(S+"\n");
        }
        r1.close();
        w.close();
        try (BufferedReader reader = new BufferedReader(new FileReader(new File("src\\text.txt")));
             BufferedWriter writer = new BufferedWriter(new FileWriter(new File(str)))) {
            int cntopen = 0;
            int ind1 = 0;
            int cntclose = 0;
            S = null;
            String str=null;
            while ((S = reader.readLine()) != null) {
                if (S.contains("class")) str=S.replaceAll(".*class ","").replaceAll("implements.*|throws.*|extends.*","");
                if (!S.contains("hash")) writer.write(S + "\n");
                else if(S.contains("int hash")){
                    if (S.contains("{")) {
                        cntopen++;
                    }
                    for (String i : g.genmethodsHashFromFile) {
                        cntopen = 0;
                        cntclose = 0;
                        for(ArrayList<String> k:r.fieldsFromFile)
                            if (i.contains(str)) {
                                ind1 = g.genmethodsHashFromFile.indexOf(i);
                            }
                    }
                    do {
                        S = reader.readLine();
                        if (S.contains("{")) {
                            cntopen++;
                        }
                        if (S.contains("}")) {
                            cntclose++;
                        }
                    } while (cntopen != cntclose);
                    writer.write(g.genmethodsHashFromFile.get(ind1).replaceAll(".*public","public") + "\n");
                }
            }
        }
    }
    public void changeget(Generate1 g) throws IOException {
        String S;
        BufferedReader r = new BufferedReader(new FileReader(new File(str)));
        BufferedWriter w = new BufferedWriter(new FileWriter(new File("src\\text.txt")));
        while ((S = r.readLine()) != null) {
            w.write(S+"\n");
        }
        r.close();
        w.close();
        try (BufferedReader reader = new BufferedReader(new FileReader(new File("src\\text.txt")));
             BufferedWriter writer = new BufferedWriter(new FileWriter(new File(str)))) {
            int cntopen = 0;
            int ind1 = 0;
            int ind2 = 0;
            int cntclose = 0;
            S = null;
            while ((S = reader.readLine()) != null) {
                if (!S.contains("get") | S.contains("getClass()")) {
                    writer.write(S + "\n");
                } else {
                    if (S.contains("{")) {
                        cntopen++;
                    }
                    for (ArrayList<String> i : g.genmethodsGetFromFile) {
                        for (String j : i) {
                            cntopen = 0;
                            cntclose = 0;
                            if (j.contains(S.replaceAll("\\(.*\\)", "").substring(S.replaceAll("\\(.*", "").lastIndexOf(" ") + 1))) {
                                ind1 = g.genmethodsGetFromFile.indexOf(i);
                                ind2 = i.indexOf(j);
                            }
                        }
                    }
                    do {
                        S = reader.readLine();
                        if (S.contains("{")) {
                            cntopen++;
                        }
                        if (S.contains("}")) {
                            cntclose++;
                        }
                    } while (cntopen != cntclose);
                    writer.write(g.genmethodsGetFromFile.get(ind1).get(ind2) + "\n");
                }
            }
        }
    }
}
public class test {
    public static void main(String[] args) throws Exception {
        int k;
        Reader1 r=new Reader1();
        File1 file1 = new File1("src\\test.txt");
        Generate1 g=new Generate1();
        r.scanfields(file1);
        r.scanmethods(file1);
        Scanner s=new Scanner(System.in);
        do
        {
            System.out.println("Ввести класс: 1\n" +
                    "Сгенерировать методы: 2\n" +
                    "Вывести сгенерированные методы set: 3\n" +
                    "Вывести сгенерированные методы get: 4\n" +
                    "Вывести сгенерированные методы equals: 5\n" +
                    "Вывести сгенерированные методы hashcode: 6\n" +
                    "Заменить методы в файле: 7\n" +
                    "Выход: любая другая клавиша\n");
            k=s.nextInt();
            if(k==1){
                r.scanScanner();
            }
            else if(k==2){
                g.generateEqualsFromScanner(r);
                g.generateHashFromScanner(r);
                g.generateGetFromScanner(r);
                g.generateSetFromScanner(r);
                g.generateSetFromFile(r);
                g.generateGetFromFile(r);
                g.generateEqualsFromFile(r);
                g.generateHashFromFile(r);
            }
            else if(k==3)
            {
                for(ArrayList<String> t:g.genmethodsSetFromFile)
                {
                    for(String q:t) {
                    System.out.println(q);
                    }
                }
               for(ArrayList<String> t:g.genmethodsSetFromScanner)
                {
                    for(String q:t) {
                        System.out.println(q);
                    }
                }
            }
           else if(k==4)
            {
                for(ArrayList<String> t:g.genmethodsGetFromFile)
                {
                    for(String q:t) {
                        System.out.println(q);
                    }
                }
                for(ArrayList<String> t:g.genmethodsGetFromScanner)
                {
                    for(String q:t) {
                        System.out.println(q);
                    }
                }
            }
           else if(k==5)
            {
                for(String q:g.genmethodsEqualsFromFile) {
                    System.out.println(q);
                }

                for(String q:g.genmethodsEqualsFromScanner) {
                    System.out.println(q);
                }
            }
           else if(k==6)
            {
                for(String q:g.genmethodsHashFromFile) {
                    System.out.println(q);
                }

                for(String q:g.genmethodsHashFromScanner) {
                    System.out.println(q);
                }
            }
           else if(k==7)
            {
                file1.changeEquals(g);
                file1.changeHash(r,g);
                file1.changeget(g);
                file1.changeset(g);
            }
           else break;
        }while(true);
    }
}
