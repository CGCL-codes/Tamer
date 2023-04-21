package org.jpc.classfile;

import java.io.*;

/**
 * An <code>OutputStream</code> implementation that limits the total number of
 * bytes written to 64K, the size limit of a method.
 * @author Chris Dennis
 */
public class MethodOutputStream extends OutputStream {

    private final OutputStream backing;

    private int count;

    /**
     * Constructs a instance wrapping an already existing <code>OutputStream</code>
     * instance.
     * @param out <code>OutputStream</code> to be wrapped
     */
    public MethodOutputStream(OutputStream out) {
        backing = out;
        count = 0;
    }

    /**
     * Returns the number of bytes written so far
     * @return bytes written
     */
    public int position() {
        return count;
    }

    public void close() throws IOException {
        backing.close();
    }

    public void flush() throws IOException {
        backing.flush();
    }

    /**
     * @throws IllegalStateException if stream limit is exceeded
     */
    public void write(byte[] b) throws IOException {
        backing.write(b);
        count += b.length;
        if (count >= ClassFile.MAX_METHOD_CODE_SIZE) throw new IllegalStateException("Oversize Method");
    }

    /**
     * @throws IllegalStateException if stream limit is exceeded
     */
    public void write(byte[] b, int off, int len) throws IOException {
        backing.write(b, off, len);
        count += len;
        if (count >= ClassFile.MAX_METHOD_CODE_SIZE) throw new IllegalStateException("Oversize Method");
    }

    /**
     * @throws IllegalStateException if stream limit is exceeded
     */
    public void write(int b) throws IOException {
        backing.write(b);
        count++;
        if (count >= ClassFile.MAX_METHOD_CODE_SIZE) throw new IllegalStateException("Oversize Method");
    }
}
