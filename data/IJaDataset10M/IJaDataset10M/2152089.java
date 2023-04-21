package org.openkonnect.interceptor.openbravo.PrinterReports;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Locale;

public class InterceptorPrintWriter extends PrintWriter {

    private PrintWriter delegate;

    private StringBuilder buffy;

    public InterceptorPrintWriter(Writer out) {
        super(out);
        delegate = (PrintWriter) out;
        buffy = new StringBuilder();
    }

    public InterceptorPrintWriter(OutputStream os) {
        super(os);
        delegate = new PrintWriter(os);
        buffy = new StringBuilder();
    }

    public StringBuilder getBuffy() {
        return buffy;
    }

    public PrintWriter append(char c) {
        buffy.append(c);
        return delegate.append(c);
    }

    public PrintWriter append(CharSequence csq, int start, int end) {
        buffy.append(csq, start, end);
        return delegate.append(csq, start, end);
    }

    public PrintWriter append(CharSequence csq) {
        buffy.append(csq);
        return delegate.append(csq);
    }

    public boolean checkError() {
        return delegate.checkError();
    }

    public void close() {
        delegate.close();
    }

    public boolean equals(Object obj) {
        return delegate.equals(obj);
    }

    public void flush() {
        delegate.flush();
    }

    public PrintWriter format(Locale l, String format, Object... args) {
        PrintWriter pw = delegate.format(l, format, args);
        buffy.append(pw.toString());
        return pw;
    }

    public PrintWriter format(String format, Object... args) {
        PrintWriter pw = delegate.format(format, args);
        buffy.append(pw.toString());
        return pw;
    }

    public int hashCode() {
        return delegate.hashCode();
    }

    public void print(boolean b) {
        buffy.append(b);
        delegate.print(b);
    }

    public void print(char c) {
        buffy.append(c);
        delegate.print(c);
    }

    public void print(char[] s) {
        buffy.append(s);
        delegate.print(s);
    }

    public void print(double d) {
        buffy.append(d);
        delegate.print(d);
    }

    public void print(float f) {
        buffy.append(f);
        delegate.print(f);
    }

    public void print(int i) {
        buffy.append(i);
        delegate.print(i);
    }

    public void print(long l) {
        buffy.append(l);
        delegate.print(l);
    }

    public void print(Object obj) {
        buffy.append(obj);
        delegate.print(obj);
    }

    public void print(String s) {
        buffy.append(s);
        delegate.print(s);
    }

    public PrintWriter printf(Locale l, String format, Object... args) {
        PrintWriter pw = delegate.printf(l, format, args);
        buffy.append(pw.toString());
        return pw;
    }

    public PrintWriter printf(String format, Object... args) {
        PrintWriter pw = delegate.printf(format, args);
        buffy.append(pw.toString());
        return pw;
    }

    public void println() {
        buffy.append("\n");
        delegate.println();
    }

    public void println(boolean x) {
        buffy.append(x);
        buffy.append("\n");
        delegate.println(x);
    }

    public void println(char x) {
        buffy.append(x);
        buffy.append("\n");
        delegate.println(x);
    }

    public void println(char[] x) {
        buffy.append(x);
        buffy.append("\n");
        delegate.println(x);
    }

    public void println(double x) {
        buffy.append(x);
        buffy.append("\n");
        delegate.println(x);
    }

    public void println(float x) {
        buffy.append(x);
        buffy.append("\n");
        delegate.println(x);
    }

    public void println(int x) {
        buffy.append(x);
        buffy.append("\n");
        delegate.println(x);
    }

    public void println(long x) {
        buffy.append(x);
        buffy.append("\n");
        delegate.println(x);
    }

    public void println(Object x) {
        buffy.append(x);
        buffy.append("\n");
        delegate.println(x);
    }

    public void println(String x) {
        buffy.append(x);
        buffy.append("\n");
        delegate.println(x);
    }

    public String toString() {
        return delegate.toString();
    }

    public void write(char[] buf, int off, int len) {
        buffy.append(buf, off, len);
        delegate.write(buf, off, len);
    }

    public void write(char[] buf) {
        buffy.append(buf);
        delegate.write(buf);
    }

    public void write(int c) {
        buffy.append(c);
        delegate.write(c);
    }

    public void write(String s, int off, int len) {
        buffy.append(s, off, len);
        delegate.write(s, off, len);
    }

    public void write(String s) {
        buffy.append(s);
        delegate.write(s);
    }
}
