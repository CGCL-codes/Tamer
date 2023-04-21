package jeeobserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Level;

/**
 * The Class MySqlDatabaseHandler.
 *
 * @author Luca Mingardi
 * @version 3.1
 */
final class MySqlDatabaseHandler extends DefaultDatabaseHandler {

    /**
	 * Instantiates a new my sql database handler.
	 *
	 * @param driver the driver
	 * @param url the url
	 * @param username the username
	 * @param password the password
	 * @param schema the schema
	 * @param connectionPoolSize the connection pool size
	 * @throws DatabaseException the database exception
	 */
    public MySqlDatabaseHandler(String driver, String url, String username, String password, String schema, Integer connectionPoolSize) throws DatabaseException {
        super(driver, url, username, password, schema, connectionPoolSize);
        this.setClobDataType("TEXT");
        this.setCheckTablesExistsStatement("SELECT COUNT(*) FROM information_schema.TABLES WHERE UCASE(TABLE_SCHEMA) = UCASE(':schema') AND UPPER(TABLE_NAME) = UPPER(':table')");
        this.setCheckSchemaExistsStatement("SELECT COUNT(*) FROM information_schema.SCHEMATA WHERE UCASE(SCHEMA_NAME) = UCASE(':schema')");
        this.setCreateSchemaStatement("CREATE SCHEMA :schema");
        this.setGroupByMillisecondStatement(":timestamp");
        this.getOptimizeStatememts().add("OPTIMIZE TABLE :schema.:table");
        this.createSchema();
        this.createTables();
    }

    @Override
    public synchronized void deleteCallStatistics(Integer elementId, String contextName, String category, String project, String name, Date dateFrom, Date dateTo, Boolean extractException, String principal) throws DatabaseException {
        final Connection connection = this.getConnection();
        try {
            connection.setAutoCommit(false);
            String queryString = "DELETE " + this.getCallInvocationsSchemaAndTableName() + " FROM " + this.getCallInvocationsSchemaAndTableName() + " INNER JOIN " + this.getCallElementsSchemaAndTableName() + " ON " + this.getCallElementsSchemaAndTableName() + ".element_id =  " + this.getCallInvocationsSchemaAndTableName() + ".element_id ";
            if (principal != null) {
                queryString = queryString + "LEFT JOIN " + this.getCallPrincipalsSchemaAndTableName() + " ON " + this.getCallInvocationsSchemaAndTableName() + ".principal_id = " + this.getCallPrincipalsSchemaAndTableName() + ".principal_id ";
            }
            queryString = queryString + "WHERE ";
            if (elementId != null) {
                queryString = queryString + this.getCallElementsSchemaAndTableName() + ".elementId = ? AND ";
            }
            if (contextName != null) {
                queryString = queryString + this.getCallElementsSchemaAndTableName() + ".context_name LIKE ? AND ";
            }
            if ((category != null)) {
                queryString = queryString + this.getCallElementsSchemaAndTableName() + ".category LIKE ? AND ";
            }
            if ((project != null)) {
                queryString = queryString + this.getCallElementsSchemaAndTableName() + ".project LIKE ? AND ";
            }
            if ((name != null)) {
                queryString = queryString + this.getCallElementsSchemaAndTableName() + ".name LIKE ? AND ";
            }
            if (dateFrom != null) {
                queryString = queryString + this.getCallInvocationsSchemaAndTableName() + ".start_timestamp >= ? AND ";
            }
            if (dateTo != null) {
                queryString = queryString + this.getCallInvocationsSchemaAndTableName() + ".start_timestamp <= ? AND ";
            }
            if (principal != null) {
                queryString = queryString + this.getCallPrincipalsSchemaAndTableName() + ".principal_name LIKE ? AND ";
            }
            if (extractException != null) {
                if (extractException.booleanValue()) {
                    queryString = queryString + this.getCallInvocationsSchemaAndTableName() + ".exception_id IS NOT NULL AND ";
                } else {
                    queryString = queryString + this.getCallInvocationsSchemaAndTableName() + ".exception_id IS NULL AND ";
                }
            }
            queryString = DefaultDatabaseHandler.removeOrphanWhereAndAndFromSelect(queryString);
            final PreparedStatement preparedStatement = DebugPreparedStatement.prepareStatement(connection, queryString);
            int indexCounter = 1;
            if (elementId != null) {
                preparedStatement.setLong(indexCounter, elementId.longValue());
                indexCounter = indexCounter + 1;
            }
            if (contextName != null) {
                preparedStatement.setString(indexCounter, contextName);
                indexCounter = indexCounter + 1;
            }
            if ((category != null)) {
                preparedStatement.setString(indexCounter, category);
                indexCounter = indexCounter + 1;
            }
            if ((project != null)) {
                preparedStatement.setString(indexCounter, project);
                indexCounter = indexCounter + 1;
            }
            if ((name != null)) {
                preparedStatement.setString(indexCounter, name);
                indexCounter = indexCounter + 1;
            }
            if (dateFrom != null) {
                preparedStatement.setTimestamp(indexCounter, new Timestamp(dateFrom.getTime()));
                indexCounter = indexCounter + 1;
            }
            if (dateTo != null) {
                preparedStatement.setTimestamp(indexCounter, new Timestamp(dateTo.getTime()));
                indexCounter = indexCounter + 1;
            }
            if (principal != null) {
                preparedStatement.setString(indexCounter, principal);
                indexCounter = indexCounter + 1;
            }
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.commit();
        } catch (final SQLException e) {
            try {
                connection.rollback();
            } catch (final SQLException ex) {
                JeeObserverServerContext.logger.log(Level.SEVERE, "Transaction rollback error.", ex);
            }
            JeeObserverServerContext.logger.log(Level.SEVERE, e.getMessage());
            throw new DatabaseException("Error deleting call statistics.", e);
        } finally {
            this.releaseConnection(connection);
        }
    }

