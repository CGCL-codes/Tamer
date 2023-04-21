package org.smartlib.pool.core;

import java.sql.*;

/**
 * This interface defines the behavior of the class that will manage  
 * various pools and provide a single access point to the pools.
 * 
 * @author	Sachin Shekar Shetty  
 * @version 1.0, 02/08/01
 */
public interface PoolManager {

    /**
	 * This method returns a Connection from the default connection pool. 
	 * The owner of this pool is marked as N/A indicating unknown.
	 *
	 * <b>Note: This method blocks if the pool size has reached it's 
	 * maximum size and no free connections are available 
	 * until a free connection is available.</b> The time period for which this
	 * method blocks depends on the connection-wait-time-out specified in 
	 * the configuration file.
	 *  
	 * 	 	 	 
	 * 
 	 * @return Connection from the default pool
	 * @exception ConnectionPoolException if there is any problem 
	 *		getting connection.	
	 */
    public Connection getConnection() throws ConnectionPoolException;

    /**
	 * This method returns a Connection from the  pool <code>poolName</code>. 
	 * The owner of this pool is marked as N/A indicating unknown.
	 *
	 * <b>Note: This method blocks if the pool size has reached it's 
	 * maximum size and no free connections are available 
	 * until a free connection is available.</b> The time period for which this
	 * method blocks depends on the connection-wait-time-out specified in 
	 * the configuration file. 
	 * 	 	 	 
	 *
	 * @param poolName Name of the pool.
 	 * @return Connection from the pool
	 * @exception ConnectionPoolException if there is any problem 
	 *		getting connection.	
	 */
    public Connection getConnection(String poolName) throws ConnectionPoolException;

    /**
	 * This method returns a Connection from the  pool <code>poolName</code>. 
	 * The owner of this connection is identified by <code>owner</code> .
	 *
	 * <b>Note: This method blocks if the pool size has reached it's 
	 * maximum size and no free connections are available 
	 * until a free connection is available.</b> The time period for which this
	 * method blocks depends on the connection-wait-time-out specified in 
	 * the configuration file. 
	 * 	 	 	 
	 * @param poolName Name of the pool.
	 * @param owner String identifying the owner.
 	 * @return Connection from the pool
	 * 
	 * @exception ConnectionPoolException if there is any problem 
	 *		getting connection.	
	 */
    public Connection getConnection(String poolName, String owner) throws ConnectionPoolException;

    /**
	 * This method adds  a connection leak listener.The methods of 
	 * <code>cle</code> will be called when a leak is detected as per the
	 * pool configuration.
	 *
	 * @param poolName Name of the pool.
	 * @param cle Class implementing ConnectionLeakListener interface.
	 * @exception ConnectionPoolException If there is any problem 
	 *	 adding ConnectionLeakListener.
	 */
    public void addConnectionLeakListener(String poolName, ConnectionLeakListener cle) throws ConnectionPoolException;

    /**
	 * This method removes a connection leak listener.<code>cle</code> will
	 * not get any  further notifications.
	 *
	 * @param poolName Name of the pool.
	 * @param cle Class implementing ConnectionLeakListener interface.
	 * @exception ConnectionPoolException If there is any problem 
	 *	 removing ConnectionLeakListener.
	 */
    public void removeConnectionLeakListener(String poolName, ConnectionLeakListener cle) throws ConnectionPoolException;

    /**
	 * This method returns the instance of PoolMonitor for the pool 
	 * <code>poolName</code>
	 *
	 * @param poolName Name of the pool.
	 * @return PoolMonitor interface to monitor the state of the pool
	 * @exception ConnectionPoolException 
	 *	 
	 */
    public MultiPoolMonitor getMultiPoolMonitor(String poolName) throws ConnectionPoolException;

    /**
     * This method shutsdown all the pools
     */
    public void shutDown();
}
