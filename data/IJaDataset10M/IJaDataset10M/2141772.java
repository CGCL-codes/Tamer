package net.sf.kerner.commons.io.buffered;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import net.sf.kerner.commons.io.IOUtils;

public abstract class AbstractBufferedWriter implements GenericBufferedWriter {

    /**
	 * {@link java.io.Writer} to which reading is delegated.
	 */
    protected BufferedWriter writer;

    public AbstractBufferedWriter(File file) throws IOException {
        synchronized (AbstractBufferedWriter.class) {
            writer = new BufferedWriter(new FileWriter(file));
        }
    }

    public AbstractBufferedWriter(Writer writer) {
        synchronized (AbstractBufferedWriter.class) {
            this.writer = new BufferedWriter(writer);
        }
    }

    public AbstractBufferedWriter(OutputStream stream) {
        synchronized (AbstractBufferedWriter.class) {
            writer = new BufferedWriter(IOUtils.outputStreamToWriter(stream));
        }
    }

    public void close() {
        IOUtils.closeProperly(writer);
    }

    public void flush() throws IOException {
        writer.flush();
    }
}