    @Override
    public synchronized void deleteHttpSessionStatistics(String contextName, String project, Date dateFrom, Date dateTo) throws DatabaseException {
        final Connection connection = this.getConnection();
        try {
            connection.setAutoCommit(false);
            String queryString = "DELETE " + this.getHttpSessionInvocationsSchemaAndTableName() + " FROM " + this.getHttpSessionInvocationsSchemaAndTableName() + " INNER JOIN " + this.getHttpSessionElementsSchemaAndTableName() + " ON " + this.getHttpSessionElementsSchemaAndTableName() + ".element_id =  " + this.getHttpSessionInvocationsSchemaAndTableName() + ".element_id WHERE ";
            if (contextName != null) {
                queryString = queryString + " context_name LIKE ? AND ";
            }
            if (project != null) {
                queryString = queryString + " project LIKE ? AND ";
            }
            if (dateFrom != null) {
                queryString = queryString + " start_timestamp >= ? AND ";
            }
            if (dateTo != null) {
                queryString = queryString + " start_timestamp <= ? AND ";
            }
            queryString = DefaultDatabaseHandler.removeOrphanWhereAndAndFromSelect(queryString);
            final PreparedStatement preparedStatement = DebugPreparedStatement.prepareStatement(connection, queryString);
            int indexCounter = 1;
            if (contextName != null) {
                preparedStatement.setString(indexCounter, contextName);
                indexCounter = indexCounter + 1;
            }
            if (project != null) {
                preparedStatement.setString(indexCounter, project);
                indexCounter = indexCounter + 1;
            }
            if (dateFrom != null) {
                preparedStatement.setTimestamp(indexCounter, new Timestamp(dateFrom.getTime()));
                indexCounter = indexCounter + 1;
            }
            if (dateTo != null) {
                preparedStatement.setTimestamp(indexCounter, new Timestamp(dateTo.getTime()));
                indexCounter = indexCounter + 1;
            }
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.commit();
        } catch (final SQLException e) {
            try {
                connection.rollback();
            } catch (final SQLException ex) {
                JeeObserverServerContext.logger.log(Level.SEVERE, "Transaction rollback error.", ex);
            }
            JeeObserverServerContext.logger.log(Level.SEVERE, e.getMessage());
            throw new DatabaseException("Error deleting HTTP session statistics.", e);
        } finally {
            this.releaseConnection(connection);
        }
    }

