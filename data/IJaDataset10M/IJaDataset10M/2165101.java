package net.borderwars.util;

import java.io.*;
import java.util.Properties;
import java.util.Vector;

/**
 * @author ehubbard
 *         Date: Aug 15, 2005
 *         Time: 11:31:10 AM
 */
public class Java2HTML {

    static final String keywords[] = { "abstract", "default", "if", "private", "throw", "boolean", "do", "implements", "protected", "throws", "break", "double", "import", "public", "transient", "byte", "else", "instanceof", "return", "try", "case", "extends", "int", "short", "void", "catch", "final", "interface", "static", "volatile", "char", "finally", "long", "super", "while", "class", "float", "native", "switch", "const", "for", "new", "synchronized", "continue", "goto", "package", "this" };

    static Vector<String> keyw = new Vector<String>(keywords.length);

    static {
        for (String keyword : keywords) {
            keyw.addElement(keyword);
        }
    }

    static int tabsize = 4;

    static String bgcolor = "C0C0C0";

    static String txcolor = "000000";

    static String kwcolor = "0000F0";

    static String cmcolor = "A00000";

    private Java2HTML() {
    }

    static void convert(String source) throws IOException {
        String dest = source + ".html";
        System.out.println(dest);
        FileReader in = new FileReader(source);
        FileWriter out = new FileWriter(dest);
        out.write("<html>\r\n<head>\r\n<title>");
        out.write(source);
        out.write("</title>\r\n</head>\r\n<body ");
        out.write("bgcolor=\"" + bgcolor + "\" ");
        out.write("text=\"" + txcolor + "\">\r\n");
        out.write("<pre>\r\n");
        StringBuffer buf = new StringBuffer(2048);
        int c = 0, kwl = 0, bufl = 0;
        char ch = 0, lastch;
        int s_normal = 0;
        int s_string = 1;
        int s_char = 2;
        int s_comline = 3;
        int s_comment = 4;
        int state = s_normal;
        while (c != -1) {
            c = in.read();
            lastch = ch;
            ch = c >= 0 ? (char) c : 0;
            if (state == s_normal) {
                if (kwl == 0 && Character.isJavaIdentifierStart(ch) && !Character.isJavaIdentifierPart(lastch) || kwl > 0 && Character.isJavaIdentifierPart(ch)) {
                    buf.append(ch);
                    bufl++;
                    kwl++;
                    continue;
                } else if (kwl > 0) {
                    String kw = buf.toString().substring(buf.length() - kwl);
                    if (keyw.contains(kw)) {
                        buf.insert(buf.length() - kwl, "<font color=\"" + kwcolor + "\">");
                        buf.append("</font>");
                    }
                    kwl = 0;
                }
            }
            switch(ch) {
                case '&':
                    buf.append("&amp;");
                    bufl++;
                    break;
                case '\"':
                    buf.append("&quot;");
                    bufl++;
                    if (state == s_normal) {
                        state = s_string;
                    } else if (state == s_string && lastch != '\\') {
                        state = s_normal;
                    }
                    break;
                case '\'':
                    buf.append("\'");
                    bufl++;
                    if (state == s_normal) {
                        state = s_char;
                    } else if (state == s_char && lastch != '\\') {
                        state = s_normal;
                    }
                    break;
                case '\\':
                    buf.append("\\");
                    bufl++;
                    if (lastch == '\\' && (state == s_string || state == s_char)) {
                        lastch = 0;
                    }
                    break;
                case '/':
                    buf.append("/");
                    bufl++;
                    if (state == s_comment && lastch == '*') {
                        buf.append("</font>");
                        state = s_normal;
                    }
                    if (state == s_normal && lastch == '/') {
                        buf.insert(buf.length() - 2, "<font color=\"" + cmcolor + "\">");
                        state = s_comline;
                    }
                    break;
                case '*':
                    buf.append("*");
                    bufl++;
                    if (state == s_normal && lastch == '/') {
                        buf.insert(buf.length() - 2, "<font color=\"" + cmcolor + "\">");
                        state = s_comment;
                    }
                    break;
                case '<':
                    buf.append("&lt;");
                    bufl++;
                    break;
                case '>':
                    buf.append("&gt;");
                    bufl++;
                    break;
                case '\t':
                    int n = bufl / tabsize * tabsize + tabsize;
                    while (bufl < n) {
                        buf.append(' ');
                        bufl++;
                    }
                    break;
                case '\r':
                case '\n':
                    if (state == s_comline) {
                        buf.append("</font>");
                        state = s_normal;
                    }
                    buf.append(ch);
                    if (buf.length() >= 1024) {
                        out.write(buf.toString());
                        buf.setLength(0);
                    }
                    bufl = 0;
                    if (kwl != 0) {
                        kwl = 0;
                    }
                    if (state != s_normal && state != s_comment) {
                        state = s_normal;
                    }
                    break;
                case 0:
                    if (c < 0) {
                        if (state == s_comline) {
                            buf.append("</font>");
                            state = s_normal;
                        }
                        out.write(buf.toString());
                        buf.setLength(0);
                        bufl = 0;
                        if (state == s_comment) {
                            buf.append("</font>");
                            state = s_normal;
                        }
                        break;
                    }
                default:
                    bufl++;
                    buf.append(ch);
            }
        }
        out.write("</pre>\r\n</body>\r\n</html>");
        in.close();
        out.close();
    }

