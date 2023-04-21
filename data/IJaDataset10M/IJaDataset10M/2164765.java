package web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.Filer;
import util.Maper;

public class Compiler {

    PrintWriter out;

    StringBuffer body;

    String encoding = "UTF-8";

    Map injectionLeft = new HashMap();

    Map injectionRight = new HashMap();

    Map declare = new HashMap();

    char[] html;

    static String view;

    final char BEGIN = '[';

    final char END = ']';

    public void compile(String packageName, String className, File htmlFile, File javaFile) throws UnsupportedEncodingException, FileNotFoundException {
        html = Filer.chars(htmlFile, encoding);
        if (view == null) view = Filer.string(getClass().getResourceAsStream("view"), encoding);
        declare.put("request", "");
        declare.put("response", "");
        declare.put("out", "");
        declare.put("i", "");
        declare.put("it", "");
        declare.put("a", "");
        long lastModified = htmlFile.lastModified();
        body = new StringBuffer(8192);
        body.append("print(out,\"");
        for (int i = 0; i < html.length; i++) {
            char c = html[i];
            switch(c) {
                case BEGIN:
                    i = exprStart(html, i + 1);
                    break;
                case '"':
                    body.append("\\\"");
                    break;
                case '\r':
                    body.append("\\r");
                    break;
                case '\n':
                    body.append("\\n");
                    break;
                case '>':
                    body.append(c);
                    Object o = injectionLeft.get(i);
                    if (o != null) {
                        body.append("\");\r\n");
                        body.append(o);
                        body.append("\r\nprint(out,\"");
                    }
                    break;
                case '<':
                    o = injectionRight.get(i);
                    if (o != null) {
                        body.append("\");\r\n");
                        body.append(o);
                        body.append("\r\nprint(out,\"");
                    }
                    body.append(c);
                    break;
                default:
                    body.append(c);
            }
        }
        body.append("\");");
        StringBuffer declares = new StringBuffer();
        for (Object entry : declare.values()) {
            if (entry.toString().length() > 0) declares.append(entry).append("\r\n");
        }
        HashMap values = new HashMap();
        values.put("packageName", packageName);
        values.put("className", className);
        values.put("lastModified", lastModified);
        values.put("declare", declares);
        values.put("body", body);
        values.put("encoding", encoding);
        StringBuffer sb = new StringBuffer(8192);
        Maper.format(view, values, sb);
        out = new PrintWriter(javaFile, encoding);
        out.print(format(sb));
        out.close();
        javaFile.setLastModified(lastModified);
        System.out.println("create java file " + javaFile.getPath());
    }

    private Object format(StringBuffer sb) {
        char[] r = sb.toString().toCharArray();
        sb.delete(0, sb.capacity());
        String tab = "";
        for (int i = 0, j; i < r.length; i++) {
            char c = r[i];
            switch(c) {
                case '\\':
                    i++;
                    break;
                case '"':
                    i++;
                    j = to(r, i, c);
                    char[] s = new char[j - i];
                    System.arraycopy(r, i, s, 0, s.length);
                    sb.append('"');
                    sb.append(s);
                    sb.append('"');
                    i = j;
                    break;
                case '\'':
                    sb.append('\'');
                    if (r[i++] == '\\') {
                        sb.append('\\');
                        i++;
                    }
                    sb.append(r[i++]);
                    sb.append('\'');
                    break;
                case ';':
                    sb.append(";\r\n");
                    sb.append(tab);
                    break;
                case '{':
                    tab += '\t';
                    sb.append("{\r\n");
                    sb.append(tab);
                    break;
                case '}':
                    tab = tab.substring(1);
                    sb.append("}\r\n");
                    sb.append(tab);
                    break;
                case '\t':
                case ' ':
                    int f = toWord(r, i, -1);
                    int e = toWord(r, i, +1);
                    if (Character.isLetterOrDigit(r[f]) && Character.isLetterOrDigit(r[e])) {
                        sb.append(' ');
                    }
                    i = e - 1;
                    break;
                case '\r':
                case '\n':
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb;
    }

    private int to(final char[] r, int i, final char d) {
        for (; ; i++) {
            char c = r[i];
            if (c == '\\') {
                i++;
            } else if (c == d) {
                return i;
            }
        }
    }

    private int toWord(final char[] r, int i, final int step) {
        for (; ; i += step) {
            char c = r[i];
            switch(c) {
                case ' ':
                case '\t':
                case '\r':
                case '\n':
                    break;
                default:
                    return i;
            }
        }
    }

    private int exprStart(char[] html, int i) {
        int j = to(i, END);
        if (j == i) {
            return j;
        }
        switch(html[i]) {
            case ':':
                inIterator(i + 1, j);
                break;
            case '#':
                include(i + 1, j);
                break;
            default:
                String expr = new String(html, i, j - i);
                parseExpr(expr);
        }
        return j;
    }

    private void include(int i, int j) {
        String page = new String(html, i, j - i);
        parseExpr("include(" + page + ",request,response)");
    }

    private int to(int k, char d) {
        while (true) {
            char c = html[k];
            if (c == d) {
                return k;
            } else {
                k++;
            }
        }
    }

    private int inIterator(int i, int j) {
        int l = j;
        for (; l < html.length; l++) {
            char c = html[l];
            if (c == '>') {
                String list = new String(html, i, j - i);
                declare("List", list);
                injectionLeft.put(l, "if(" + list + "!=null){\r\n" + "int i=0;\r\n" + "for(Iterator it=" + list + ".iterator();it.hasNext();i++){\r\n" + "Map a=(Map) it.next();");
                break;
            }
        }
        int count = 0;
        for (; l < html.length; l++) {
            char c = html[l];
            switch(c) {
                case '<':
                    char d = html[l + 1];
                    if (d == '/') {
                        count--;
                        if (count == -1) {
                            injectionRight.put(l, "}\r\n}");
                            break;
                        }
                    } else {
                        count++;
                    }
                    break;
                case '/':
                    if (html[l + 1] == '>') {
                        count--;
                    }
            }
        }
        return j;
    }

    Pattern pExpr = Pattern.compile("[\\w.]+");

    private String dotExpr(String a) {
        String[] s = a.split("\\.");
        String r = s[0];
        String type = s.length == 1 ? "String" : "Map";
        declare(type, r);
        for (int i = 1; i < s.length; i++) {
            r = "get(" + r + ",\"" + s[i] + "\")";
        }
        return r;
    }

    private CharSequence parseExpr(CharSequence expr) {
        body.append("\");\r\n");
        body.append("print(out,");
        Matcher m = pExpr.matcher(expr);
        int end = 0;
        while (m.find()) {
            String dotExpr = m.group();
            if (m.end() < expr.length() && expr.charAt(m.end()) == '(') {
                body.append(expr, end, m.end());
            } else {
                body.append(expr, end, m.start()).append(dotExpr(dotExpr));
            }
            end = m.end();
        }
        body.append(expr, end, expr.length());
        body.append(");\r\n");
        body.append("print(out,\"");
        return body;
    }

    private void declare(String type, String name) {
        if (null == declare.get(name)) {
            declare.put(name, type + " " + name + "=(" + type + ")request.getAttribute(\"" + name + "\");");
        }
    }
}
