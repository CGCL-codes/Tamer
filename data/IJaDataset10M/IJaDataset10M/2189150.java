package com.sun.jini.jeri.internal.connection;

import net.jini.jeri.RequestDispatcher;
import net.jini.jeri.connection.ServerConnection;
import net.jini.jeri.connection.ServerConnectionManager;

/**
 * Manages server-side connections by delegating directly to
 * {@link ServerConnectionManager}.
 * 
 * @author Sun Microsystems, Inc.
 * @since 2.0
 */
public class BasicServerConnManager implements ServerConnManager {

    private static final ServerConnectionManager manager = new ServerConnectionManager();

    /**
	 * Creates new instance containing a <code>ServerConnectionManager</code>.
	 */
    public BasicServerConnManager() {
    }

    /**
	 * Delegates to the {@link ServerConnectionManager#handleConnection
	 * handleConnection} method of the contained
	 * <code>ServerConnectionManager</code>.
	 * 
	 * @throws NullPointerException
	 *             if either argument is <code>null</code>
	 */
    public void handleConnection(ServerConnection conn, RequestDispatcher dispatcher) {
        manager.handleConnection(conn, dispatcher);
    }
}
