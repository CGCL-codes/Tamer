package fr.x9c.cadmium.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * This class provides means of random access over an input stream. <br/>
 * There are two ways to use this class :
 * <ul>
 *   <li>over a stream (by <tt>InputStream</tt> constructors):<br/>
 *       the whole stream is read and stored in memory;</li>
 *   <li>over a file (by <tt>File</tt> or <tt>String</tt> constructor):<br/>
 *       the class relies on <tt>java.io.RandomAccessFile</tt>.</li>
 * </ul>
 * <br/>
 * When in "stream" mode maximum size (and thus offset) equals
 * {@link java.lang.Integer#MAX_VALUE}, whereas in "file" mode maximum size
 * equals {@link java.lang.Long#MAX_VALUE}.
 *
 * @see java.io.File
 * @see java.io.InputStream
 * @see java.io.RandomAccessFile
 *
 * @author <a href="mailto:cadmium@x9c.fr">Xavier Clerc</a>
 * @version 1.0
 * @since 1.0
 */
public final class RandomAccessInputStream {

    /**
     * File mode for bytecode file (read only).
     * @see java.io.RandomAccessFile
     */
    private static final String FILE_MODE = "r";

    /**
     * Bytes read from string when in "stream" mode,
     * <tt>null</tt> otherwise.
     */
    private final byte[] bytes;

    /**
     * Underlying file when in "file" mode,
     * <tt>null</tt> otherwise.
     */
    private final RandomAccessFile file;

    /**
     * Constructs an instance from a file.
     * @param f file - should not be <tt>null</tt>
     * @throws FileNotFoundException if file does not exist
     */
    public RandomAccessInputStream(final File f) throws FileNotFoundException {
        assert f != null : "null f";
        this.file = new RandomAccessFile(f, RandomAccessInputStream.FILE_MODE);
        this.bytes = null;
    }

    /**
     * Constructs an instance from a file.
     * @param name file name - should not be <tt>null</tt>
     * @throws FileNotFoundException if file does not exist
     */
    public RandomAccessInputStream(final String name) throws FileNotFoundException {
        this(new File(name));
    }

    /**
     * Constructs an instance from a stream. <br/>
     * Bytes are read from the stream until either its end,
     * either <tt>maxSize</tt> bytes have been read.
     * @param in source stream - should not be <tt>null</tt> <br/>
     *        the stream is <b>not</b> closed after read
     * @param maxSize maximum number of bytes to read - should be >= 0
     * @throws IOException if an error occurs during read
     */
    public RandomAccessInputStream(final InputStream in, final int maxSize) throws IOException {
        assert in != null : "null in";
        assert maxSize >= 0 : "maxSize should be >= 0";
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int size = 0;
        int b = in.read();
        while ((b != -1) && (size < maxSize)) {
            baos.write(b);
            size++;
            b = in.read();
        }
        this.bytes = baos.toByteArray();
        this.file = null;
    }

    /**
     * Constructs an instance from a stream. <br/>
     * Bytes are read from the stream until its end
     * or after {@link java.lang.Integer#MAX_VALUE} bytes. <br/>
     * Code is exactly <tt>this(int, Integer.MAX_VALUE)</tt>.
     * @param in source stream - should not be <tt>null</tt> <br/>
     *        the stream is <b>not</b> closed after read
     * @throws IOException if an error occurs during read
     */
    public RandomAccessInputStream(final InputStream in) throws IOException {
        this(in, Integer.MAX_VALUE);
    }

    /**
     * Returns a "view" of the underlying stream.
     * @param ofs stream offset - should be >= 0
     * @return a "view" of the stream, starting a the given offset
     * @throws IOException if an error occurs during "view" construction
     */
    public DataInput dataInputFrom(final long ofs) throws IOException {
        assert ofs >= 0 : "ofs should be >= 0";
        if (this.file != null) {
            this.file.seek(ofs);
            return this.file;
        } else {
            assert ofs <= Integer.MAX_VALUE : "ofs should be <= Integer.MAX_VALUE when in 'stream' mode";
            final int offset = (int) ofs;
            final ByteArrayInputStream bais = new ByteArrayInputStream(this.bytes, offset, this.bytes.length - offset);
            return new DataInputStream(bais);
        }
    }

    /**
     * Reads bytes from the stream.
     * @param ofs offset of bytes - should be >= 0
     * @param len number of bytes - should be >= 0
     * @return the bytes read from the stream
     * @throws IOException if an error occurs during read
     */
    public byte[] readBytes(final long ofs, final int len) throws IOException {
        assert ofs >= 0 : "ofs should be >= 0";
        assert len >= 0 : "len should be >= 0";
        final byte[] res = new byte[len];
        if (this.file != null) {
            this.file.seek(ofs);
            this.file.readFully(res);
        } else {
            assert ofs <= Integer.MAX_VALUE : "ofs should be <= Integer.MAX_VALUE when in 'stream' mode";
            try {
                System.arraycopy(this.bytes, (int) ofs, res, 0, len);
            } catch (final IndexOutOfBoundsException x) {
                throw new IOException(x.toString());
            }
        }
        return res;
    }

    /**
     * Closes the underlying file when in "file" mode,
     * do nothing <tt>otherwise</tt>.
     * @throws IOException if an I/O error occurs
     */
    public void close() throws IOException {
        if (this.file != null) {
            this.file.close();
        }
    }

    /**
     * Returns the length of the input stream.
     * @return the length of the input stream
     * @throws IOException if an I/O error occurs
     */
    public long length() throws IOException {
        if (this.file != null) {
            return this.file.length();
        } else {
            return this.bytes.length;
        }
    }

    /**
     * Creates an in-memory input stream.
     * @return an input stream based on the bytes read from the stream when
     *         the instance has been created from a stream, <tt>null</tt>
     *         otherwise <br/>
     *         <b>The byte array is not copied</b>
     */
    public MemoryInputStream createInputStream() {
        if (this.bytes != null) {
            return new MemoryInputStream(this.bytes);
        } else {
            return null;
        }
    }
}
