package eu.pisolutions.io;

import java.io.IOException;
import java.io.Writer;
import eu.pisolutions.lang.Validations;

/**
 * {@link java.io.Writer} wrapper.
 * <p>
 * Unlike {@link java.io.FilterWriter}, this class makes several methods final and lets only one protected method to override.
 * </p>
 *
 * @author Laurent Pireyn
 */
public class WriterWrapper extends Writer {

    protected final Writer writer;

    public WriterWrapper(Writer writer) {
        super();
        Validations.notNull(writer, "writer");
        this.writer = writer;
    }

    @Override
    public void write(int c) throws IOException {
        this.writer.write(c);
    }

    @Override
    public final void write(char[] array) throws IOException {
        this.write(array, 0, array.length);
    }

    @Override
    public void write(char[] array, int offset, int length) throws IOException {
        this.writer.write(array, offset, length);
    }

    @Override
    public final void write(String string) throws IOException {
        this.write(string, 0, string.length());
    }

    @Override
    public final void write(String string, int offset, int length) throws IOException {
        this.append(string, offset, offset + length);
    }

    @Override
    public final Writer append(char c) throws IOException {
        this.write(c);
        return this;
    }

    @Override
    public final Writer append(CharSequence sequence) throws IOException {
        return this.append(sequence, 0, sequence.length());
    }

    @Override
    public final Writer append(CharSequence sequence, int start, int end) throws IOException {
        this.write(sequence, start, end);
        return this;
    }

    @Override
    public void flush() throws IOException {
        this.writer.flush();
    }

    @Override
    public void close() throws IOException {
        this.writer.close();
    }

    protected void write(CharSequence sequence, int start, int end) throws IOException {
        this.writer.append(sequence, start, end);
    }
}
