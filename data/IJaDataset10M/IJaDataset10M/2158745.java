package eu.pisolutions.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * {@link eu.pisolutions.io.InputStreamWrapper} that swallows {@link java.io.EOFException}s.
 *
 * @author Laurent Pireyn
 */
public final class SilentEofInputStream extends InputStreamWrapper {

    public SilentEofInputStream(InputStream in) {
        super(in);
    }

    @Override
    public int read() throws IOException {
        try {
            return this.in.read();
        } catch (EOFException exception) {
            return -1;
        }
    }

    @Override
    public int read(byte[] array, int offset, int length) throws IOException {
        try {
            return this.in.read(array, offset, length);
        } catch (EOFException exception) {
            return -1;
        }
    }
}
