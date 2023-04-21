package ca.ucalgary.cpsc.ebe.fitClipse.javaSourceModification;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaFile {

    private LinkedList imports;

    private String packageName = "";

    private JavaClass mainClass;

    public void addImport(String in) {
        if (imports == null) imports = new LinkedList();
        imports.add(in);
    }

    public JavaClass getMainClass() {
        return mainClass;
    }

    public void setMainClass(JavaClass mainClass) {
        this.mainClass = mainClass;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String renderSource() {
        StringBuffer source = new StringBuffer();
        if (!("".equals(this.packageName))) source.append("package ").append(this.packageName).append(";\n");
        source.append(renderImports());
        source.append("\n");
        source.append(this.mainClass.renderSource());
        return source.toString();
    }

    private String renderImports() {
        StringBuffer result = new StringBuffer();
        if (this.imports == null) return "";
        for (int i = 0; i < imports.size(); i++) {
            result.append("import " + imports.get(i).toString() + ";\n");
        }
        return result.toString();
    }

    public String commitToFile(String sourcePath) throws Exception {
        String filename = sourcePath + "/" + this.packageName.replaceAll("\\.", "/");
        if (this.packageName.equals("")) filename = sourcePath + "/";
        System.out.println("the path is: " + filename);
        File file = new File(filename);
        if (!file.exists()) {
            file.mkdirs();
        }
        filename += "/" + this.mainClass.getName() + ".java";
        file = new File(filename);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            PrintWriter out = new PrintWriter(new FileOutputStream(file));
            out.println(this.renderSource().replaceAll("\n", "\r\n"));
            out.close();
            return filename;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static JavaFile javaFile;

    public static synchronized JavaFile loadSourceFromFile(String filename) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(filename));
        String line = null;
        String txtDataLeft = "";
        while ((line = in.readLine()) != null) {
            txtDataLeft += line + "\n";
        }
        javaFile = new JavaFile();
        String matcher = "";
        while (txtDataLeft.length() > 0) {
            char c = txtDataLeft.charAt(0);
            if (c != ' ' && c != '\t' && c != '\n') matcher += txtDataLeft.charAt(0); else {
                matcher = "";
            }
            txtDataLeft = txtDataLeft.substring(1);
            if (matcher.equals("package")) {
                int index = doPackage(txtDataLeft);
                txtDataLeft = txtDataLeft.substring(index);
                matcher = "";
            } else if (matcher.equals("public")) {
                int index = doPublic(txtDataLeft);
                txtDataLeft = txtDataLeft.substring(index);
                matcher = "";
            } else if (matcher.equals("private")) {
                matcher = "";
            } else if (matcher.equals("protected")) {
                matcher = "";
            }
        }
        return javaFile;
    }

    private static int doPublic(String remaining) {
        int propIndex = remaining.indexOf(";");
        int methodIndex = remaining.indexOf("(");
        int classIndex = remaining.indexOf("{");
        int min = Integer.MAX_VALUE;
        if (propIndex == -1) propIndex = Integer.MAX_VALUE;
        if (methodIndex == -1) methodIndex = Integer.MAX_VALUE;
        if (classIndex == -1) classIndex = Integer.MAX_VALUE;
        min = Math.min(propIndex, methodIndex);
        min = Math.min(min, classIndex);
        if (min == classIndex) {
            return doClass(remaining, "public");
        } else if (min == propIndex) {
            return doProperty(remaining, "public");
        } else if (min == methodIndex) {
            return doMethod(remaining, "public");
        }
        return 0;
    }

    private static int doClass(String remaining, String Qualifier) {
        int index = remaining.indexOf("{");
        String line = remaining.substring(0, index);
        String[] parts = line.replace("\t", " ").replace("\n", " ").split(" ");
        boolean extending = false;
        boolean implementing = false;
        boolean isAbstract = false;
        JavaClass newClass = null;
        for (int i = 0; i < parts.length; i++) {
            if ("abstract".equals(parts[i])) {
                isAbstract = true;
            } else if ("extends".equals(parts[i])) {
                implementing = false;
                extending = true;
            } else if ("class".equals(parts[i])) {
                newClass = new JavaClass(parts[i + 1], Qualifier);
                if (isAbstract) {
                    newClass.makeAbstract();
                }
            } else if ("implements".equals(parts[i])) {
                implementing = true;
                extending = false;
            } else if (!"".equals(parts[i])) {
                if (extending) {
                    newClass.addSuperClass(parts[i]);
                } else if (implementing) {
                    newClass.addInterface(parts[i]);
                }
            }
        }
        javaFile.setMainClass(newClass);
        return index;
    }

    private static int doProperty(String remaining, String qualifier) {
        int index = remaining.indexOf(";");
        String line = remaining.substring(0, index);
        String[] temp = line.replace("\t", " ").replace("\n", " ").split(" ");
        JavaProperty prop = null;
        boolean isAbstract = false;
        boolean isStatic = false;
        String[] parts = removeNullElements(temp);
        for (int i = 0; i < parts.length; i++) {
            if ("abstract".equals(parts[i])) {
                isAbstract = true;
            } else if ("static".equals(parts[i])) {
                isStatic = true;
            } else if (!"".equals(parts[i])) {
                String rType = parts[i];
                String name = parts[i + 1];
                String value = null;
                if (parts.length > i + 2 && "=".equals(parts[i + 2])) {
                    value = stripQuotes(parts[i + 3]);
                }
                prop = new JavaProperty(name, qualifier);
                prop.setAbstract(isAbstract);
                prop.setStatic(isStatic);
                prop.setValue(value);
                prop.setType(rType);
                break;
            }
        }
        javaFile.getMainClass().addChildElement(prop);
        return index;
    }

    private static int doMethod(String remaining, String qualifier) {
        int index = remaining.indexOf("(");
        String line = remaining.substring(0, index);
        String[] parts = removeNullElements(line.replace("\t", " ").replace("\n", " ").split(" "));
        JavaMethod meth = null;
        boolean isAbstract = false;
        boolean isStatic = false;
        for (int i = 0; i < parts.length; i++) {
            if ("abstract".equals(parts[i])) {
                isAbstract = true;
            } else if ("static".equals(parts[i])) {
                isStatic = true;
            } else {
                String rType = parts[i];
                String name = parts[i + 1];
                meth = new JavaMethod(name, qualifier);
                meth.setReturnType(rType);
                meth.setStatic(isStatic);
                meth.setAbstract(isAbstract);
                break;
            }
        }
        int nextStep = remaining.indexOf(")") + 1;
        String params = remaining.substring(index + 1, nextStep - 1);
        System.out.println("params: " + params);
        index = nextStep;
        StringTokenizer paramTokens = new StringTokenizer(params, ",");
        while (paramTokens.hasMoreTokens()) {
            String[] paramParts = removeNullElements(paramTokens.nextToken().split(" "));
            meth.addParameter(paramParts[1], paramParts[0]);
        }
        index = remaining.indexOf("{");
        int depth = 1;
        String content = "";
        int i = index + 1;
        while (depth > 0) {
            char c = remaining.charAt(i);
            if (c == '{') {
                depth++;
            } else if (c == '}') {
                depth--;
            }
            content += c;
            i++;
        }
        System.out.println(content.substring(0, content.length() - 2));
        index += i;
        meth.addContentNode(new JavaMethodContentNode(content));
        System.out.println("Method: " + meth.render());
        javaFile.getMainClass().addChildElement(meth);
        return index;
    }

    private static int doPackage(String remaining) {
        String packageName = "";
        StringTokenizer tokens = new StringTokenizer(remaining, ";");
        int index = remaining.indexOf(";");
        String part = remaining.substring(0, index);
        javaFile.setPackageName(removeLeadingTrailingWhiteSpace(part));
        return index;
    }

    public static String[] removeNullElements(String[] in) {
        String[] result = null;
        int count = 0;
        for (int i = 0; i < in.length; i++) {
            if (!"".equals(in[i]) && in[i] != null) {
                count++;
            }
        }
        result = new String[count];
        count = 0;
        for (int i = 0; i < in.length; i++) {
            if (!"".equals(in[i]) && in[i] != null) {
                result[count] = in[i];
                count++;
            }
        }
        return result;
    }

    public static String stripQuotes(String in) {
        if (in.charAt(0) == '"' || in.charAt(0) == '\'') {
            in = in.substring(1);
        }
        if (in.charAt(in.length() - 1) == '"' || in.charAt(in.length() - 1) == '\'') {
            in = in.substring(0, in.length() - 1);
        }
        return in;
    }

    public static String removeLeadingTrailingWhiteSpace(String in) {
        Pattern p = Pattern.compile("\\s*([\\w|\\.]*)\\s*");
        Matcher match = p.matcher(in);
        if (match.matches()) return match.group(1); else return null;
    }
}
