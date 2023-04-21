package org.creativor.rayson.transport.client;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.creativor.rayson.transport.api.Connection;
import org.creativor.rayson.transport.api.TimeLimitConnection;
import org.creativor.rayson.transport.common.ConnectionProtocol;
import org.creativor.rayson.util.Log;

/**
 * This object is used to manage all {@link Connection}s in client side of
 * transport layer.
 * 
 * @author Nick Zhang
 */
class ConnectionManager extends Thread {

    private static Logger LOGGER = Log.getLogger();

    private static final int THECK_TIME_OUT_INTERVAL = ConnectionProtocol.RPC_TIME_OUT_INTERVAL / 2;

    private static AtomicLong CONNECTION_UID = new AtomicLong(0);

    /**
	 * Gets the next connection id.
	 * 
	 * @return the next connection id
	 */
    public static long getNextConnectionId() {
        return CONNECTION_UID.getAndIncrement();
    }

    private ConcurrentHashMap<Long, TimeLimitConnection> connections;

    /**
	 * Instantiates a new connection manager.
	 */
    ConnectionManager() {
        setName("Client connection manager");
        connections = new ConcurrentHashMap<Long, TimeLimitConnection>();
    }

    private void checkTimeouts() {
        for (Iterator<TimeLimitConnection> itr = this.connections.values().iterator(); itr.hasNext(); ) {
            TimeLimitConnection conn = itr.next();
            if (conn.isTimeOut()) {
                try {
                    LOGGER.info("Remove and close time out conection: " + conn.toString());
                    conn.close();
                    this.remove(conn);
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Close time out connection error: " + e.getMessage());
                }
            }
        }
    }

    /**
	 * Removes the.
	 * 
	 * @param connection
	 *            the connection
	 */
    public void remove(TimeLimitConnection connection) {
        this.connections.remove(connection.getId());
    }

    /**
	 * Run.
	 */
    @Override
    public void run() {
        LOGGER.info(getName() + " starting...");
        while (true) {
            try {
                Thread.sleep(THECK_TIME_OUT_INTERVAL);
            } catch (InterruptedException e) {
                break;
            }
            checkTimeouts();
        }
        LOGGER.info(getName() + " stoped");
    }

    /**
	 * Put a connection into this manager.
	 * 
	 * @param connection
	 *            the connection
	 */
    public void put(TimeLimitConnection connection) {
        this.connections.put(connection.getId(), connection);
    }
}
