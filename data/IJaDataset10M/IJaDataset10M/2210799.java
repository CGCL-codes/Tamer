package net.sourceforge.freecol.server;

import java.net.Socket;
import net.sourceforge.freecol.common.Player;
import net.sourceforge.freecol.networking.Connection;

/**
 * A player as known by the server, adding to the common Player
 * information that which the server needs to keep track of the player.
 */
public class ServerPlayer extends Player {

    private Socket socket;

    private Connection connection;

    private boolean ready;

    private int entrySeaLane;

    /**
    * The constructor for this object.
    * @param name The player name.
    * @param admin Whether the player is the game administrator.
    * @param socket The socket to the player's client.
    * @param connection The Connection for that socket.
    */
    public ServerPlayer(String name, boolean admin, Socket socket, Connection connection) {
        super(name, admin);
        this.socket = socket;
        this.connection = connection;
        ready = false;
    }

    /**
     * Gets the socket.
     * @return Socket
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Gets the connection.
     * @return Connection.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Gets the player's assigned y coordinate for entry to the map from Europe.
     * @return Y coordinate
     */
    public int getEntrySeaLane() {
        return entrySeaLane;
    }

    /**
     * Returns whether the player is Ready or not.
     * @return True if the player is Ready.
     */
    public boolean isReady() {
        return ready;
    }

    /**
     * Sets the player's assigned y coordinate for entry to the map from Europe.
     * @param y Y coordinate
     */
    public void setEntrySeaLane(int y) {
        entrySeaLane = y;
    }

    /**
     * Toggles the player's Ready state between True and False.
     */
    public void switchReady() {
        ready = !ready;
    }
}
