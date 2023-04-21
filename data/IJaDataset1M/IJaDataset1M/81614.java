package org.catacombae.hfsexplorer.io;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

/**
 *
 * @author Erik
 */
public interface WritableRandomAccessChannel extends RandomAccessChannel, WritableByteChannel {

    public abstract FileChannel truncate(long size) throws IOException;
}
