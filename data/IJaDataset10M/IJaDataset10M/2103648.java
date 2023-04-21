package org.ardverk.daap;

import java.io.IOException;

/**
 * Use this interface to implement a Stream Source which maps Songs to
 * FileInputStreams.
 * 
 * @author Roger Kapsi
 */
public interface DaapStreamSource {

    /**
     * Returns an <code>InputStream</code> for the provided Song.
     */
    public Object getSource(Song song) throws IOException;
}
