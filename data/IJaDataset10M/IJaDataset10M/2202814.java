package mobi.ilabs;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ReadableByteChannel;

/**
 * Returning  ReadableByteArray object representing the
 * stored object. Is used by Restlet to implement
 * nonblocking IO writes of the content of the object.
 *
 * @author rmz@ilabs.mobi
 *
 */
public class ByteArrayBackedReadableByteChannel implements ReadableByteChannel {

    /**
     * An empty array to  use when instantiating the
     * the empty channel.
     */
    private static final byte[] EMPTY_BYTE_ARRAY = {};

    /**
     * A singelton single channel.
     */
    public static final ByteArrayBackedReadableByteChannel EMPTY_CHANNEL = new ByteArrayBackedReadableByteChannel(EMPTY_BYTE_ARRAY);

    /**
     * The backing array.
     */
    private byte[] backing;

    /**
     * The current position in the backing array.
     */
    private int pos = 0;

    /**
     * Creating a new ByteArrayBackedReadableByteChannel based on a
     * byte array from somewhere.
     * @param back the backing byte array for this channel.
     */
    public ByteArrayBackedReadableByteChannel(final byte[] back) {
        super();
        if (back == null) {
            throw new IllegalStateException("Null backing byte array");
        }
        this.backing = back;
    }

    /**
     * Local state.  True iff the channel has been closed.
     */
    private boolean isClosed = false;

    /**
     * Set the channel to closed state.
     * @throws IOException doesn't really throw anything,
     *         but the interface requires this declaration.
     */
    public final synchronized void close() throws IOException {
        isClosed = true;
    }

    /**
     * Return true iff the object isn't closed. The
     * object is open by default.
     * @return true iff open.
     */
    public final synchronized boolean isOpen() {
        return !isClosed;
    }

    /**
     * Read from the byte array into the ByteBuffer.
     * Return integer indicating the number of bytes that
     * was read, -1 if no bytes could be read.
     * @param dst the byte buffer we are reading into.
     * @throws IOException when IO bugs.
     * @return number of bytes read.
     */
    public final synchronized int read(final ByteBuffer dst) throws IOException {
        if (!isOpen()) {
            throw new ClosedChannelException();
        }
        if (dst == null) {
            throw new NullPointerException("null ByteBuffer destination");
        }
        final int bytesLeft = backing.length - pos;
        if (bytesLeft == 0) {
            return -1;
        }
        if (bytesLeft < 0) {
            throw new RuntimeException("Inconsistent ByteArrayBacked byte channel");
        }
        final int bytesRead = Math.min(bytesLeft, dst.remaining());
        dst.put(backing, pos, bytesRead);
        pos += bytesRead;
        return bytesRead;
    }
}