    @Override
    public synchronized void deleteJvmStatistics(String contextName, Date dateFrom, Date dateTo) throws DatabaseException {
        final Connection connection = this.getConnection();
        try {
            connection.setAutoCommit(false);
            String queryString = "DELETE " + this.getJvmInvocationsSchemaAndTableName() + " FROM " + this.getJvmInvocationsSchemaAndTableName() + " INNER JOIN " + this.getJvmElementsSchemaAndTableName() + " ON " + this.getJvmElementsSchemaAndTableName() + ".element_id =  " + this.getJvmInvocationsSchemaAndTableName() + ".element_id WHERE ";
            if (contextName != null) {
                queryString = queryString + " context_name LIKE ? AND ";
            }
            if (dateFrom != null) {
                queryString = queryString + " start_timestamp >= ? AND ";
            }
            if (dateTo != null) {
                queryString = queryString + " start_timestamp <= ? AND ";
            }
            queryString = DefaultDatabaseHandler.removeOrphanWhereAndAndFromSelect(queryString);
            final PreparedStatement preparedStatement = DebugPreparedStatement.prepareStatement(connection, queryString);
            int indexCounter = 1;
            if (contextName != null) {
                preparedStatement.setString(indexCounter, contextName);
                indexCounter = indexCounter + 1;
            }
            if (dateFrom != null) {
                preparedStatement.setTimestamp(indexCounter, new Timestamp(dateFrom.getTime()));
                indexCounter = indexCounter + 1;
            }
            if (dateTo != null) {
                preparedStatement.setTimestamp(indexCounter, new Timestamp(dateTo.getTime()));
                indexCounter = indexCounter + 1;
            }
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.commit();
        } catch (final SQLException e) {
            try {
                connection.rollback();
            } catch (final SQLException ex) {
                JeeObserverServerContext.logger.log(Level.SEVERE, "Transaction rollback error.", ex);
            }
            JeeObserverServerContext.logger.log(Level.SEVERE, e.getMessage());
            throw new DatabaseException("Error deleting JVM statistics.", e);
        } finally {
            this.releaseConnection(connection);
        }
    }

    @Override
    public synchronized void deletePersistenceEntityStatistics(Integer elementId, String contextName, String project, String name, Date dateFrom, Date dateTo) throws DatabaseException {
        final Connection connection = this.getConnection();
        try {
            connection.setAutoCommit(false);
            String queryString = "DELETE " + this.getPersistenceEntityStatisticsSchemaAndTableName() + " FROM " + this.getPersistenceEntityStatisticsSchemaAndTableName() + " INNER JOIN " + this.getPersistenceEntityElementsSchemaAndTableName() + " ON " + this.getPersistenceEntityElementsSchemaAndTableName() + ".element_id =  " + this.getPersistenceEntityStatisticsSchemaAndTableName() + ".element_id WHERE ";
            if (elementId != null) {
                queryString = queryString + " elementId = ? AND ";
            }
            if (contextName != null) {
                queryString = queryString + " context_name LIKE ? AND ";
            }
            if ((project != null)) {
                queryString = queryString + " project LIKE ? AND ";
            }
            if ((name != null)) {
                queryString = queryString + " name LIKE ? AND ";
            }
            if (dateFrom != null) {
                queryString = queryString + " start_timestamp >= ? AND ";
            }
            if (dateTo != null) {
                queryString = queryString + " start_timestamp <= ? AND ";
            }
            queryString = DefaultDatabaseHandler.removeOrphanWhereAndAndFromSelect(queryString);
            final PreparedStatement preparedStatement = DebugPreparedStatement.prepareStatement(connection, queryString);
            int indexCounter = 1;
            if (elementId != null) {
                preparedStatement.setLong(indexCounter, elementId.longValue());
                indexCounter = indexCounter + 1;
            }
            if (contextName != null) {
                preparedStatement.setString(indexCounter, contextName);
                indexCounter = indexCounter + 1;
            }
            if ((project != null)) {
                preparedStatement.setString(indexCounter, project);
                indexCounter = indexCounter + 1;
            }
            if ((name != null)) {
                preparedStatement.setString(indexCounter, name);
                indexCounter = indexCounter + 1;
            }
            if (dateFrom != null) {
                preparedStatement.setTimestamp(indexCounter, new Timestamp(dateFrom.getTime()));
                indexCounter = indexCounter + 1;
            }
            if (dateTo != null) {
                preparedStatement.setTimestamp(indexCounter, new Timestamp(dateTo.getTime()));
                indexCounter = indexCounter + 1;
            }
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.commit();
        } catch (final SQLException e) {
            try {
                connection.rollback();
            } catch (final SQLException ex) {
                JeeObserverServerContext.logger.log(Level.SEVERE, "Transaction rollback error.", ex);
            }
            JeeObserverServerContext.logger.log(Level.SEVERE, e.getMessage());
            throw new DatabaseException("Error deleting persistence entity statistics.", e);
        } finally {
            this.releaseConnection(connection);
        }
    }

