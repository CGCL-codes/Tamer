package cck.text;

import java.io.PrintStream;

public class Printer {

    private final PrintStream o;

    private boolean begLine;

    private int listdepth;

    private boolean first;

    private boolean nlcomma;

    private int indent;

    public static final Printer STDOUT = new Printer(System.out);

    public static final Printer STDERR = new Printer(System.out);

    public Printer(PrintStream o) {
        this.o = o;
        this.begLine = true;
    }

    public void println(String s) {
        spaces();
        if (listdepth > 0) {
            if (!first) o.print(", ");
        }
        first = false;
        o.println(s);
        begLine = true;
        first = false;
    }

    public void print(String s) {
        spaces();
        if (listdepth > 0) {
            if (!first) o.print(", ");
        }
        first = false;
        o.print(s);
    }

    public void nextln() {
        if (!begLine) {
            o.print("\n");
            begLine = true;
        }
    }

    public void indent() {
        indent++;
    }

    public void spaces() {
        if (begLine) {
            for (int cntr = 0; cntr < indent; cntr++) o.print("    ");
            begLine = false;
        }
    }

    public void unindent() {
        indent--;
        if (indent < 0) indent = 0;
    }

    public void startblock() {
        println("{");
        indent();
    }

    public void startblock(String name) {
        println(name + " {");
        indent();
    }

    public void endblock() {
        unindent();
        println("}");
    }

    public void endblock(String s) {
        unindent();
        println('}' + s);
    }

    public void close() {
        o.close();
    }

    public void beginList(String beg) {
        print(beg);
        listdepth++;
        first = true;
    }

    public void beginList() {
        listdepth++;
        first = true;
    }

    public void endList(String end) {
        listdepth--;
        if (listdepth < 0) listdepth = 0;
        print(end);
    }

    public void endListln(String end) {
        listdepth--;
        if (listdepth < 0) listdepth = 0;
        println(end);
    }

    public void endList() {
        listdepth--;
        if (listdepth < 0) listdepth = 0;
    }

    public void endListln() {
        listdepth--;
        if (listdepth < 0) listdepth = 0;
        nextln();
    }
}
