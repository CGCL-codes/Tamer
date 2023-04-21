package com.netx.generics.R1.util;

import java.io.Writer;
import java.io.OutputStream;

public class HtmlWriter extends CountingWriter {

    public HtmlWriter(Writer out) {
        super(out);
    }

    public HtmlWriter(Writer out, boolean autoFlush) {
        super(out, autoFlush);
    }

    public HtmlWriter(OutputStream out) {
        super(out);
    }

    public HtmlWriter(OutputStream out, boolean autoFlush) {
        super(out, autoFlush);
    }

    public void print(String s) {
        if (s != null) {
            super.print(Tools.toHTML(s));
        }
    }

    public void println() {
        super.print("<br>");
        super.println();
    }
}
