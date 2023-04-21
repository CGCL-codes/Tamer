package sun.jkernel;

import java.io.FilterOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import sun.jkernel.StandaloneMessageDigest;

public class DigestOutputStream extends FilterOutputStream {

    private static final String DEFAULT_ALGORITHM = "SHA-1";

    private final boolean debug = false;

    private StandaloneMessageDigest smd = null;

    private void initDigest(String algorithm) throws NoSuchAlgorithmException {
        smd = StandaloneMessageDigest.getInstance(algorithm);
    }

    protected volatile OutputStream out;

    /**
     * Creates an output stream filter built on top of
     * underlying output stream <code>out</code> for checking with
     * algorithm <code>algorithm</code>.
     * <p>
     * If <code>algorithm</code> is not supported then
     * <code>NoSuchAlgorithm</code> is thrown.
     * <p>
     * See {linkplain sun.security.provider.StandaloneMessageDigest} for an
     * implementation-specific list of supported algorithms.
     *
     * @param out the underlying output stream to be assigned to
     *     the field <tt>this.out</tt> for later use, or
     *     <code>null</code> if this instance is to be
     *     created without an underlying stream.
     * @param algorithm the check algorithm to use.
     * @throws NoSuchAlgorithm if <code>algorithm</code> is not supported
     * @see sun.security.provider.StandaloneMessageDigest
     * @see DigestInputStream(InputStream, String)
     */
    public DigestOutputStream(OutputStream out, String algorithm) throws NoSuchAlgorithmException {
        super(out);
        initDigest(algorithm);
        this.out = out;
    }

    /**
     * Creates an output stream filter built on top of
     * underlying output stream <code>out</code> for the default checking
     * algorithm.
     * <p>
     * This implemention provides "SHA-1" as the default checking algorithm.
     *
     * @param out the underlying output stream to be assigned to
     *     the field <tt>this.out</tt> for later use, or
     *     <code>null</code> if this instance is to be
     *     created without an underlying stream.
     * @see DigestInputStream(InputStream)
     */
    public DigestOutputStream(OutputStream out) {
        super(out);
        try {
            initDigest(DEFAULT_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("DigestOutputStream() unknown algorithm");
        }
    }

    /**
     * Writes a byte specified by <code>v</code> to this stream
     * and updates the check information.
     *
     *
     * @param v the byte to be written.
     * @throws IOException if an I/O error occurs.
     */
    public void write(int v) throws IOException {
        super.write(v);
        byte[] b = new byte[] { (byte) (v & 0xff) };
        smd.update(b, 0, 1);
    }

    /**
     * Writes the bytes in array <code>data</code>
     * to this stream and updates the check information.
     *
     * @param data the data.
     * @throws IOException  if an I/O error occurs.
     * @throws NullPointerException  if <code>data</code> is <code>null</code>
     */
    public void write(byte[] data) throws IOException {
        write(data, 0, data.length);
    }

    /**
     * Writes a sub array as a sequence of bytes to this output stream and
     * updates the check information.
     * @param data the data to be written
     * @param ofs the start offset in the data
     * @param len the number of bytes that are written
     * @throws IOException If an I/O error has occurred.
     * @throws NullPointerException  if <code>data</code> is <code>null</code>
     * @throws IndexOutOfBoundsException If <code>ofs</code> is negative,
     *     <code>len</code> is negative, or <code>len</code> is greater than
     *     <code>b.length - ofs</code>
     */
    public void write(byte[] data, int ofs, int len) throws IOException {
        if (debug) {
            System.out.print("DigestOutputStream.write: ");
            for (int i = ofs; i < (len - ofs); i++) {
                System.out.format("%02X", data[i]);
            }
            System.out.println();
        }
        if (data == null) {
            throw new NullPointerException("null array in DigestOutputStream.write");
        } else if (ofs < 0 || len < 0 || len > data.length - ofs) {
            throw new IndexOutOfBoundsException();
        }
        out.write(data, ofs, len);
        if (debug) {
            System.out.println("DigestOutputStream.write before");
        }
        smd.update(data, ofs, len);
        if (debug) {
            System.out.println("DigestOutputStream.write after");
        }
    }

    /**
     * Closes this file output stream and releases any system resources
     * associated with this stream and makes the check value for the stream
     * available via <code>getCheckValue</code>. This file output stream may
     * no longer be used for writing bytes.
     *
     * @throws  IOException  if an I/O error occurs.
     * @see getCheckValue
     */
    public void close() throws IOException {
        super.close();
    }

    /**
     * Return the check value computed for the stream and reset the state of
     * check value generation.
     *
     * @return the check value bytes
     */
    public byte[] getCheckValue() {
        byte[] b = smd.digest();
        if (debug) {
            System.out.print("DigestOutputStream.getCheckValue: ");
            for (int i = 0; i < b.length; i++) {
                System.out.format("%02X", b[i]);
            }
            System.out.println();
        }
        smd.reset();
        return b;
    }

    /**
     * Flushes this output stream.
     *
     * @throws IOException if an I/O error occurs.
     * @see java.io.FilterOutputStream#flush()
     */
    public void flush() throws IOException {
        super.flush();
    }
}
