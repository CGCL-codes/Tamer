package com.emc.esu.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * Wraps the HTTP input stream and connection object so that 
 * when the stream is closed the connection can be closed as well.
 * 
 * @author Jason
 */
public class HttpInputStreamWrapper extends InputStream {

    private InputStream in;

    private HttpURLConnection con;

    public HttpInputStreamWrapper(InputStream in, HttpURLConnection con) {
        this.in = in;
        this.con = con;
    }

    @Override
    public int available() throws IOException {
        return in.available();
    }

    @Override
    public void close() throws IOException {
        try {
            in.close();
        } finally {
            con.disconnect();
        }
    }

    @Override
    public synchronized void mark(int readlimit) {
        in.mark(readlimit);
    }

    @Override
    public boolean markSupported() {
        return in.markSupported();
    }

    @Override
    public int read() throws IOException {
        return in.read();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return in.read(b, off, len);
    }

    @Override
    public int read(byte[] b) throws IOException {
        return in.read(b);
    }

    @Override
    public synchronized void reset() throws IOException {
        in.reset();
    }

    @Override
    public long skip(long n) throws IOException {
        return in.skip(n);
    }

    @Override
    public boolean equals(Object obj) {
        return in.equals(obj);
    }

    @Override
    public int hashCode() {
        return in.hashCode();
    }

    @Override
    public String toString() {
        return in.toString();
    }
}
