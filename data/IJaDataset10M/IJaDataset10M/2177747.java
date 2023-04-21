package org.datascooter.impl;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.ConnectionPoolDataSource;
import org.datascooter.inface.IContextConnector;
import org.datascooter.inface.IDataSource;
import org.datascooter.utils.DSSettings;

/**
 * Abstract object for creating pooled connection with different types of database
 * 
 * @author nemo
 * 
 */
public abstract class ContextConnector implements IContextConnector {

    protected IDataSource dataSource;

    private ConnectionPoolDataSource source;

    public abstract ConnectionPoolDataSource createPooledDataSource() throws SQLException;

    /**
	 * Returns a database context Id - a type of database
	 */
    public abstract String getContextId();

    public ContextConnector() {
    }

    public ContextConnector(IDataSource dataSource) throws SQLException {
        this.dataSource = dataSource;
        source = createPooledDataSource();
    }

    /**
	 * Retrieves a database connection from a pool
	 */
    @Override
    public Connection getConnection() throws SQLException {
        Connection connection = source.getPooledConnection().getConnection();
        connection.setTransactionIsolation(DSSettings.getTransactionIsolation());
        connection.setHoldability(DSSettings.getResultSetHoldability());
        connection.setAutoCommit(false);
        return connection;
    }

    @Override
    public void close() {
        try {
            if (source != null) {
                source.getPooledConnection().close();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public IDataSource getDataSource() {
        return dataSource;
    }

    /**
	 * Informs that this database supports a sql clauses for partially select operations if not - datascooter can replace such functionality
	 */
    public boolean isSupportPagination() {
        return false;
    }

    public ConnectionPoolDataSource getSource() {
        return source;
    }

    @Override
    public boolean isCallParameterMetadata() {
        return false;
    }
}
