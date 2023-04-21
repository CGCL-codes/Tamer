package org.smartlib.pool.core;

import java.sql.Connection;
import java.util.*;

/**
 * This interface defines the behavior of the class that will manage the 
 * connections i.e a single pool of connections.
 */
public interface Pool {

    /**
	 * This method returns a Connection from the connection pool. 
	 * The owner of this pool is marked as N/A indicating unknown.
	 *
	 * <b>Note: This method blocks if the pool size has reached it's 
	 * maximum size and no free connections are available 
	 * until a free connection is available</b>. The time period for which this
	 * method blocks depends on the connection-wait-time-out specified in 
	 * the configuration file.
	 * 	 	 
	 * 
 	 * @return Connection from the pool
	 * @exception ConnectionPoolException if there is any problem 
	 *		getting connection.	
	 */
    public Connection getConnection() throws ConnectionPoolException;

    /**
	 * This method returns a Connection from the  pool. 
	 * The owner of this connection is identified by <code>owner</code> .
	 *
	 * <b>Note: This method blocks if the pool size has reached it's 
	 * maximum size and no free connections are available 
	 * until a free connection is available</b>. The time period for which this
	 * method blocks depends on the connection-wait-time-out specified in 
	 * the configuration file.
	 * 	 	 
	 *
	 * @param owner String identifying the owner.
 	 * @return Connection from the pool
	 * 
	 * @exception ConnectionPoolException if there is any problem 
	 *		getting connection.	
	 */
    public Connection getConnection(String owner) throws ConnectionPoolException;

    /**
	 * This method returns the current size of the pool.
	 * @return Current size of the pool
	 */
    public int getCurrentPoolSize();

    /**
	 * This method releases the Connection <code>conn</code> to the pool.
	 */
    public void returnConnection(Connection conn);

    /**
	 * @return Vector of connections in use
	 */
    public Vector getConnectionsInUse();

    /**
	 * @return Vector of registered ConnectionLeakListeners
	 */
    public Vector getConnectionLeakListeners();

    /**
	 * @return Number of free connections in the pool.
	 */
    public int getNoOfFreeConnections();

    /**
	 * This method adds  a connection leak listener. The methods of 
	 * <code>cle</code> will be called when a leak is detected as per the
	 * pool configuration.
	 *
	 * @param cle Class implementing ConnectionLeakListener interface.
	 * @exception ConnectionPoolException If there is any problem 
	 *	 adding ConnectionLeakListener.
	 */
    public void addConnectionLeakListener(ConnectionLeakListener cle) throws ConnectionPoolException;

    /**
	 * This method removes a connection leak listener.<code>cle</code> will
	 * not get any further notifications.
	 *
	 * @param cle Class implementing ConnectionLeakListener interface.
	 * @exception ConnectionPoolException If there is any problem 
	 *	 removing ConnectionLeakListener.
	 */
    public void removeConnectionLeakListener(ConnectionLeakListener cle) throws ConnectionPoolException;

    /**
	 * This method releases excessive connections.
	 */
    public void releaseConnections();

    /**
     * This method shuts down the pool
     */
    public void shutDown();
}
