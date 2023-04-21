package com.vsetec.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class StreamReader extends Reader {

    protected final InputStream stream;

    public StreamReader(InputStream stream) {
        this.stream = stream;
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        byte[] buf = new byte[cbuf.length * 2];
        int ret = stream.read(buf, 0, len * 2);
        for (int b = 0, c = off; b < ret; c++, b++) {
            int ch = (buf[b] & 0xFF) << 8;
            b++;
            ch |= buf[b] & 0xFF;
            cbuf[c] = (char) ch;
        }
        return ret;
    }

    @Override
    public void close() throws IOException {
        stream.close();
    }
}
