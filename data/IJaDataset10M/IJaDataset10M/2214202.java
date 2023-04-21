package com.volantis.mcs.repository.jdbc;

import com.volantis.mcs.repository.RepositoryConnection;
import com.volantis.mcs.repository.RepositoryEnumeration;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.LocalRepositoryConnection;
import java.sql.Connection;

/**
 * @mock.generate
 */
public interface JDBCRepositoryConnection extends LocalRepositoryConnection {

    /**
     * Get the underlying jdbc Connection for this JDBCConnection object. If
     * the connection is not already established then this method will handle
     * that.
     *
     * @return the Connection object of this connection
     * @throws com.volantis.mcs.repository.RepositoryException
     *          if there is a problem accessing the
     *          repository
     */
    public Connection getConnection() throws RepositoryException;

    /**
     * Close the real connection if there is one.
     *
     * @throws com.volantis.mcs.repository.RepositoryException
     *          if there was a problem closing the real
     *          connection.
     */
    public void releaseConnection() throws RepositoryException;

    /**
     * Close this RepositoryConnection and the underlying jdbc Connection if
     * it exists.
     *
     * @throws com.volantis.mcs.repository.RepositoryException
     *          if there is a problem accessing the
     *          repository
     */
    public void closeConnection() throws RepositoryException;

    public InternalJDBCRepository getJDBCRepository();

    public RepositoryEnumeration selectUniqueValues(String columnName, String tableName, String projectName) throws RepositoryException;

    public String getVendorSpecificSQLKeyWord(String keyword);

    public RepositoryConnection getUnderLyingConnection() throws RepositoryException;
}
