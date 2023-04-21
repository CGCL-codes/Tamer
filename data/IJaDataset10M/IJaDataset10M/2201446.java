package eu.pisolutions.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * {@link ReaderWrapper} that buffers one character to allow putback.
 *
 * @author Laurent Pireyn
 */
public final class PutbackableInputStream extends BaseInputStreamWrapper {

    private static final int NO_BYTE = -1;

    private int bufferedByte;

    public PutbackableInputStream(InputStream in) {
        super(in);
    }

    @Override
    public boolean markSupported() {
        return false;
    }

    @Override
    public int available() throws IOException {
        int count = this.in.available();
        if (this.hasBufferedByte()) {
            ++count;
        }
        return count;
    }

    @Override
    public void reset() throws IOException {
        throw new IOException("Reset not supported");
    }

    @Override
    public int read() throws IOException {
        if (this.hasBufferedByte()) {
            final byte b = (byte) this.bufferedByte;
            this.bufferedByte = PutbackableInputStream.NO_BYTE;
            return b;
        }
        return this.in.read();
    }

    public void putback(byte b) {
        if (this.hasBufferedByte()) {
            throw new IllegalStateException("Byte already buffered");
        }
        this.bufferedByte = b;
    }

    private boolean hasBufferedByte() {
        return this.bufferedByte != PutbackableInputStream.NO_BYTE;
    }
}
