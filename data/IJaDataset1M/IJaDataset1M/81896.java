package org.mikha.utils.nio;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

/**
 * An input stream that reads from a list of byte buffers. 
 * @author dmitrym
 */
public class ByteBufferInputStream extends InputStream {

    private final List<ByteBuffer> buffers = new LinkedList<ByteBuffer>();

    private int available = 0;

    private ByteBuffer currentBuffer = null;

    /**
     * Constructor.
     */
    public ByteBufferInputStream() {
    }

    /**
     * Constructor.
     * @param b buffer to read from
     */
    public ByteBufferInputStream(ByteBuffer b) {
        add(b);
    }

    /**
     * Constructor.
     * @param buffers list of buffers to read from
     */
    public ByteBufferInputStream(List<ByteBuffer> buffers) {
        addAll(buffers);
    }

    /**
     * Adds byte buffer to read from.
     * @param b byte buffer to read from
     */
    public void add(ByteBuffer b) {
        buffers.add(b);
        available += b.remaining();
        if (currentBuffer == null) {
            currentBuffer = buffers.get(0);
        }
    }

    /**
     * Add list of byte buffers to read from.
     * @param buffers list of byte buffers to read from
     */
    public void addAll(List<ByteBuffer> buffers) {
        for (ByteBuffer b : buffers) {
            add(b);
        }
    }

    @Override
    public int read() throws IOException {
        int r = checkEOF();
        if (r == -1) {
            return -1;
        }
        available--;
        return currentBuffer.get();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int read = 0;
        while (len > 0) {
            int s = checkEOF();
            if (s == -1) {
                if (read > 0) {
                    return read;
                }
                return -1;
            }
            if (s > len) {
                s = len;
            }
            currentBuffer.get(b, off, s);
            read += s;
            off += s;
            len -= s;
            available -= s;
        }
        return read;
    }

    @Override
    public int available() {
        return available;
    }

    private int checkEOF() {
        while (true) {
            if (currentBuffer == null) {
                return -1;
            }
            int r = currentBuffer.remaining();
            if (r > 0) {
                return r;
            }
            buffers.remove(0);
            currentBuffer = (buffers.size() > 0 ? buffers.get(0) : null);
        }
    }
}
