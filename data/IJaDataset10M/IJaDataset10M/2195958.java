package com.objectwave.persist.broker;

import java.util.Vector;
import com.objectwave.logging.MessageLog;

/**
 *  Manage multiple connections.
 *
 * @author  dhoag
 * @version  $Date: 2005/02/21 03:43:34 $ $Revision: 2.4 $
 */
public class RDBConnectionPool {

    RDBConnection[] connections;

    Vector list;

    boolean initialized = false;

    long sqlTime;

    /**
	 *  the index to a silly algorithm to provide more balance to the connection
	 *  usage.
	 */
    int balanceIdx = 0;

    static boolean metrics = System.getProperty("ow.persistMetrics") != null;

    /**
	 */
    public RDBConnectionPool() {
    }

    /**
	 * @param  conns com.objectwave.persist.RDBConnection[]
	 * @author  Dave Hoag
	 */
    public void setConnections(RDBConnection[] conns) {
        connections = conns;
        initialized = true;
    }

    /**
	 *  RDBConnectionPool constructor comment. All connections created by this
	 *  connection pool will have the following parameters. This method MUST be
	 *  called prior to getConnections being called.
	 *
	 * @param  connectUrl Description of Parameter
	 * @param  userName Description of Parameter
	 * @param  password Description of Parameter
	 * @param  connectionCount Description of Parameter
	 * @deprecated
	 */
    public void initialize(String connectUrl, String userName, String password, final int connectionCount) {
        connections = new RDBConnection[connectionCount];
        for (int i = 0; i < connectionCount; i++) {
            connections[i] = new RDBConnection(this, connectUrl, userName, password);
        }
        initialized = true;
    }

    /**
	 *  Gets the SqlTime attribute of the RDBConnectionPool object
	 *
	 * @return  The SqlTime value
	 */
    protected long getSqlTime() {
        return sqlTime;
    }

    /**
	 *  Every thread will attempt to get a connection to the database. This is the
	 *  point at which we handle synchronization issues. Once a thread has a
	 *  connection, it will hold onto the connection as long as necessary. No other
	 *  threads will be allowed to have that connection until it is set free.
	 *
	 * @return  The Connection value
	 * @fixme  - Thread death? Preemptive dropping of connection? More threads than
	 *      connections?
	 * @author  Dave Hoag
	 */
    protected synchronized RDBConnection getConnection() {
        if (!initialized) {
            throw new RuntimeException("Connection pool is not initialized!");
        }
        RDBConnection available = null;
        final Thread current = Thread.currentThread();
        while (true) {
            for (int i = 0; i < connections.length; i++) {
                if (connections[i].getThread() == current) {
                    return connections[i];
                }
                if (available == null && connections[i].getThread() == null) {
                    available = connections[i];
                }
            }
            if (available != null) {
                available.setThread(current);
                return available;
            }
            waitForConnection();
        }
    }

    /**
	 *  Used for metric information
	 *
	 * @param  l Description of Parameter
	 */
    protected synchronized void incrementSqlTime(long l) {
        sqlTime += l;
    }

    /**
	 *  Description of the Method
	 */
    protected void resetSqlTime() {
        sqlTime = 0;
    }

    /**
	 *  The other way the pool works is maintains the list of connections.
	 *
	 * @param  con The feature to be added to the Connection attribute
	 */
    protected void addConnection(RDBConnection con) {
        if (list == null) {
            list = new Vector();
        }
        list.add(con);
    }

    /**
	 * @author  Dave Hoag
	 */
    protected final synchronized void notifyConnection() {
        notify();
    }

    /**
	 * @author  Dave Hoag
	 */
    protected final synchronized void waitForConnection() {
        if (metrics) {
            MessageLog.debug(this, "Waiting for connection");
        }
        try {
            wait();
        } catch (InterruptedException ex) {
        }
    }

    /**
	 * @return  The Connections value
	 */
    RDBConnection[] getConnections() {
        if (connections == null && list != null) {
            RDBConnection[] values = new RDBConnection[list.size()];
            list.copyInto(values);
            return values;
        }
        return connections;
    }
}
