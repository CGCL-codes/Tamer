package net.sf.kdgcommons.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *  A decorator <code>InputStream</code> that writes all bytes read to a
 *  specified <code>OutputStream</code>.
 */
public class TeeInputStream extends InputStream {

    private InputStream _base;

    private OutputStream _tee;

    public TeeInputStream(InputStream base, OutputStream tee) {
        _base = base;
        _tee = tee;
    }

    /**
     *  Returns the number of bytes available from the base stream. Has
     *  no effect on the tee.
     */
    @Override
    public int available() throws IOException {
        return _base.available();
    }

    /**
     *  Closes the base stream, but <em>not</em> the tee.
     */
    @Override
    public void close() throws IOException {
        _base.close();
    }

    /**
     *  Reads a single byte from the base, writing it to the tee. If at
     *  end-of-file on the base, has no effect on the tee.
     */
    @Override
    public int read() throws IOException {
        int b = _base.read();
        if (b >= 0) _tee.write(b);
        return b;
    }

    /**
     *  Reads multiple bytes from the base, into an arbitrary position
     *  in the specified buffer, and writes the same bytes to the tee.
     *  If no bytes read, has no effect on the tee.
     */
    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int count = _base.read(b, off, len);
        if (count > 0) _tee.write(b, off, count);
        return count;
    }

    /**
     *  Reads as many bytes as possible from the base, up to the size of
     *  the passed buffer, and writes them to the tee. If no bytes read,
     *  has no effect on the tee.
     */
    @Override
    public int read(byte[] b) throws IOException {
        int count = _base.read(b);
        if (count > 0) _tee.write(b, 0, count);
        return count;
    }

    /**
     *  Returns whether the base stream supports {@link #mark}. Has no
     *  effect on the tee.
     */
    @Override
    public boolean markSupported() {
        return _base.markSupported();
    }

    /**
     *  Sets a mark on the base stream. Has no effect on the tee.
     */
    @Override
    public synchronized void mark(int readlimit) {
        _base.mark(readlimit);
    }

    /**
     *  Resets the base stream to a preset mark. Has no effect on
     *  the tee.
     */
    @Override
    public synchronized void reset() throws IOException {
        _base.reset();
    }

    /**
     *  Skips a specified number of bytes in the input stream. Has
     *  no effect on the tee.
     */
    @Override
    public long skip(long n) throws IOException {
        return _base.skip(n);
    }
}
