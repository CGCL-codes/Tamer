package net.sourceforge.hlm.generic;

import java.io.*;

public class SimpleWriterAdapter implements SimpleWriter {

    public SimpleWriterAdapter(Writer writer, boolean autoClose) {
        this.writer = writer;
        this.autoClose = autoClose;
    }

    public void write(String text) throws IOException {
        this.writer.write(text);
    }

    public void finished() throws IOException {
        if (this.autoClose) {
            this.writer.close();
        }
    }

    private Writer writer;

    private boolean autoClose;
}
