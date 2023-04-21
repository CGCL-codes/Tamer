package com.google.code.nanorm.internal.session;

import java.sql.Connection;
import java.sql.SQLException;
import com.google.code.nanorm.exceptions.SessionException;

/**
 * {@link SessionSpi} implementation that is build on top of externally provided
 * connection.
 * 
 * The connection is not closed at the session end. All queries are executed
 * using this single connection.
 * 
 * By default, allows running multiple queries on the same connection at the
 * same time.
 * 
 * @author Ivan Dubrov
 * @version 1.0 19.06.2008
 */
public class SingleConnSessionSpi implements SessionSpi {

    private final Connection connection;

    private final boolean isAllowMultiple;

    /**
     * Constructor.
     * @param connection connection to use for this session.
     */
    public SingleConnSessionSpi(Connection connection) {
        this(connection, true);
    }

    /**
     * Constructor.
     * @param connection connection to use for this session.
     * @param isAllowMultiple allow running multiple queries at the same time in
     * the single session.
     */
    public SingleConnSessionSpi(Connection connection, boolean isAllowMultiple) {
        this.connection = connection;
        this.isAllowMultiple = isAllowMultiple;
    }

    /**
     * {@inheritDoc}
     */
    public void commit() {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new SessionException("Failed to commit the JDBC transaction!", e);
        }
    }

    /**
     * This method does nothing. The connection is provided externaly, therefore
     * we don't close it.
     */
    public void end() {
    }

    /**
     * {@inheritDoc}
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isAllowMultipleQueries() {
        return isAllowMultiple;
    }

    /**
     * This method does nothing. We use single connection for all requests.
     * @param conn not used
     */
    public void releaseConnection(Connection conn) {
    }

    /**
     * {@inheritDoc}
     */
    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new SessionException("Failed to rollback the JDBC transaction!", e);
        }
    }
}
