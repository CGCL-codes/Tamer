package qat.parser;

import java.io.*;

public class HtmlPrintStream extends PrintStream {

    public static final String BREAK = "<br>";

    public static final String RED = "#990000";

    public static final String GREEN = "#009900";

    public static final String BLUE = "#000099";

    public static final String PURPLE = "#993399";

    private boolean htmlMode = true;

    private PrintStream originalStream;

    public HtmlPrintStream(PrintStream originalStream, boolean useHtml) {
        super(originalStream, true);
        this.originalStream = originalStream;
        htmlMode = useHtml;
        if (htmlMode) this.print("<html><body>");
    }

    public void print(String color, String s) {
        if (htmlMode) originalStream.print("<font color=\"" + color + "\">" + s + "</font>"); else originalStream.print(s);
    }

    public void printBold(String s) {
        if (htmlMode) originalStream.print("<b>" + s + "</b>"); else originalStream.print(s);
    }

    public void println() {
        if (htmlMode) originalStream.println(BREAK); else originalStream.println();
    }

    public void println(boolean x) {
        if (htmlMode) originalStream.println(x + BREAK); else originalStream.println(x);
    }

    public void println(long x) {
        if (htmlMode) originalStream.println(x + BREAK); else originalStream.println(x);
    }

    public void println(int x) {
        if (htmlMode) originalStream.println(x + BREAK); else originalStream.println(x);
    }

    public void println(float x) {
        if (htmlMode) originalStream.println(x + BREAK); else originalStream.println(x);
    }

    public void println(double x) {
        if (htmlMode) originalStream.println(x + BREAK); else originalStream.println(x);
    }

    public void println(char[] x) {
        if (htmlMode) originalStream.println(new String(x) + BREAK); else originalStream.println(x);
    }

    public void println(Object x) {
        if (htmlMode) originalStream.println(x + BREAK); else originalStream.println(x);
    }

    public void println(String s) {
        if (htmlMode) originalStream.println(s + BREAK); else originalStream.println(s);
    }

    public void println(String color, String s) {
        if (htmlMode) originalStream.println("<font color=\"" + color + "\">" + s + "</font><br>"); else originalStream.println(s);
    }

    public void printBoldln(String s) {
        if (htmlMode) originalStream.println("<b>" + s + "</b><br>"); else originalStream.println(s);
    }

    public void close() {
        try {
            if (htmlMode) {
                originalStream.print("</body></html>");
            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            originalStream.close();
        }
    }
}
