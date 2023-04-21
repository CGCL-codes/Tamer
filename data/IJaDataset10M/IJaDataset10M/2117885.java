package gnu.saw.compression;

import java.io.*;
import java.util.zip.*;

/**
 * Filter input stream that is able to decompress data compressed with
 * {@link SAWCompressedOutputStream}. Since the latter features strong
 * flush semantics, the two can be used as a transport for RMI or RPC.
 *
 * Note that standard {@link java.util.zip.ZipOutputStream} and
 * {@link java.util.zip.GZIPOutputStream} are useless for this purpose due to
 * their insufficiently strong flushing semantics: they don't guarantee that
 * flush sends out all the data that was written so far, which leads to
 * deadlocks in request-response-based protocols.
 *
 * @see SAWCompressedOutputStream
 *
 * @author Dawid Kurzyniec
 * @version 1.0
 */
public class SAWCompressedInputStream extends FilterInputStream {

    static final short DEFLATED = (short) 0x8000;

    static final short STORED = (short) 0x0000;

    final byte[] buf;

    final DataInputStream din;

    final Inflater inflater;

    boolean deflated = false;

    int pending = 0;

    /**
     * Creates a new compressed stream over a specified stream.
     *
     * @param in the input to read from
     */
    public SAWCompressedInputStream(InputStream in) {
        this(in, 8192);
    }

    /**
     * Creates a new compressed stream that reads from a specified stream and
     * uses specified buffer size.
     *
     * @param in the input to read from
     * @param bufSize buffer size
     */
    public SAWCompressedInputStream(InputStream in, int bufSize) {
        super(in);
        buf = new byte[bufSize];
        this.inflater = new Inflater(true);
        this.din = new DataInputStream(in);
    }

    public synchronized int read() throws IOException {
        byte[] tmp = new byte[1];
        int read = read(tmp);
        if (read < 0) return -1;
        return tmp[0];
    }

    public synchronized int read(byte[] dest, int off, int len) throws IOException {
        while (true) {
            if (deflated) {
                try {
                    int inflated = inflater.inflate(dest, off, len);
                    if (inflated > 0) {
                        return inflated;
                    }
                } catch (DataFormatException e) {
                    throw new IOException(e.toString());
                }
                if (pending == 0 || inflater.finished()) {
                    if (pending > 0) {
                        throw new IOException("Premature end of compressed data block");
                    }
                    if (!(newPacket())) return -1;
                    if (!deflated) continue;
                }
                if (inflater.needsInput()) {
                    int todo = Math.min(buf.length, pending);
                    int read = in.read(buf, 0, todo);
                    if (read < 0) {
                        throw new EOFException("Unexpected EOF");
                    }
                    pending -= read;
                    inflater.setInput(buf, 0, read);
                    continue;
                } else {
                    throw new RuntimeException();
                }
            } else {
                if (pending == 0) {
                    if (!(newPacket())) return -1;
                    if (deflated) continue;
                }
                int todo = Math.min(pending, len);
                int read = in.read(dest, off, todo);
                if (read < 0) {
                    throw new EOFException("Unexpected EOF");
                }
                pending -= read;
                return read;
            }
        }
    }

    private boolean newPacket() throws IOException {
        short header;
        try {
            header = din.readShort();
        } catch (EOFException e) {
            return false;
        }
        deflated = ((header & DEFLATED) == DEFLATED);
        pending = header & 0x7FFF;
        if (pending <= 0) {
            throw new IOException("Bogus packet length");
        }
        if (deflated) {
            inflater.reset();
        }
        return true;
    }
}
