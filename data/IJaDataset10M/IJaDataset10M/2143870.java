package org.vexi.format;

import java.io.IOException;
import java.io.Reader;

public class LineTrapReader extends Reader {

    Reader r;

    StringBuffer line = new StringBuffer(100);

    public LineTrapReader(Reader r) {
        this.r = r;
    }

    @Override
    public void close() throws IOException {
        r.close();
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        for (int i = 0; i < len; i++) {
            int c = read();
            if (c == -1) {
                if (i == 0) return -1;
                return i;
            }
            cbuf[off + i] = (char) c;
        }
        return 0;
    }

    @Override
    public int read() throws IOException {
        int i = r.read();
        if (i == -1) return i;
        char c = (char) i;
        if (c == '\n') {
            lineFinished();
            line = new StringBuffer(100);
        } else {
            line.append(c);
        }
        return i;
    }

    protected void lineFinished() {
    }

    public String currentLine() {
        return line.toString();
    }
}