    @Override
    public synchronized void deletePersistenceQueryStatistics(Integer elementId, String contextName, String project, String name, Date dateFrom, Date dateTo) throws DatabaseException {
        final Connection connection = this.getConnection();
        try {
            connection.setAutoCommit(false);
            String queryString = "DELETE " + this.getPersistenceQueryStatisticsSchemaAndTableName() + " FROM " + this.getPersistenceQueryStatisticsSchemaAndTableName() + " INNER JOIN " + this.getPersistenceQueryElementsSchemaAndTableName() + " ON " + this.getPersistenceQueryElementsSchemaAndTableName() + ".element_id =  " + this.getPersistenceQueryStatisticsSchemaAndTableName() + ".element_id WHERE ";
            if (elementId != null) {
                queryString = queryString + " elementId = ? AND ";
            }
            if (contextName != null) {
                queryString = queryString + " context_name LIKE ? AND ";
            }
            if ((project != null)) {
                queryString = queryString + " project LIKE ? AND ";
            }
            if ((name != null)) {
                queryString = queryString + " name LIKE ? AND ";
            }
            if (dateFrom != null) {
                queryString = queryString + " start_timestamp >= ? AND ";
            }
            if (dateTo != null) {
                queryString = queryString + " start_timestamp <= ? AND ";
            }
            queryString = DefaultDatabaseHandler.removeOrphanWhereAndAndFromSelect(queryString);
            final PreparedStatement preparedStatement = DebugPreparedStatement.prepareStatement(connection, queryString);
            int indexCounter = 1;
            if (elementId != null) {
                preparedStatement.setLong(indexCounter, elementId.longValue());
                indexCounter = indexCounter + 1;
            }
            if (contextName != null) {
                preparedStatement.setString(indexCounter, contextName);
                indexCounter = indexCounter + 1;
            }
            if ((project != null)) {
                preparedStatement.setString(indexCounter, project);
                indexCounter = indexCounter + 1;
            }
            if ((name != null)) {
                preparedStatement.setString(indexCounter, name);
                indexCounter = indexCounter + 1;
            }
            if (dateFrom != null) {
                preparedStatement.setTimestamp(indexCounter, new Timestamp(dateFrom.getTime()));
                indexCounter = indexCounter + 1;
            }
            if (dateTo != null) {
                preparedStatement.setTimestamp(indexCounter, new Timestamp(dateTo.getTime()));
                indexCounter = indexCounter + 1;
            }
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.commit();
        } catch (final SQLException e) {
            try {
                connection.rollback();
            } catch (final SQLException ex) {
                JeeObserverServerContext.logger.log(Level.SEVERE, "Transaction rollback error.", ex);
            }
            JeeObserverServerContext.logger.log(Level.SEVERE, e.getMessage());
            throw new DatabaseException("Error deleting persistence query statistics.", e);
        } finally {
            this.releaseConnection(connection);
        }
    }

