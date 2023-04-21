package com.hs.mail.io;

import java.io.IOException;
import java.io.InputStream;

public class CountingInputStream extends InputStream {

    private InputStream in;

    private int lineCount;

    private int octetCount;

    public CountingInputStream(InputStream in) {
        super();
        this.in = in;
    }

    @Override
    public int read() throws IOException {
        int next = in.read();
        if (next > 0) {
            octetCount++;
            if (next == '\r') {
                lineCount++;
            }
        }
        return next;
    }

    public int getLineCount() {
        return lineCount;
    }

    public int getOctetCount() {
        return octetCount;
    }

    public void readAll() throws IOException {
        while (read() > 0) ;
    }
}
