package de.psychomatic.applicationtools.io;

import java.nio.ByteBuffer;

public class RingBuffer {

    protected static final int DEFAULT_BUFFER_SIZE = 2048;

    protected volatile int bufferSize = 0;

    protected ByteBuffer buffer;

    protected volatile int putHere = 0;

    protected volatile int getHere = 0;

    protected volatile boolean eof = false;

    /**
     * Constructor.
     * @param size  The size of the ring buffer
     */
    public RingBuffer(final int size) {
        bufferSize = size;
        buffer = ByteBuffer.allocate(size);
    }

    /**
     * Constructor.
     */
    public RingBuffer() {
        this(DEFAULT_BUFFER_SIZE);
    }

    /**
     * Return the size of the ring buffer.
     * @return  The ring buffer size
     */
    public synchronized int size() {
        return buffer.capacity();
    }

    /**
     * return the space avaiable for writing.
     * @return The byte that may be written to the ring buffer
     */
    public synchronized int putAvailable() {
        if (putHere == getHere) {
            return bufferSize - 1;
        }
        if (putHere < getHere) {
            return getHere - putHere - 1;
        }
        return bufferSize - (putHere - getHere) - 1;
    }

    /**
     * Empty the ring buffer.
     */
    public synchronized void empty() {
        putHere = 0;
        getHere = 0;
    }

    /**
     * Put data into the ring buffer.
     * @param data  The data to write
     * @param offset    The start position in the data array
     * @param len   The bytes from the data array to write
     */
    public synchronized void put(final byte[] data, final int offset, final int len) {
        if (len == 0) {
            return;
        }
        if (putHere >= getHere) {
            final int l = Math.min(len, bufferSize - putHere);
            buffer.position(putHere);
            buffer.put(data, offset, l);
            putHere += l;
            if (putHere >= bufferSize) {
                putHere = 0;
            }
            if (len > l) {
                put(data, offset + l, len - l);
            }
        } else {
            final int l = Math.min(len, getHere - putHere - 1);
            buffer.position(putHere);
            buffer.put(data, offset, l);
            putHere += l;
            if (putHere >= bufferSize) {
                putHere = 0;
            }
        }
    }

    /**
     * Return the bytes available for reading.
     * @return The number of bytes that may be read from the ring buffer
     */
    public synchronized int getAvailable() {
        if (putHere == getHere) {
            return 0;
        }
        if (putHere < getHere) {
            return bufferSize - (getHere - putHere);
        }
        return putHere - getHere;
    }

    /**
     * Read data from the ring buffer.
     * @param data  Where to put the data
     * @param offset    The offset into the data array to start putting data
     * @param len   The maximum data to read
     * @return  The number of bytes read
     */
    public synchronized int get(final byte[] data, final int offset, int len) {
        if (len == 0) {
            return 0;
        }
        int dataLen = 0;
        if (eof && getAvailable() == 0) {
            return -1;
        }
        len = Math.min(len, getAvailable());
        if (getHere < putHere) {
            final int l = Math.min(len, putHere - getHere);
            buffer.position(getHere);
            buffer.get(data, offset, l);
            getHere += l;
            if (getHere >= bufferSize) {
                getHere = 0;
            }
            dataLen = l;
        } else {
            final int l = Math.min(len, bufferSize - getHere);
            buffer.position(getHere);
            buffer.get(data, offset, l);
            getHere += l;
            if (getHere >= bufferSize) {
                getHere = 0;
            }
            dataLen = l;
            if (len > l) {
                dataLen += get(data, offset + l, len - l);
            }
        }
        return dataLen;
    }

    /**
     * Return EOF status.
     * @return True if EOF.
     */
    public boolean isEOF() {
        return eof;
    }

    /**
     * Set the EOF status.
     * @param eof The eof to set.
     */
    public void setEOF(final boolean eof) {
        this.eof = eof;
    }

    /**
     * Test main routine.
     * @param args  Not used
     */
    public static void main(final String[] args) {
        final RingBuffer r = new RingBuffer(9);
        final byte[] b = new String("ABCDEFG").getBytes();
        final byte[] g = new byte[3];
        System.out.println("Start");
        r.put(b, 0, 3);
        r.get(g, 0, 3);
        System.out.println(new String(g));
        r.put(b, 0, 3);
        r.get(g, 0, 3);
        System.out.println(new String(g));
        r.put(b, 0, 3);
        r.get(g, 0, 3);
        System.out.println(new String(g));
        r.put(b, 0, 3);
        r.get(g, 0, 3);
        System.out.println(new String(g));
        r.put(b, 0, 3);
        r.get(g, 0, 2);
        System.out.println(new String(g));
        r.put(b, 0, 3);
        r.get(g, 0, 2);
        System.out.println(new String(g));
        r.put(b, 0, 3);
        r.get(g, 0, 3);
        System.out.println(new String(g));
        r.put(b, 0, 3);
        r.get(g, 0, 3);
        System.out.println(new String(g));
    }
}
