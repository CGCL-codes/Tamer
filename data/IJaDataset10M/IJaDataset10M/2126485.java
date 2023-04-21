package com.volantis.shared.io;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

public class CachingOutputStream extends FilterOutputStream {

    /**
     * The cache of what was written.
     */
    private ByteArrayOutputStream cache = new ByteArrayOutputStream();

    public CachingOutputStream(OutputStream out) {
        super(out);
    }

    public void write(int c) throws IOException {
        out.write(c);
        cache.write(c);
    }

    public void write(byte cbuf[], int off, int len) throws IOException {
        out.write(cbuf, off, len);
        cache.write(cbuf, off, len);
    }

    /**
     * Return the content of the cache as a character array.
     *
     * @return the content of the cache as a character array.
     */
    public byte[] toByteArray() {
        return cache.toByteArray();
    }

    /**
     * Return a reader to the cache content.
     *
     * @return a reader to the cache content.
     */
    public InputStream getCacheInputStream() {
        return new ByteArrayInputStream(cache.toByteArray());
    }

    public void close() throws IOException {
        out.close();
        cache.close();
    }

    public void flush() throws IOException {
        out.flush();
        cache.flush();
    }
}
