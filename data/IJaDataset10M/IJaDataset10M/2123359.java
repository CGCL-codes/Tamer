package org.jtools.io;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

public final class IndentWriter extends Writer implements Indentable {

    private final Writer out;

    private final int indent;

    private boolean indenting = true;

    private int level = 0;

    private boolean indented = false;

    private char[] indentation = null;

    private final char[] nlBuf;

    public IndentWriter(Writer out, int indent) {
        super(out);
        this.out = out;
        this.indent = indent;
        this.nlBuf = ((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).toCharArray();
    }

    @Override
    public void indented() {
        indenting = true;
    }

    @Override
    public void unindented() {
        indenting = false;
    }

    @Override
    public void indent() {
        level++;
    }

    @Override
    public void unindent() {
        level--;
    }

    public void print(String arg) {
        try {
            write(arg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printLn(String arg) {
        print(arg);
        printLn();
    }

    public void printLn() {
        print("\n");
    }

    protected void internalWrite(char[] cbuf, int off, int len) throws IOException {
        if (!indenting) indented = true;
        if (!indented) {
            if (indentation == null || indentation.length < level * indent) {
                indentation = new char[level * indent];
                Arrays.fill(indentation, ' ');
            }
            out.write(indentation, 0, level * indent);
            indented = true;
        }
        out.write(cbuf, off, len);
    }

    protected void internalNewline() throws IOException {
        out.write(nlBuf, 0, nlBuf.length);
        indented = false;
    }

    private boolean checkNewLine(char[] cbuf, int off) {
        for (int i = 0; i < nlBuf.length; i++) {
            if (cbuf.length <= off + i) return false;
            if (cbuf[off + i] != nlBuf[i]) return false;
        }
        return true;
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        for (int nl = 0; nl < len; nl++) {
            if (checkNewLine(cbuf, off + nl)) {
                if (nl > 0) internalWrite(cbuf, off, nl);
                internalNewline();
                if (nl + nlBuf.length < len) write(cbuf, off + nl + nlBuf.length, len - nl - nlBuf.length);
                return;
            }
        }
        internalWrite(cbuf, off, len);
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }

    @Override
    public void close() throws IOException {
        out.close();
    }
}
