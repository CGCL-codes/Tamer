package info.monitorenter.cpdetector.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * Prints m_out every character read. Use as a proxy. Only needed for debugging the ANTLR Parser (ParsingDetector).
 * Therefore the chronological order is preserved: sun's StreamDecoder.CharsetDS (nio) replaces InputStream.read() by
 * buffer operations that fetch complete arrays. This is avoided by allowing to fetch only one char per method call.
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 * 
 */
public class InputStreamDebug extends InputStream {

    private InputStream m_delegate;

    public InputStreamDebug(InputStream delegate) {
        this.m_delegate = delegate;
    }

    public synchronized int read() throws IOException {
        int ret = this.m_delegate.read();
        System.out.print((char) ret);
        return ret;
    }

    /**
     * 
     */
    public int available() throws IOException {
        return m_delegate.available();
    }

    /**
     * 
     */
    public void close() throws IOException {
        m_delegate.close();
    }

    public boolean equals(Object obj) {
        return m_delegate.equals(obj);
    }

    public int hashCode() {
        return m_delegate.hashCode();
    }

    /**
     * 
     */
    public void mark(int readlimit) {
        m_delegate.mark(readlimit);
    }

    /**
     * 
     */
    public boolean markSupported() {
        return m_delegate.markSupported();
    }

    /**
     * 
     */
    public int read(byte[] b) throws IOException {
        return this.read(b, 0, b.length);
    }

    /**
     * 
     */
    public int read(byte[] b, int off, int len) throws IOException {
        int ret = this.read();
        if (ret != -1) {
            b[off] = (byte) ret;
            ret = 1;
        }
        return ret;
    }

    /**
     * 
     */
    public void reset() throws IOException {
        m_delegate.reset();
    }

    /**
     * 
     */
    public long skip(long n) throws IOException {
        return m_delegate.skip(n);
    }

    public String toString() {
        return m_delegate.toString();
    }
}
