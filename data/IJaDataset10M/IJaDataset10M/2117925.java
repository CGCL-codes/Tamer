package org.synthful.gwt.rpc.server;

import java.io.IOException;
import javax.servlet.jsp.JspWriter;

public class JspResponseWriter extends JspWriter {

    public JspResponseWriter() {
        super(0, true);
        this.Body = new StringBuffer();
    }

    @Override
    public void clear() throws IOException {
        this.Body = new StringBuffer();
    }

    @Override
    public void clearBuffer() throws IOException {
        this.Body.setLength(0);
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public void flush() throws IOException {
    }

    @Override
    public int getRemaining() {
        return 0;
    }

    @Override
    public void newLine() throws IOException {
        this.Body.append('\n');
    }

    @Override
    public void print(boolean arg0) throws IOException {
        this.Body.append('\n');
    }

    @Override
    public void print(char arg0) throws IOException {
        this.Body.append(arg0);
    }

    @Override
    public void print(int arg0) throws IOException {
        this.Body.append(arg0);
    }

    @Override
    public void print(long arg0) throws IOException {
        this.Body.append(arg0);
    }

    @Override
    public void print(float arg0) throws IOException {
    }

    @Override
    public void print(double arg0) throws IOException {
        this.Body.append(arg0);
    }

    @Override
    public void print(char[] arg0) throws IOException {
        this.Body.append(arg0);
    }

    @Override
    public void print(String arg0) throws IOException {
        this.Body.append(arg0);
    }

    @Override
    public void print(Object arg0) throws IOException {
        this.Body.append(arg0);
    }

    @Override
    public void println() throws IOException {
        this.newLine();
    }

    @Override
    public void println(boolean arg0) throws IOException {
        this.Body.append(arg0);
        this.newLine();
    }

    @Override
    public void println(char arg0) throws IOException {
        this.Body.append(arg0);
        this.newLine();
    }

    @Override
    public void println(int arg0) throws IOException {
        this.Body.append(arg0);
        this.newLine();
    }

    @Override
    public void println(long arg0) throws IOException {
    }

    @Override
    public void println(float arg0) throws IOException {
        this.Body.append(arg0);
        this.newLine();
    }

    @Override
    public void println(double arg0) throws IOException {
        this.Body.append(arg0);
        this.newLine();
    }

    @Override
    public void println(char[] arg0) throws IOException {
        this.Body.append(arg0);
        this.newLine();
    }

    @Override
    public void println(String arg0) throws IOException {
        this.Body.append(arg0);
        this.newLine();
    }

    @Override
    public void println(Object arg0) throws IOException {
        this.Body.append(arg0);
        this.newLine();
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        for (int i = off; i < off + len; i++) this.Body.append(cbuf[i]);
    }

    public String getBody() {
        return this.Body.toString();
    }

    protected StringBuffer Body;
}