    @Override
    public synchronized void deleteHardDiskStatistics(String contextName, String path, Date dateFrom, Date dateTo) throws DatabaseException {
        final Connection connection = this.getConnection();
        try {
            connection.setAutoCommit(false);
            String queryString = "DELETE " + this.getHardDiskInvocationsSchemaAndTableName() + " FROM " + this.getHardDiskInvocationsSchemaAndTableName() + " INNER JOIN " + this.getHardDiskElementsSchemaAndTableName() + " ON " + this.getHardDiskElementsSchemaAndTableName() + ".element_id =  " + this.getHardDiskInvocationsSchemaAndTableName() + ".element_id WHERE ";
            if (contextName != null) {
                queryString = queryString + " context_name LIKE ? AND ";
            }
            if (path != null) {
                queryString = queryString + " path LIKE ? AND ";
            }
            if (dateFrom != null) {
                queryString = queryString + " start_timestamp >= ? AND ";
            }
            if (dateTo != null) {
                queryString = queryString + " start_timestamp <= ? AND ";
            }
            queryString = DefaultDatabaseHandler.removeOrphanWhereAndAndFromSelect(queryString);
            final PreparedStatement preparedStatement = DebugPreparedStatement.prepareStatement(connection, queryString);
            int indexCounter = 1;
            if (contextName != null) {
                preparedStatement.setString(indexCounter, contextName);
                indexCounter = indexCounter + 1;
            }
            if (path != null) {
                preparedStatement.setString(indexCounter, path);
                indexCounter = indexCounter + 1;
            }
            if (dateFrom != null) {
                preparedStatement.setTimestamp(indexCounter, new Timestamp(dateFrom.getTime()));
                indexCounter = indexCounter + 1;
            }
            if (dateTo != null) {
                preparedStatement.setTimestamp(indexCounter, new Timestamp(dateTo.getTime()));
                indexCounter = indexCounter + 1;
            }
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.commit();
        } catch (final SQLException e) {
            try {
                connection.rollback();
            } catch (final SQLException ex) {
                JeeObserverServerContext.logger.log(Level.SEVERE, "Transaction rollback error.", ex);
            }
            JeeObserverServerContext.logger.log(Level.SEVERE, e.getMessage());
            throw new DatabaseException("Error deleting disk statistics.", e);
        } finally {
            this.releaseConnection(connection);
        }
    }

