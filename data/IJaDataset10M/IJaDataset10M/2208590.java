package net.cattaka.util;

import java.io.IOException;
import java.io.InputStream;

/**
 * XMLDecoderなどは勝手にストリームをクローズするのでその対策用。
 * 
 * @author cattaka
 */
public class UncloseableInputStream extends InputStream {

    private InputStream in;

    public UncloseableInputStream(InputStream in) {
        super();
        this.in = in;
    }

    public int available() throws IOException {
        return in.available();
    }

    public void close() throws IOException {
    }

    public boolean equals(Object obj) {
        return in.equals(obj);
    }

    public int hashCode() {
        return in.hashCode();
    }

    public void mark(int readlimit) {
        in.mark(readlimit);
    }

    public boolean markSupported() {
        return in.markSupported();
    }

    public int read() throws IOException {
        return in.read();
    }

    public int read(byte[] b, int off, int len) throws IOException {
        return in.read(b, off, len);
    }

    public int read(byte[] b) throws IOException {
        return in.read(b);
    }

    public void reset() throws IOException {
        in.reset();
    }

    public long skip(long n) throws IOException {
        return in.skip(n);
    }

    public String toString() {
        return in.toString();
    }
}
