package javacream.io;

import java.io.OutputStream;
import java.io.InputStream;

/**
 * FastByteArrayOutputStream
 *
 * @author Glenn Powell
 *
 */
public class FastByteArrayOutputStream extends OutputStream {

    private byte[] buf = null;

    private int size = 0;

    public FastByteArrayOutputStream() {
        this(5 * 1024);
    }

    public FastByteArrayOutputStream(int initSize) {
        this.size = 0;
        this.buf = new byte[initSize];
    }

    private void verifyBufferSize(int sz) {
        if (sz > buf.length) {
            byte[] old = buf;
            buf = new byte[Math.max(sz, 2 * buf.length)];
            System.arraycopy(old, 0, buf, 0, old.length);
            old = null;
        }
    }

    public int getSize() {
        return size;
    }

    public byte[] getByteArray() {
        return buf;
    }

    public final void write(byte b[]) {
        verifyBufferSize(size + b.length);
        System.arraycopy(b, 0, buf, size, b.length);
        size += b.length;
    }

    public final void write(byte b[], int off, int len) {
        verifyBufferSize(size + len);
        System.arraycopy(b, off, buf, size, len);
        size += len;
    }

    public final void write(int b) {
        verifyBufferSize(size + 1);
        buf[size++] = (byte) b;
    }

    public void reset() {
        size = 0;
    }

    public InputStream getInputStream() {
        return new FastByteArrayInputStream(buf, size);
    }
}
