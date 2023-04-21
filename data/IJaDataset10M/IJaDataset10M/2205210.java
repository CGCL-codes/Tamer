package org.openymsg.execute.read;

import org.openymsg.network.ConnectionHandler;

public interface PacketReader extends ReaderRegistry {

    void initializeConnection(ConnectionHandler connection);

    void shutdown();
}
