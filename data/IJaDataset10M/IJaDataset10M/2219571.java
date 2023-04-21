package net.sourceforge.freejava.sio;

import java.io.IOException;
import java.io.Writer;

/**
 * @see WriterCharOut
 */
public abstract class DynamicWriterCharOut extends AbstractCharOut {

    /**
     * @return non-<code>null</code> {@link Writer} instance.
     */
    public abstract Writer getWriter();

    @Override
    public void write(int c) throws IOException {
        getWriter().write(c);
    }

    @Override
    public void write(char[] chars) throws IOException {
        getWriter().write(chars);
    }

    @Override
    public void write(char[] chars, int off, int len) throws IOException {
        getWriter().write(chars, off, len);
    }

    @Override
    public void write(String s) throws IOException {
        getWriter().write(s);
    }

    @Override
    public void write(String string, int off, int len) throws IOException {
        getWriter().write(string, off, len);
    }

    @Override
    public void flush(boolean strict) throws IOException {
        getWriter().flush();
    }

    @Override
    public void close() throws IOException {
        getWriter().close();
    }

    @Override
    public Writer toWriter() {
        return getWriter();
    }
}