    public static void main(String args[]) {
        if (args.length < 1 || args.length > 2) {
            System.out.println("java2html converter + syntax coloring + tabs2spaces");
            System.out.println("");
            System.out.println("java  [java_opt]  java2html  [properties_file]  source");
            System.out.println("");
            System.out.println("  - java is the name of the Java interpreter");
            System.out.println("  - java_opt are the options of the Java interpreter");
            System.out.println("  - java2html is the name of this application");
            System.out.println("  - properties_file (optional) is the path ");
            System.out.println("    of a file which has a structure like this:");
            System.out.println("        tabsize=number  (default value is 4)");
            System.out.println("        bgcolor=RRGGBB  (default value is C0C0C0) - background");
            System.out.println("        txcolor=RRGGBB  (default value is 000000) - source code");
            System.out.println("        kwcolor=RRGGBB  (default value is 0000F0) - keywords");
            System.out.println("        cmcolor=RRGGBB  (default value is A00000) - comments");
            System.out.println("  - source is a file or the directory to the Java source file(s)");
            System.out.println("");
            System.out.println("Examples:");
            System.out.println("    java  java2html  java2html.java");
            System.out.println("    java  java2html  C:\\TEMP");
            System.out.println("    java  java2html  java2html.properties  C:\\TEMP");
            System.exit(1);
        }
        String source, propfile;
        if (args.length == 2) {
            propfile = args[0];
            source = args[1];
        } else {
            propfile = "java2html.properties";
            source = args[0];
        }
        try {
            InputStream in = new FileInputStream(propfile);
            Properties prop = new Properties();
            prop.load(in);
            in.close();
            tabsize = Integer.parseInt(prop.getProperty("tabsize", "4"));
            bgcolor = "#" + prop.getProperty("bgcolor", bgcolor);
            txcolor = "#" + prop.getProperty("txcolor", txcolor);
            kwcolor = "#" + prop.getProperty("kwcolor", kwcolor);
            cmcolor = "#" + prop.getProperty("cmcolor", cmcolor);
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            System.out.println(e);
        } catch (NumberFormatException e) {
            System.out.println(e);
        }
        File f = new File(source);
        if (f.isFile()) {
            try {
                convert(f.getPath());
            } catch (IOException e) {
                System.out.println(e);
            }
        } else if (f.isDirectory()) {
            try {
                String src[] = f.list();
                for (String aSrc : src) {
                    if (aSrc.endsWith(".java")) {
                        convert(new File(f, aSrc).getPath());
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        } else {
            System.out.println("The source parameter must be an existent file or directory");
            System.out.println("Run java2html without parameters for help");
        }
    }
}
