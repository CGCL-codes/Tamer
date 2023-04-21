package eu.pisolutions.io;

import java.io.Writer;

/**
 * Prevents a {@link java.io.Writer} from being closed.
 *
 * @author Laurent Pireyn
 * @see java.io.Writer#close
 */
public final class UnclosableWriter extends WriterWrapper {

    public UnclosableWriter(Writer writer) {
        super(writer);
    }

    /**
     * Does nothing.
     */
    @Override
    public void close() {
    }
}
