package org.wings.io;

import java.io.IOException;
import java.util.LinkedList;

/**
 * A Device, that buffers the data written to it to be
 * written to some other Device later (see {@link writeTo(Device)})
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision: 1759 $
 */
public final class DeviceBuffer implements Device {

    private byte[] buffer;

    private int position = 0;

    private int capacityIncrement;

    public DeviceBuffer(int initialCapacity, int capacityIncrement) {
        buffer = new byte[initialCapacity];
        this.capacityIncrement = capacityIncrement;
    }

    public boolean isSizePreserving() {
        return true;
    }

    /**
     * TODO: documentation
     *
     * @param initialCapacity
     * @return
     */
    public DeviceBuffer(int initialCapacity) {
        this(initialCapacity, -1);
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public DeviceBuffer() {
        this(2000);
    }

    public void flush() {
    }

    public void close() {
    }

    /**
     * Print a String.
     */
    public Device print(String s) throws IOException {
        if (s == null) return print("null");
        int len = s.length();
        for (int i = 0; i < len; i++) {
            write((byte) s.charAt(i));
        }
        return this;
    }

    /**
     * Print an array of chars.
     */
    public Device print(char[] c) throws IOException {
        return print(c, 0, c.length - 1);
    }

    /**
     * Print a character array.
     */
    public Device print(char[] c, int start, int len) throws IOException {
        final int end = start + len;
        for (int i = start; i < end; i++) print(c[i]);
        return this;
    }

    /**
     * Print an integer.
     */
    public Device print(int i) throws IOException {
        return print(String.valueOf(i));
    }

    /**
     * Print any Object
     */
    public Device print(Object o) throws IOException {
        if (o != null) return print(o.toString()); else return print("null");
    }

    /**
     * Print a character.
     */
    public Device print(char c) throws IOException {
        return print(String.valueOf(c));
    }

    /**
     * Writes the specified byte to this data output stream.
     */
    public Device write(int c) throws IOException {
        return print(String.valueOf(c));
    }

    /**
     * Writes b.length bytes from the specified byte array to this
     * output stream.
     *
     * @param b
     * @return
     * @throws IOException
     */
    public Device write(byte b) throws IOException {
        while (position + 1 > buffer.length) incrementCapacity();
        buffer[position++] = b;
        return this;
    }

    /**
     * Writes b.length bytes from the specified byte array to this
     * output stream.
     */
    public Device write(byte b[]) throws IOException {
        return write(b, 0, b.length);
    }

    /**
     * TODO: documentation
     *
     */
    public void clear() {
        position = 0;
    }

    private void incrementCapacity() {
        byte[] oldBuffer = buffer;
        int newCapacity = (capacityIncrement > 0) ? (buffer.length + capacityIncrement) : (buffer.length * 2);
        buffer = new byte[newCapacity];
        System.arraycopy(oldBuffer, 0, buffer, 0, position);
    }

    /**
     * Writes len bytes from the specified byte array starting at offset
     * off to this output stream.
     */
    public Device write(byte b[], int off, int len) throws IOException {
        while (position + len > buffer.length) incrementCapacity();
        System.arraycopy(b, off, buffer, position, len);
        position += len;
        return this;
    }

    /**
     * TODO: documentation
     *
     * @param d
     */
    public void writeTo(Device d) {
        try {
            d.write(buffer, 0, position);
        } catch (IOException ignore) {
        }
    }
}
