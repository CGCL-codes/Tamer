package com.teletalk.jserver.jdbc;

import java.sql.Connection;

/**
 * Connection wrapper interface for creating proxies for pooled connections. This interface is used by {@link PooledDataSource}
 * to make sure that a connection is returned to the pool when the {@link #close()} is called. This interface contains a method        
 * that makes it possible to get the underlying connection.
 * 
 * @author Tobias L�fstrand
 * 
 * @since 2.1
 */
public interface PooledConnection extends Connection {

    public Connection getTargetConnection();
}
