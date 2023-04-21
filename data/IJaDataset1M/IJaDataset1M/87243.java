package com.yuchengtech.simpleServer.core;

import java.io.PushbackInputStream;
import java.io.InputStream;
import java.io.IOException;

public class InternetInputStream extends PushbackInputStream {

    public InternetInputStream(InputStream in, int size) {
        super(in, size);
    }

    public InternetInputStream(InputStream in) {
        super(in, 4096);
    }

    public String readline() throws IOException {
        StringBuffer buf = readBuffer();
        if (buf == null) return null;
        return buf.toString();
    }

    public StringBuffer readBuffer() throws IOException {
        StringBuffer buffer = null;
        int ch = -1;
        while ((ch = read()) >= 0) {
            if (buffer == null) {
                buffer = new StringBuffer();
            }
            if (ch == '\r') {
                ch = read();
                if (ch > 0 && ch != '\n') {
                    unread(ch);
                }
                break;
            } else if (ch == '\n') {
                break;
            }
            buffer.append((char) ch);
        }
        return buffer;
    }
}
