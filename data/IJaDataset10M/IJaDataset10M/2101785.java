package org.codemonkey.swiftsocketclient;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Contains all session data concerning the server. Also contains a session data container for external use only. External users can utilize
 * this container to store server related (session) data for the duration of the connection.
 * <p>
 * Only the server's id and the session data container is exposed publicly. The server id is determined during construction so that even
 * when the connection is lost, external users can still access the server's id without connection exceptions.
 * <p>
 * Instances of this classes act as unique identifiers for client sockets, doing so allows executable message objects to access the server's
 * session data without needing the Socket or InetAddress to identify the client.
 * <p>
 * This context keeps information about the server which may remain accessible even when the client or server has disconnected.
 * 
 * @author Benny Bottema
 * @since 1.0
 */
public class ServerContext {

    /**
	 * Server to which this context is dedicated to. Used to read from and write to.
	 */
    private final ServerEndpoint serverEndpoint;

    /**
	 * Server id to which context is dedicated to. Acquired from {@link #serverInetAddress}. Kept separate for when the socket has been
	 * closed already and the user still wants to know the original server id.
	 */
    private final InetAddress serverInetAddress;

    /**
	 * Flag that indicated whether the client has sent a 'Bye Bye' notification to gracefully close the connection, or when the connection
	 * was lost somehow.
	 */
    private volatile boolean serverSaidByeBye;

    /**
	 * A session scoped data container for external use only. Users can access this container to store temporary data concerning the current
	 * server connection, until the connection is lost and the context destroyed.
	 */
    private final Map<Object, Object> sessionData = new HashMap<Object, Object>();

    /**
	 * Stores the given server endpoint and saves its {@link InetAddress} for later use (in case the endpoint is being closed and we lose
	 * the ability the query for the {@link InetAddress}).
	 * 
	 * @param serverEndpoint The server with which we have established a communication channel.
	 */
    public ServerContext(final ServerEndpoint serverEndpoint) {
        this.serverEndpoint = serverEndpoint;
        serverInetAddress = serverEndpoint.getInetAddress();
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public String toString() {
        return String.valueOf(serverEndpoint.getInetAddress());
    }

    /**
	 * @return {@link #serverEndpoint}.
	 */
    final ServerEndpoint getServerEndpoint() {
        return serverEndpoint;
    }

    /**
	 * @return {@link #serverInetAddress}
	 */
    public InetAddress getServerInetAddress() {
        return serverInetAddress;
    }

    /**
	 * @return {@link #sessionData}
	 */
    public Map<Object, Object> getSessionData() {
        return sessionData;
    }

    /**
	 * @return {@link #serverSaidByeBye}
	 */
    final boolean isServerSaidByeBye() {
        return serverSaidByeBye;
    }

    final void setServerSaidByeBye(final boolean serverSaidByeBye) {
        this.serverSaidByeBye = serverSaidByeBye;
    }
}
