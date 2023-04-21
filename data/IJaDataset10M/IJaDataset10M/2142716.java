package pl.edu.mimuw.xqtav.xqgen.xqgenerator_1;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 *
 * @author marchant
 */
public class XQueryWriter {

    protected PrintWriter pw = null;

    protected Stack completeStack = null;

    protected int currentIndent = 0;

    protected int increaseIndent = 2;

    /** Creates a new instance of XQueryWriter */
    public XQueryWriter(OutputStream dataOutput) {
        pw = new PrintWriter(dataOutput);
        completeStack = new Stack();
    }

    public void completeStackOps() {
        while (completeStack.size() > 0) {
            String s = (String) completeStack.pop();
            insertText(s);
            if (s.equals(")")) {
                decIndent();
            }
        }
    }

    public void close() {
        completeStackOps();
        pw.close();
        pw = null;
    }

    public static String escapeComment(String s) {
        return s.replaceAll("\\(:", "( :").replaceAll(":\\)", ": )");
    }

    public void printIndented(String data, boolean newLine) {
        for (int i = 0; i < currentIndent; i++) {
            pw.write(' ');
        }
        pw.write(data);
        if (newLine) pw.write('\n');
    }

    public void incIndent() {
        currentIndent += increaseIndent;
    }

    public void decIndent() {
        currentIndent -= increaseIndent;
        if (currentIndent < 0) currentIndent = 0;
    }

    /** API **/
    public void insertComment(String commentText) {
        printIndented("(: ***", true);
        incIndent();
        StringTokenizer tok = new StringTokenizer(commentText, "\n");
        while (tok.hasMoreTokens()) {
            printIndented(escapeComment(tok.nextToken()), true);
        }
        decIndent();
        printIndented("*** :)", true);
    }

    public void insertText(String text) {
        printIndented(text, true);
    }

    public void insertNamespaceDeclaration(String name, String uri) {
        printIndented("", false);
        pw.print("declare namespace ");
        pw.print(name);
        pw.print(" = \"");
        pw.print(uri);
        pw.println("\";");
    }

    public String escapeStringValue(String v) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < v.length(); i++) {
            char c = v.charAt(i);
            if (c == '&') {
                sb.append("&amp;");
                continue;
            }
            if (c == '"') {
                sb.append("&quot;");
                continue;
            }
            sb.append(c);
        }
        return sb.toString();
    }

    public void appendToStack(String value) {
        completeStack.push(value);
    }
}
