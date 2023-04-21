package eu.pisolutions.io;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import eu.pisolutions.lang.Validations;

/**
 * {@link java.io.Reader} wrapper.
 * <p>
 * Unlike {@link java.io.FilterReader}, this class makes the {@link #read(char[])} method final.
 * </p>
 *
 * @author Laurent Pireyn
 */
public class ReaderWrapper extends Reader {

    protected final Reader reader;

    public ReaderWrapper(Reader reader) {
        super();
        Validations.notNull(reader, "reader");
        this.reader = reader;
    }

    @Override
    public boolean markSupported() {
        return this.reader.markSupported();
    }

    @Override
    public void mark(int readAheadLimit) throws IOException {
        this.reader.mark(readAheadLimit);
    }

    @Override
    public void reset() throws IOException {
        this.reader.reset();
    }

    @Override
    public boolean ready() throws IOException {
        return this.reader.ready();
    }

    @Override
    public int read() throws IOException {
        return this.reader.read();
    }

    @Override
    public final int read(char[] array) throws IOException {
        return this.read(array, 0, array.length);
    }

    @Override
    public int read(char[] array, int offset, int length) throws IOException {
        return this.reader.read(array, offset, length);
    }

    @Override
    public int read(CharBuffer target) throws IOException {
        return this.reader.read(target);
    }

    @Override
    public long skip(long count) throws IOException {
        return this.reader.skip(count);
    }

    @Override
    public void close() throws IOException {
        this.reader.close();
    }
}
