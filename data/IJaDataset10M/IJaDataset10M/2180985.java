package com.blogspot.qbeukes.addp.packets;

import java.nio.ByteBuffer;

/**
 *
 * @author quintin
 */
public class BufferParser {

    private static final int NO_LIMIT = -1;

    private ByteBuffer buffer;

    private int limit = -1;

    public BufferParser(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    /**
   * Set the read limit on the ByteBuffer for this parser instance. This limit
   * will apply from the time it's set. So after the limit is set to N, only
   * N more bytes can be read. You can not reconfigure the limit again.
   *
   * @param limit The number of bytes that may still be read.
   */
    public void setLimit(int limit) {
        if (this.limit != NO_LIMIT) {
            throw new IllegalStateException("You can't reconfigure the limit.");
        }
        this.limit = buffer.position() + limit;
    }

    /**
   * Checks if there are less than the specified number of bytes available before
   * the limit is reached. If not limit is configured this call will depend on the
   * data available in the byte buffer.
   *
   * @param len
   * @return True if the limit would be exceeded.
   */
    protected boolean exceedLimit(int len) {
        return (buffer.position() + len) > (limit == NO_LIMIT ? buffer.limit() : limit);
    }

    /**
   * @return the number of bytes available to read
   */
    public int getBytesAvailable() {
        return (limit == NO_LIMIT) ? buffer.remaining() : limit - buffer.position();
    }

    /**
   * @return True if there is still data available to parse
   */
    public boolean isDataAvailable() {
        return (limit == NO_LIMIT) ? buffer.hasRemaining() : limit > buffer.position();
    }

    /**
   * Read a number of bytes 
   *
   * @param len Number of bytes to read
   * @return Array of bytes retrieved from the buffer
   */
    public byte[] readBytes(int len) {
        if (exceedLimit(len)) {
            throw new IllegalArgumentException("This call would exceed the buffers limit.");
        }
        byte[] d = new byte[len];
        buffer.get(d);
        return d;
    }

    /**
   * Read a single byte
   * 
   * @return Byte read from buffer
   */
    public byte readByte() {
        if (exceedLimit(1)) {
            throw new IllegalArgumentException("This call would exceed the buffers limit.");
        }
        return buffer.get();
    }
}
