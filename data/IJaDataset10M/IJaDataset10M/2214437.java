package com.vividsolutions.jts.io;

import java.io.*;

/**
 * An adapter to allow an {@link InputStream} to be used as an {@link InStream}
 */
public class InputStreamInStream implements InStream {

    private InputStream is;

    public InputStreamInStream(InputStream is) {
        this.is = is;
    }

    public void read(byte[] buf) throws IOException {
        is.read(buf);
    }
}
