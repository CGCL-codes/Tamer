package org.codemonkey.swiftsocketserver;

/**
 * Message that encodes pong messages. This message is on the server level and therefore only interacts with the {@link ClientHandler} and
 * is filtered from the client message queue in the server.
 * 
 * @author Benny Bottema
 * @see ServerMessageToClient
 * @since 1.0
 */
final class ServerMessageToClientPingPong extends ServerMessageToClient {

    public ServerMessageToClientPingPong(final ClientContext clientContext) {
        super(clientContext);
    }
}
