package org.rascalli.framework.net.tcp;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface OutgoingPacket {

    /**
     * Write data to the specified channel.
     * 
     * @param socketChannel
     *            A SocketChannel to which data can be written.
     * 
     * @return {@code true} if the packet has finished writing all of its data,
     *         {@code false} if there is data left to write.
     * @throws IOException
     *             forwarded from writing to the {@link SocketChannel}.
     */
    boolean write(SocketChannel socketChannel) throws IOException;
}
