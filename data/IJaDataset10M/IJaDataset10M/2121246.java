package org.pdfbox.io;

import java.io.IOException;
import java.util.Arrays;

/**
 * An interface to allow PDF files to be stored completely in memory.
 *
 * @author <a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>
 * @version $Revision: 1.2 $
 */
public class RandomAccessBuffer implements RandomAccess {

    private static final int EXTRA_SPACE = 16384;

    private byte[] buffer;

    private long pointer;

    private long size;

    /**
     * Default constructor.
     */
    public RandomAccessBuffer() {
        buffer = new byte[EXTRA_SPACE];
        pointer = 0;
        size = EXTRA_SPACE;
    }

    /**
     * {@inheritDoc}
     */
    public void close() throws IOException {
        buffer = null;
        pointer = 0;
        size = 0;
    }

    /**
     * {@inheritDoc}
     */
    public void seek(long position) throws IOException {
        this.pointer = position;
    }

    /**
     * {@inheritDoc}
     */
    public int read() throws IOException {
        if (pointer >= this.size) {
            return -1;
        }
        int result = buffer[(int) pointer];
        pointer++;
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public int read(byte[] b, int offset, int length) throws IOException {
        if (pointer >= this.size) {
            return 0;
        }
        int maxLength = (int) Math.min(length, this.size - pointer);
        System.arraycopy(buffer, (int) pointer, b, offset, maxLength);
        pointer += maxLength;
        return maxLength;
    }

    /**
     * {@inheritDoc}
     */
    public long length() throws IOException {
        return size;
    }

    /**
     * {@inheritDoc}
     */
    public void write(int b) throws IOException {
        write(new byte[] { (byte) b }, 0, 1);
    }

    /**
     * {@inheritDoc}
     */
    public void write(byte[] b, int offset, int length) throws IOException {
        if (pointer + length >= buffer.length) {
            byte[] temp = new byte[buffer.length + length + EXTRA_SPACE];
            Arrays.fill(temp, (byte) 0);
            System.arraycopy(buffer, 0, temp, 0, (int) this.size);
            buffer = temp;
        }
        System.arraycopy(b, offset, buffer, (int) pointer, length);
        pointer += length;
        if (pointer > this.size) {
            this.size = pointer;
        }
    }
}
