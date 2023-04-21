package org.unitils.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * Wrapper that makes an Reader available as an InputStream 
 */
public class ReaderInputStream extends InputStream {

    private Reader reader;

    public ReaderInputStream(Reader reader) {
        this.reader = reader;
    }

    public int read() throws IOException {
        return reader.read();
    }
}
