package com.protomatter.jdbc.pool;

import java.sql.*;
import javax.sql.*;
import java.util.*;
import com.protomatter.util.*;
import com.protomatter.pool.*;

/**
 *  An implementation of the <tt>javax.sql.PooledConnection</tt> interface.
 *  This is only for use with the <tt>JdbcConnectionPoolDataSource</tt>
 *  class.
 *
 *  @see JdbcConnectionPoolDataSource
 *  @see JdbcConnectionPoolDriver
 *  @see JdbcConnectionPool
 *  @see javax.sql.PooledConnection
 */
public class JdbcConnectionPoolPooledConnection implements PooledConnection {

    private JdbcConnectionPoolConnection connection = null;

    /**
   *  Create a new <tt>JdbcConnectionPoolPooledConnection</tt> wrapping
   *  the given <tt>JdbcConnectionPoolConnection</tt>.
   */
    JdbcConnectionPoolPooledConnection(JdbcConnectionPoolConnection connection) {
        this.connection = connection;
    }

    /**
   *  @see javax.sql.ConnectionPoolDataSource
   */
    public Connection getConnection() throws SQLException {
        if (this.connection.isObjectPoolObjectValid()) return (Connection) this.connection;
        throw new SQLException("The connection pool connection is no longer valid");
    }

    /**
   *  @see javax.sql.ConnectionPoolDataSource
   */
    public void close() throws SQLException {
        if (this.connection.isObjectPoolObjectValid()) this.connection.reallyClose();
    }

    /**
   *  Not currently implemented.
   *
   *  @see javax.sql.ConnectionPoolDataSource
   */
    public void addConnectionEventListener(ConnectionEventListener listener) {
    }

    /**
   *  Not currently implemented.
   *
   *  @see javax.sql.ConnectionPoolDataSource
   */
    public void removeConnectionEventListener(ConnectionEventListener listener) {
    }
}
