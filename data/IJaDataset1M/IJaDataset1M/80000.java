package org.fenggui.composite.console;

import java.io.IOException;
import java.io.OutputStream;
import org.fenggui.binding.render.text.ITextRenderer;

public class ConsoleOutputStream extends OutputStream {

    private ITextRenderer textRenderer = null;

    public ConsoleOutputStream(ITextRenderer textRenderer) {
        this.textRenderer = textRenderer;
    }

    @Override
    public void write(int b) throws IOException {
    }

    @Override
    public void close() throws IOException {
        super.close();
    }

    @Override
    public void flush() throws IOException {
        super.flush();
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        super.write(b, off, len);
    }

    @Override
    public void write(byte[] b) throws IOException {
        super.write(b);
    }
}
