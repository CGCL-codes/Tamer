package de.bea.domingo.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * Utility for making deep copies (vs. clone()'s shallow copies) of objects. Objects are first serialized and then
 * deserialized. Error checking is fairly minimal in this implementation. If an object is encountered that cannot be
 * serialized (or that references an object that cannot be serialized) an error is printed to System.err and null is
 * returned.
 */
public final class DeepCopy {

    /**
     * Private Constructor.
     */
    private DeepCopy() {
    }

    /**
     * @param orig the original object to copy
     * @return a copy of the object, or null if the object cannot be serialized.
     */
    public static Object copy(final Object orig) {
        Object obj = null;
        try {
            FastByteArrayOutputStream fbos = new FastByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(fbos);
            out.writeObject(orig);
            out.flush();
            out.close();
            ObjectInputStream in = new ObjectInputStream(fbos.getInputStream());
            obj = in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        return obj;
    }

    /**
     * ByteArrayInputStream implementation that does not synchronize methods.
     */
    private static final class FastByteArrayInputStream extends InputStream {

        /**
         * Our byte buffer
         */
        private byte[] buf = null;

        /**
         * Number of bytes that we can read from the buffer
         */
        private int count = 0;

        /**
         * Number of bytes that have been read from the buffer
         */
        private int pos = 0;

        /**
         * Constructor.
         *
         * @param buf a byte array to read from
         * @param count number of bytes to read
         */
        public FastByteArrayInputStream(byte[] buf, int count) {
            this.buf = buf;
            this.count = count;
        }

        /**
         * {@inheritDoc}
         * @see java.io.InputStream#available()
         */
        public int available() {
            return count - pos;
        }

        /**
         * {@inheritDoc}
         * @see java.io.InputStream#read()
         */
        public int read() {
            return (pos < count) ? (buf[pos++] & 0xff) : -1;
        }

        /**
         * {@inheritDoc}
         * @see java.io.InputStream#read(byte[], int, int)
         */
        public int read(final byte[] b, final int off, int len) {
            if (pos >= count) {
                return -1;
            }
            if ((pos + len) > count) {
                len = (count - pos);
            }
            System.arraycopy(buf, pos, b, off, len);
            pos += len;
            return len;
        }

        /**
         * {@inheritDoc}
         * @see java.io.InputStream#skip(long)
         */
        public long skip(long n) {
            if ((pos + n) > count) {
                n = count - pos;
            }
            if (n < 0) {
                return 0;
            }
            pos += n;
            return n;
        }
    }

    /**
     * ByteArrayOutputStream implementation that doesn't synchronize methods and doesn't copy the data on toByteArray().
     */
    private static final class FastByteArrayOutputStream extends OutputStream {

        /**
         * Buffer and size
         */
        private byte[] buf = null;

        private int size = 0;

        /**
         * Constructs a stream with buffer capacity size 5K
         */
        public FastByteArrayOutputStream() {
            this(5 * 1024);
        }

        /**
         * Constructs a stream with the given initial size
         */
        public FastByteArrayOutputStream(int initSize) {
            this.size = 0;
            this.buf = new byte[initSize];
        }

        /**
         * Ensures that we have a large enough buffer for the given size.
         */
        private void verifyBufferSize(int sz) {
            if (sz > buf.length) {
                byte[] old = buf;
                buf = new byte[Math.max(sz, 2 * buf.length)];
                System.arraycopy(old, 0, buf, 0, old.length);
            }
        }

        public int getSize() {
            return size;
        }

        /**
         * Returns the byte array containing the written data. Note that this array will almost always be larger than
         * the amount of data actually written.
         */
        public byte[] getByteArray() {
            return buf;
        }

        /**
         * {@inheritDoc}
         * @see java.io.OutputStream#write(byte[])
         */
        public void write(final byte[] b) {
            verifyBufferSize(size + b.length);
            System.arraycopy(b, 0, buf, size, b.length);
            size += b.length;
        }

        /**
         * {@inheritDoc}
         * @see java.io.OutputStream#write(byte[], int, int)
         */
        public void write(final byte[] b, final int off, final int len) {
            verifyBufferSize(size + len);
            System.arraycopy(b, off, buf, size, len);
            size += len;
        }

        /**
         * {@inheritDoc}
         * @see java.io.OutputStream#write(int)
         */
        public void write(final int b) {
            verifyBufferSize(size + 1);
            buf[size++] = (byte) b;
        }

        public void reset() {
            size = 0;
        }

        /**
         * Returns a ByteArrayInputStream for reading back the written data
         */
        public InputStream getInputStream() {
            return new FastByteArrayInputStream(buf, size);
        }
    }
}
