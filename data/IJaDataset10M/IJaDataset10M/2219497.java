package org.jdbc.support;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * This class mostly delegates the calls on the Connection interface to the
 * associated Connecton implementation.
 * <p>
 * The functionality added is that close does not close the connection but
 * releases it back into the connection pool. A timestamp of creation is set and
 * we make our Connection comparable by sorting by creation time.
 * 
 * @author Sebastian Janisch
 * 
 */
class JdbcSupportConnectionImp extends ForwardingConnection implements JdbcSupportConnection, Comparable<JdbcSupportConnectionImp> {

    private java.sql.Connection connection;

    private DbAccessConfig dbAccessConfig;

    private Long creationTime;

    public JdbcSupportConnectionImp(Connection connection, DbAccessConfig dbAccessConfig) throws SQLException {
        super(connection);
        this.dbAccessConfig = dbAccessConfig;
        this.connection = connection;
        this.creationTime = System.currentTimeMillis();
    }

    @Override
    public void close() {
        AbstractJdbcSupport.releaseConnection(this);
    }

    public int compareTo(JdbcSupportConnectionImp other) {
        return other.getCreationTime().compareTo(this.getCreationTime());
    }

    public DbAccessConfig getDbAccessConfig() {
        return dbAccessConfig;
    }

    public Long getCreationTime() {
        return creationTime;
    }

    public java.sql.Connection getEnclosedConnection() {
        return connection;
    }
}
