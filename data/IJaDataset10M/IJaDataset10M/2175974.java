package org.dbe.composer.wfengine.bpel.server.engine.storage.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.log4j.Logger;
import org.dbe.composer.wfengine.bpel.server.engine.storage.SdlStorageException;

/**
 * Base class for objects that access the database.
 */
public class SdlSQLObject {

    private static final Logger logger = Logger.getLogger(SdlSQLObject.class.getName());

    /** Executes SQL queries. */
    private final QueryRunner mQueryRunner = new SdlQueryRunner(getDataSource());

    /**
     * Returns database connection.
     *
     * @throws SdlStorageException
     */
    protected Connection getConnection() throws SdlStorageException {
        try {
            return getDataSource().getConnection();
        } catch (SQLException e) {
            logger.error("Error connecting to the database.\nPlease check that the database is available and properly configured then restart the application.");
            throw new SdlStorageException("Error connecting to the database.\nPlease check that the database is available and properly configured then restart the application.", e);
        }
    }

    /**
     * Returns a new database connection, outside of the current transaction if
     * one is active.
     *
     * @throws SdlStorageException
     */
    protected Connection getNewConnection() throws SdlStorageException {
        try {
            return getDataSource().getNewConnection();
        } catch (SQLException e) {
            logger.error("Error connecting to the database.\nPlease check that the database is available and properly configured then restart the application.");
            throw new SdlStorageException("Error connecting to the database.\nPlease check that the database is available and properly configured then restart the application.", e);
        }
    }

    /**
     * Accessor for connection with auto-commit flag set to false.
     * Callers must explicitly call commit to persist.
     * @throws SdlStorageException
     */
    protected Connection getCommitControlConnection() throws SdlStorageException {
        return getCommitControlConnection(false);
    }

    /**
     * Accessor for connection with auto-commit flag set to false.
     * Callers must explicitly call commit to persist.
     *
     * @param aVerifyFlag Verify that {@link java.sql.Connection#commit()} or {@link java.sql.Connection#rollback()} is called.
     * @throws SdlStorageException
     */
    protected Connection getCommitControlConnection(boolean aVerifyFlag) throws SdlStorageException {
        try {
            return getDataSource().getCommitControlConnection(aVerifyFlag);
        } catch (SQLException e) {
            logger.error("Error connecting to the database for storage transaction.\nPlease check that the database is available and properly configured then restart the application.");
            throw new SdlStorageException("Error connecting to the database for storage transaction.\nPlease check that the database is available and properly configured then restart the application.", e);
        }
    }

    /**
     * Returns database connection directly from container data source.
     *
     * @throws SQLException
     */
    protected Connection getContainerManagedConnection() throws SdlStorageException {
        try {
            return getDataSource().getContainerManagedConnection();
        } catch (SQLException e) {
            logger.error("Error connecting to the database.\nPlease check that the database is available and properly configured then restart the application.");
            throw new SdlStorageException("Error connecting to the database.\nPlease check that the database is available and properly configured then restart the application.", e);
        }
    }

    /**
     * Returns the <code>DataSource</code> to use for SQL operations.
     */
    protected SdlDataSource getDataSource() {
        return SdlDataSource.MAIN;
    }

    /**
     * Returns the <code>QueryRunner</code> to use for queries and updates.
     */
    protected QueryRunner getQueryRunner() {
        return mQueryRunner;
    }

    /**
     * Convenience method that returns the date passed in or the sql type null for
     * timestamp.
     *
     * @param aDate
     */
    protected Object getDateOrSqlNull(Date aDate) {
        if (aDate == null) {
            return SdlQueryRunner.NULL_TIMESTAMP;
        }
        return aDate;
    }

    /**
     * Convenience method that returns the date passed as a Long (time in millis) or a
     * NULL_BIGINT if the date is null.
     *
     * @param aDate
     */
    protected Object getDateAsLongOrSqlNull(Date aDate) {
        if (aDate == null) {
            return SdlQueryRunner.NULL_BIGINT;
        }
        return new Long(aDate.getTime());
    }
}
