package net.jxta.impl.xindice.core.filer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Streamable is an interface implemented by objects used by Filers and
 * Indexers in order to serialize objects to and from IO streams.
 */
public interface Streamable {

    /**
     * read reads the object state from the stream.
     *
     * @param is The DataInputStream
     * @throws IOException if an IOException occurs
     */
    public void read(DataInputStream is) throws IOException;

    /**
     * write writes the object state to the stream.
     *
     * @param os The DataOutputStream
     * @throws IOException if an IOException occurs
     */
    public void write(DataOutputStream os) throws IOException;
}
