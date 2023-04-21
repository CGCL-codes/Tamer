package org.gamegineer.client.core;

import java.io.IOException;
import java.util.Collection;
import org.gamegineer.client.core.connection.IGameServerConnection;
import org.gamegineer.game.ui.system.IGameSystemUi;

/**
 * A game client.
 * 
 * <p>
 * This interface is not intended to be implemented or extended by clients.
 * </p>
 */
public interface IGameClient {

    /**
     * Connects this client to the server associated with the specified
     * connection.
     * 
     * <p>
     * The current connection, if any, will be disconnected before applying the
     * new connection.
     * </p>
     * 
     * <p>
     * If the new connection cannot be opened, this client will be left with no
     * connection.
     * </p>
     * 
     * @param connection
     *        The game server connection; must not be {@code null}.
     * 
     * @throws java.io.IOException
     *         If an error occurs while connecting to the server.
     * @throws java.lang.NullPointerException
     *         If {@code connection} is {@code null}.
     */
    public void connect(IGameServerConnection connection) throws IOException;

    /**
     * Disconnects this client from the connected server.
     * 
     * <p>
     * This method may be called even if this client is not connected.
     * </p>
     */
    public void disconnect();

    public IGameServerConnection getGameServerConnection();

    public IGameSystemUi getGameSystemUi(String gameSystemId);

    public Collection<IGameSystemUi> getGameSystemUis();
}