    @Override
    protected void removeOrphansElements() throws DatabaseException {
        this.getIdChache().clear();
        final Connection connection = this.getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement;
            preparedStatement = DebugPreparedStatement.prepareStatement(connection, "DELETE " + this.getCallElementsSchemaAndTableName() + " FROM " + this.getCallElementsSchemaAndTableName() + " LEFT JOIN " + this.getCallInvocationsSchemaAndTableName() + " ON " + this.getCallElementsSchemaAndTableName() + ".element_id =  " + this.getCallInvocationsSchemaAndTableName() + ".element_id WHERE " + this.getCallInvocationsSchemaAndTableName() + ".element_id IS NULL");
            preparedStatement.executeUpdate();
            preparedStatement.close();
            preparedStatement = DebugPreparedStatement.prepareStatement(connection, "DELETE " + this.getCallExceptionsSchemaAndTableName() + " FROM " + this.getCallExceptionsSchemaAndTableName() + " LEFT JOIN " + this.getCallInvocationsSchemaAndTableName() + " ON " + this.getCallExceptionsSchemaAndTableName() + ".exception_id =  " + this.getCallInvocationsSchemaAndTableName() + ".exception_id WHERE " + this.getCallInvocationsSchemaAndTableName() + ".exception_id IS NULL");
            preparedStatement.executeUpdate();
            preparedStatement.close();
            preparedStatement = DebugPreparedStatement.prepareStatement(connection, "DELETE " + this.getCallPrincipalsSchemaAndTableName() + " FROM " + this.getCallPrincipalsSchemaAndTableName() + " LEFT JOIN " + this.getCallInvocationsSchemaAndTableName() + " ON " + this.getCallPrincipalsSchemaAndTableName() + ".principal_id =  " + this.getCallInvocationsSchemaAndTableName() + ".principal_id WHERE " + this.getCallInvocationsSchemaAndTableName() + ".principal_id IS NULL");
            preparedStatement.executeUpdate();
            preparedStatement.close();
            preparedStatement = DebugPreparedStatement.prepareStatement(connection, "DELETE " + this.getHttpSessionElementsSchemaAndTableName() + " FROM " + this.getHttpSessionElementsSchemaAndTableName() + " LEFT JOIN " + this.getHttpSessionInvocationsSchemaAndTableName() + " ON " + this.getHttpSessionElementsSchemaAndTableName() + ".element_id =  " + this.getHttpSessionInvocationsSchemaAndTableName() + ".element_id WHERE " + this.getHttpSessionInvocationsSchemaAndTableName() + ".element_id IS NULL");
            preparedStatement.executeUpdate();
            preparedStatement.close();
            preparedStatement = DebugPreparedStatement.prepareStatement(connection, "DELETE " + this.getJvmElementsSchemaAndTableName() + " FROM " + this.getJvmElementsSchemaAndTableName() + " LEFT JOIN " + this.getJvmInvocationsSchemaAndTableName() + " ON " + this.getJvmElementsSchemaAndTableName() + ".element_id =  " + this.getJvmInvocationsSchemaAndTableName() + ".element_id WHERE " + this.getJvmInvocationsSchemaAndTableName() + ".element_id IS NULL");
            preparedStatement.executeUpdate();
            preparedStatement.close();
            preparedStatement = DebugPreparedStatement.prepareStatement(connection, "DELETE " + this.getPersistenceEntityElementsSchemaAndTableName() + " FROM " + this.getPersistenceEntityElementsSchemaAndTableName() + " LEFT JOIN " + this.getPersistenceEntityStatisticsSchemaAndTableName() + " ON " + this.getPersistenceEntityElementsSchemaAndTableName() + ".element_id =  " + this.getPersistenceEntityStatisticsSchemaAndTableName() + ".element_id WHERE " + this.getPersistenceEntityStatisticsSchemaAndTableName() + ".element_id IS NULL ");
            preparedStatement.executeUpdate();
            preparedStatement.close();
            preparedStatement = DebugPreparedStatement.prepareStatement(connection, "DELETE " + this.getPersistenceQueryElementsSchemaAndTableName() + " FROM " + this.getPersistenceQueryElementsSchemaAndTableName() + " LEFT JOIN " + this.getPersistenceQueryStatisticsSchemaAndTableName() + " ON " + this.getPersistenceQueryElementsSchemaAndTableName() + ".element_id =  " + this.getPersistenceQueryStatisticsSchemaAndTableName() + ".element_id WHERE " + this.getPersistenceQueryStatisticsSchemaAndTableName() + ".element_id IS NULL ");
            preparedStatement.executeUpdate();
            preparedStatement.close();
            preparedStatement = DebugPreparedStatement.prepareStatement(connection, "DELETE " + this.getHardDiskElementsSchemaAndTableName() + " FROM " + this.getHardDiskElementsSchemaAndTableName() + " LEFT JOIN " + this.getHardDiskInvocationsSchemaAndTableName() + " ON " + this.getHardDiskElementsSchemaAndTableName() + ".element_id =  " + this.getHardDiskInvocationsSchemaAndTableName() + ".element_id WHERE " + this.getHardDiskInvocationsSchemaAndTableName() + ".element_id IS NULL ");
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.commit();
        } catch (final SQLException e) {
            try {
                connection.rollback();
            } catch (final SQLException ex) {
                JeeObserverServerContext.logger.log(Level.SEVERE, "Transaction rollback error.", ex);
            }
            JeeObserverServerContext.logger.log(Level.SEVERE, e.getMessage());
            throw new DatabaseException("Error cleaning database.", e);
        } finally {
            this.releaseConnection(connection);
        }
        return;
    }
}
