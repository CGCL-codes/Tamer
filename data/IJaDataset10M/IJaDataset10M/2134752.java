package com.netx.generics.util;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

public class CountingWriter extends PrintWriter {

    private long _numCharsWritten = 0;

    public CountingWriter(Writer out) {
        super(out);
    }

    public CountingWriter(Writer out, boolean autoFlush) {
        super(out, autoFlush);
    }

    public CountingWriter(OutputStream out) {
        super(out);
    }

    public CountingWriter(OutputStream out, boolean autoFlush) {
        super(out, autoFlush);
    }

    public void print(String s) {
        if (s != null) {
            _numCharsWritten += s.length();
        } else {
            _numCharsWritten += 4;
        }
        super.print(s);
    }

    public void println() {
        _numCharsWritten++;
        super.println();
    }

    public long getWrittenCharCount() {
        return _numCharsWritten;
    }

    public void print(Object o) {
        if (o != null) {
            print(o.toString());
        } else {
            print("null");
        }
    }

    public void print(boolean b) {
        print(new Boolean(b).toString());
    }

    public void print(char c) {
        print(new Character(c).toString());
    }

    public void print(char[] s) {
        print(new String(s));
    }

    public void print(byte b) {
        print(new Byte(b).toString());
    }

    public void print(short s) {
        print(new Short(s).toString());
    }

    public void print(int i) {
        print(new Integer(i).toString());
    }

    public void print(long l) {
        print(new Long(l).toString());
    }

    public void print(float f) {
        print(new Float(f).toString());
    }

    public void print(double d) {
        print(new Double(d).toString());
    }
}
