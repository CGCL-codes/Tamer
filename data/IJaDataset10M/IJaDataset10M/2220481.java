package com.pz.net;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 *
 * @author jannek
 */
public interface NetworkListener {

    /**
     * Whenever a message is received from a player or server.
     * @param data Message.
     * @param client Sender.
     */
    public void onMessage(ByteBuffer data, Other client);

    /**
     * Called whenever a new client has connected. Whether the client is a
     * player connection to a server, or a server connecting to a player.
     * @param client The newly connected client.
     * @return Allow client to connect?
     */
    public boolean onConnect(Other client);

    /**
     * Called every time a client is lagging behind.
     * @param client The client we are waiting for.
     * @param msDelay Delay in milliseconds.
     * @return Wait for client? (false -> kick)
     */
    public boolean onDelay(Other client, int msDelay);

    /**
     * Called when a client is disconnecting. Whether a player leaves a server
     * or a server "leaves" a player.
     * @param client Who.
     * @param reason Why.
     */
    public void onDisconnect(Other client, Command reason);

    /**
     * Called if an IOException occurs while the client/server is running as a
     * thread.
     * @param ioe Cause.
     * @return Ignore and try again? (false -> disconnect)
     */
    public boolean onUdpError(IOException ioe);
}
