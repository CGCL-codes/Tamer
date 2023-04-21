package org.cofax.connectionpool;

import java.sql.*;
import java.util.*;

/**
 *  This class is a wrapper around a Connection, overriding the close method to
 *  just inform the pool that it's available for reuse again, and the isClosed
 *  method to return the state of the wrapper instead of the Connection.
 *
 *@author     kmartino
 *@created    August 11, 2002
 */
public class ConnectionWrapper implements Connection {

    private static long counter = 0;

    Connection realConn;

    private ConnectionPool pool;

    private boolean isClosed = false;

    private long id;

    private int timesUsed;

    private long timeCreated;

    private long lastTimeUsed;

    private String currentActivity;

    /**
	 *  Constructor for the ConnectionWrapper object
	 *
	 *@param  realConn  Description of Parameter
	 *@param  pool      Description of Parameter
	 */
    public ConnectionWrapper(Connection realConn, ConnectionPool pool) {
        this.realConn = realConn;
        this.pool = pool;
        timesUsed = 0;
        counter++;
        id = counter;
        java.util.Date currDate = new java.util.Date();
        timeCreated = currDate.getTime();
        lastTimeUsed = currDate.getTime();
    }

    /**
	 *  Sets the lastTimeUsed attribute of the ConnectionWrapper object
	 */
    public void setLastTimeUsed() {
        java.util.Date currDate = new java.util.Date();
        lastTimeUsed = currDate.getTime();
    }

    /**
	 *  Function: setCurrentActivity Purpose: to set the query that this connection
	 *  is 'about' to run Args: activity - the string representing what this
	 *  connection is doing Author: Sam Cohen Created: 7/23/2001 Revision History:
	 *  8/29/2001 - made it only take up to 50 chars to keep run-aways out of the
	 *  log file
	 *
	 *@param  activity  The new currentActivity value
	 */
    public void setCurrentActivity(String activity) {
        int junkStart = activity.indexOf("   ");
        if (junkStart != -1) {
            this.currentActivity = activity.substring(0, junkStart);
        } else if (activity.length() > 250) {
            this.currentActivity = activity.substring(0, 250);
        } else {
            this.currentActivity = activity;
        }
    }

    /**
	 *  Sets the holdability attribute of the ConnectionWrapper object
	 *
	 *@param  holdability               The new holdability value
	 *@exception  SQLException  Description of Exception
	 */
    public void setHoldability(int holdability) throws SQLException {
        if (isClosed) {
            throw new SQLException("Pooled connection is closed");
        }
        realConn.setHoldability(holdability);
    }

    /**
	 *  Gets the holdability attribute of the ConnectionWrapper object
	 *
	 */
    public int getHoldability() throws SQLException {
        if (isClosed) {
            throw new SQLException("Pooled connection is closed");
        }
        return realConn.getHoldability();
    }

    /**
	 *  Sets the savepoint attribute of the ConnectionWrapper object
	 *
	 *@exception  SQLException  Description of Exception
	 */
    public Savepoint setSavepoint() throws SQLException {
        if (isClosed) {
            throw new SQLException("Pooled connection is closed");
        }
        return realConn.setSavepoint();
    }

    /**
	 *  Sets the savepoint attribute of the ConnectionWrapper object
	 *
	 *@param  holdability               The new holdability value
	 *@exception  SQLException  Description of Exception
	 */
    public Savepoint setSavepoint(String savepoint) throws SQLException {
        if (isClosed) {
            throw new SQLException("Pooled connection is closed");
        }
        return realConn.setSavepoint(savepoint);
    }

    /**
	 *  Executes the rollback method of the ConnectionWrapper object
	 *
	 *@param  holdability               The new holdability value
	 *@exception  SQLException  Description of Exception
	 */
    public void rollback(Savepoint savepoint) throws SQLException {
        if (isClosed) {
            throw new SQLException("Pooled connection is closed");
        }
        realConn.rollback(savepoint);
    }

    /**
	 *  Executes the createStatement method of the ConnectionWrapper object
	 *
	 *@param resultSetType one of the following ResultSet 
	 * constants: ResultSet.TYPE_FORWARD_ONLY, ResultSet.TYPE_SCROLL_INSENSITIVE, or ResultSet.TYPE_SCROLL_SENSITIVE
	 *@param resultSetConcurrency one of the following ResultSet 
	 * constants: ResultSet.CONCUR_READ_ONLY or ResultSet.CONCUR_UPDATABLE
	 *@param resultSetHoldability one of the following ResultSet 
	 * constants: ResultSet.HOLD_CURSORS_OVER_COMMIT or ResultSet.CLOSE_CURSORS_AT_COMMIT 
	 *@exception  SQLException  if a database access error occurs or the given 
	 * parameters are not ResultSet constants indicating type, concurrency, and holdability
	 *@return  a new Statement object that will generate ResultSet objects with the given type, concurrency, and holdability       
	 */
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        if (isClosed) {
            throw new SQLException("Pooled connection is closed");
        }
        return realConn.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    /**
	 *  Executes the prepareStatement method of the ConnectionWrapper object
	 *
	 *@param sql a String object that is the SQL statement to be sent to the database; 
	 * may contain one or more ? IN parameters
	 *@param resultSetType one of the following ResultSet 
	 * constants: ResultSet.TYPE_FORWARD_ONLY, ResultSet.TYPE_SCROLL_INSENSITIVE, or ResultSet.TYPE_SCROLL_SENSITIVE
	 *@param resultSetConcurrency one of the following ResultSet 
	 * constants: ResultSet.CONCUR_READ_ONLY or ResultSet.CONCUR_UPDATABLE
	 *@param resultSetHoldability one of the following ResultSet 
	 * constants: ResultSet.HOLD_CURSORS_OVER_COMMIT or ResultSet.CLOSE_CURSORS_AT_COMMIT 
	 *@exception  SQLException  if a database access error occurs or the given 
	 * parameters are not ResultSet constants indicating type, concurrency, and holdability
	 *@return  a new Statement object that will generate ResultSet objects with the given type, concurrency, and holdability       
	 */
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        if (isClosed) {
            throw new SQLException("Pooled connection is closed");
        }
        return realConn.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    /**
	 *  Executes the prepareStatement method of the ConnectionWrapper object
	 *
	 *@param sql a String object that is the SQL statement to be sent to the database; 
	 * may contain one or more ? IN parameters
	 *@param autoGeneratedKeys a flag indicating whether auto-generated keys 
	 * should be returned; one of the following Statement constants: 
	 *@exception  SQLException  if a database access error occurs or the given 
	 * parameters are not ResultSet constants indicating type, concurrency, and holdability
	 *@return  a new Statement object that will generate ResultSet objects with the given type, concurrency, and holdability       
	 */
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        if (isClosed) {
            throw new SQLException("Pooled connection is closed");
        }
        return realConn.prepareStatement(sql, autoGeneratedKeys);
    }

    /**
	 *  Executes the prepareStatement method of the ConnectionWrapper object
	 *
	 *@param sql a String object that is the SQL statement to be sent to the database; 
	 * may contain one or more ? IN parameters
	 *@param columnIndexes an array of column indexes indicating the columns that 
	 * should be returned from the inserted row or rows 
	 *@exception  SQLException  if a database access error occurs or the given 
	 * parameters are not ResultSet constants indicating type, concurrency, and holdability
	 *@return  a new Statement object that will generate ResultSet objects with the given type, concurrency, and holdability       
	 */
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        if (isClosed) {
            throw new SQLException("Pooled connection is closed");
        }
        return realConn.prepareStatement(sql, columnIndexes);
    }

    /**
	 *  Executes the prepareStatement method of the ConnectionWrapper object
	 *
	 *@param sql a String object that is the SQL statement to be sent to the database; 
	 * may contain one or more ? IN parameters
	 *@param columnNames - an array of column names indicating the columns that 
	 * should be returned from the inserted row or rows 
	 *@exception  SQLException  if a database access error occurs or the given 
	 * parameters are not ResultSet constants indicating type, concurrency, and holdability
	 *@return  a new Statement object that will generate ResultSet objects with the given type, concurrency, and holdability       
	 */
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        if (isClosed) {
            throw new SQLException("Pooled connection is closed");
        }
        return realConn.prepareStatement(sql, columnNames);
    }

    /**
	 *  Executes the prepareCall method of the ConnectionWrapper object
	 *
	 *@param sql a String object that is the SQL statement to be sent to the database; 
	 * may contain one or more ? IN parameters
	 *@param resultSetType one of the following ResultSet 
	 * constants: ResultSet.TYPE_FORWARD_ONLY, ResultSet.TYPE_SCROLL_INSENSITIVE, or ResultSet.TYPE_SCROLL_SENSITIVE
	 *@param resultSetConcurrency one of the following ResultSet 
	 * constants: ResultSet.CONCUR_READ_ONLY or ResultSet.CONCUR_UPDATABLE
	 *@param resultSetHoldability one of the following ResultSet 
	 * constants: ResultSet.HOLD_CURSORS_OVER_COMMIT or ResultSet.CLOSE_CURSORS_AT_COMMIT 
	 *@exception  SQLException  if a database access error occurs or the given 
	 * parameters are not ResultSet constants indicating type, concurrency, and holdability
	 *@return  a new Statement object that will generate ResultSet objects with the given type, concurrency, and holdability       
	 */
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        if (isClosed) {
            throw new SQLException("Pooled connection is closed");
        }
        return realConn.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    /**
	 *  Executes the releaseSavepoint method of the ConnectionWrapper object
	 *
	 *@param  holdability               The new holdability value
	 *@exception  SQLException  Description of Exception
	 */
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        if (isClosed) {
            throw new SQLException("Pooled connection is closed");
        }
        realConn.releaseSavepoint(savepoint);
    }

    /**
	 *  Sets the typeMap attribute of the ConnectionWrapper object
	 *
	 *@param  map               The new typeMap value
	 *@exception  SQLException  Description of Exception
	 */
    public void setTypeMap(Map map) throws SQLException {
        if (isClosed) {
            throw new SQLException("Pooled connection is closed");
        }
        realConn.setTypeMap(map);
    }

    /**
	 *  Sets the autoCommit attribute of the ConnectionWrapper object
	 *
	 *@param  autoCommit        The new autoCommit value
	 *@exception  SQLException  Description of Exception
	 */
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        if (isClosed) {
            throw new SQLException("Pooled connection is closed");
        }
        realConn.setAutoCommit(autoCommit);
    }

    /**
	 *  Sets the catalog attribute of the ConnectionWrapper object
	 *
	 *@param  catalog           The new catalog value
	 *@exception  SQLException  Description of Exception
	 */
    public void setCatalog(String catalog) throws SQLException {
        if (isClosed) {
            throw new SQLException("Pooled connection is closed");
        }
        realConn.setCatalog(catalog);
    }

    /**
	 *  Sets the readOnly attribute of the ConnectionWrapper object
	 *
	 *@param  readOnly          The new readOnly value
	 *@exception  SQLException  Description of Exception
	 */
    public void setReadOnly(boolean readOnly) throws SQLException {
        if (isClosed) {
            throw new SQLException("Pooled connection is closed");
        }
        realConn.setReadOnly(readOnly);
    }

    /**
	 *  Sets the transactionIsolation attribute of the ConnectionWrapper object
	 *
	 *@param  level             The new transactionIsolation value
	 *@exception  SQLException  Description of Exception
	 */
    public void setTransactionIsolation(int level) throws SQLException {
        if (isClosed) {
            throw new SQLException("Pooled connection is closed");
        }
        realConn.setTransactionIsolation(level);
    }

    /**
	 *  Gets the timesUsed attribute of the ConnectionWrapper object
	 *
	 *@return    The timesUsed value
	 */
    public int getTimesUsed() {
        return timesUsed;
    }

    /**
	 *  Gets the timeCreated attribute of the ConnectionWrapper object
	 *
	 *@return    The timeCreated value
	 */
    public long getTimeCreated() {
        return timeCreated;
    }

    /**
	 *  Gets the lastTimeUsed attribute of the ConnectionWrapper object
	 *
	 *@return    The lastTimeUsed value
	 */
    public long getLastTimeUsed() {
        return lastTimeUsed;
    }

    /**
	 *  Gets the connectionId attribute of the ConnectionWrapper object
	 *
	 *@return    The connectionId value
	 */
    public long getConnectionId() {
        return id;
    }

    /**
	 *  Gets the currentActivity attribute of the ConnectionWrapper object
	 *
	 *@return    The currentActivity value
	 */
    public String getCurrentActivity() {
        return this.currentActivity;
    }

    /**
	 *  Returns true if the ConnectionWrapper is closed, false otherwise.
	 *
	 *@return                   The closed value
	 *@exception  SQLException  Description of Exception
	 */
    public boolean isClosed() throws SQLException {
        return isClosed;
    }

    /**
	 *  Gets the autoCommit attribute of the ConnectionWrapper object
	 *
	 *@return                   The autoCommit value
	 *@exception  SQLException  Description of Exception
	 */
    public boolean getAutoCommit() throws SQLException {
        if (isClosed) {
            throw new SQLException("Pooled connection is closed");
        }
        return realConn.getAutoCommit();
    }

    /**
	 *  Gets the catalog attribute of the ConnectionWrapper object
	 *
	 *@return                   The catalog value
	 *@exception  SQLException  Description of Exception
	 */
    public String getCatalog() throws SQLException {
        if (isClosed) {
            throw new SQLException("Pooled connection is closed");
        }
        return realConn.getCatalog();
    }

    /**
	 *  Gets the metaData attribute of the ConnectionWrapper object
	 *
	 *@return                   The metaData value
	 *@exception  SQLException  Description of Exception
	 */
    public DatabaseMetaData getMetaData() throws SQLException {
        if (isClosed) {
            throw new SQLException("Pooled connection is closed");
        }
        return realConn.getMetaData();
    }

    /**
	 *  Gets the typeMap attribute of the ConnectionWrapper object
	 *
	 *@return                   The typeMap value
	 *@exception  SQLException  Description of Exception
	 */
    public Map getTypeMap() throws SQLException {
        if (isClosed) {
            throw new SQLException("Pooled connection is closed");
        }
        return realConn.getTypeMap();
    }

    /**
	 *  Gets the transactionIsolation attribute of the ConnectionWrapper object
	 *
	 *@return                   The transactionIsolation value
	 *@exception  SQLException  Description of Exception
	 */
    public int getTransactionIsolation() throws SQLException {
        if (isClosed) {
            throw new SQLException("Pooled connection is closed");
        }
        return realConn.getTransactionIsolation();
    }

    /**
	 *  Gets the warnings attribute of the ConnectionWrapper object
	 *
	 *@return                   The warnings value
	 *@exception  SQLException  Description of Exception
	 */
    public SQLWarning getWarnings() throws SQLException {
        if (isClosed) {
            throw new SQLException("Pooled connection is closed");
        }
        return realConn.getWarnings();
    }

    /**
	 *  Gets the readOnly attribute of the ConnectionWrapper object
	 *
	 *@return                   The readOnly value
	 *@exception  SQLException  Description of Exception
	 */
    public boolean isReadOnly() throws SQLException {
        if (isClosed) {
            throw new SQLException("Pooled connection is closed");
        }
        return realConn.isReadOnly();
    }

    /**
	 *  Description of the Method
	 */
    public void incrementTimesUsed() {
        timesUsed++;
        setLastTimeUsed();
    }

    /**
	 *  Inform the ConnectionPool that the ConnectionWrapper is closed.
	 *
	 *@exception  SQLException  Description of Exception
	 */
    public void close() throws SQLException {
        pool.freeConnection(this);
    }

    /**
	 *  Description of the Method
	 *
	 *@exception  SQLException  Description of Exception
	 */
    public void closeConnection() throws SQLException {
        try {
            realConn.close();
        } catch (Exception e) {
        } finally {
            realConn = null;
            isClosed = true;
        }
    }

    /**
	 *  Description of the Method
	 *
	 *@exception  SQLException  Description of Exception
	 */
    public void clearWarnings() throws SQLException {
        if (isClosed) {
            throw new SQLException("Pooled connection is closed");
        }
        realConn.clearWarnings();
    }

    /**
	 *  Description of the Method
	 *
	 *@exception  SQLException  Description of Exception
	 */
    public void commit() throws SQLException {
        if (isClosed) {
            throw new SQLException("Pooled connection is closed");
        }
        realConn.commit();
    }

    /**
	 *  Description of the Method
	 *
	 *@return                   Description of the Returned Value
	 *@exception  SQLException  Description of Exception
	 */
    public Statement createStatement() throws SQLException {
        if (isClosed) {
            throw new SQLException("Pooled connection is closed");
        }
        return realConn.createStatement();
    }

    /**
	 *  Description of the Method
	 *
	 *@param  resultSetType         Description of Parameter
	 *@param  resultSetConcurrency  Description of Parameter
	 *@return                       Description of the Returned Value
	 *@exception  SQLException      Description of Exception
	 */
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        if (isClosed) {
            throw new SQLException("Pooled connection is closed");
        }
        return realConn.createStatement(resultSetType, resultSetConcurrency);
    }

    /**
	 *  Description of the Method
	 *
	 *@param  sql               Description of Parameter
	 *@return                   Description of the Returned Value
	 *@exception  SQLException  Description of Exception
	 */
    public String nativeSQL(String sql) throws SQLException {
        if (isClosed) {
            throw new SQLException("Pooled connection is closed");
        }
        return realConn.nativeSQL(sql);
    }

    /**
	 *  Description of the Method
	 *
	 *@param  sql               Description of Parameter
	 *@return                   Description of the Returned Value
	 *@exception  SQLException  Description of Exception
	 */
    public CallableStatement prepareCall(String sql) throws SQLException {
        if (isClosed) {
            throw new SQLException("Pooled connection is closed");
        }
        return realConn.prepareCall(sql);
    }

    /**
	 *  Description of the Method
	 *
	 *@param  sql                   Description of Parameter
	 *@param  resultSetType         Description of Parameter
	 *@param  resultSetConcurrency  Description of Parameter
	 *@return                       Description of the Returned Value
	 *@exception  SQLException      Description of Exception
	 */
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        if (isClosed) {
            throw new SQLException("Pooled connection is closed");
        }
        return realConn.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    /**
	 *  Description of the Method
	 *
	 *@param  sql               Description of Parameter
	 *@return                   Description of the Returned Value
	 *@exception  SQLException  Description of Exception
	 */
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        if (isClosed) {
            throw new SQLException("Pooled connection is closed");
        }
        return realConn.prepareStatement(sql);
    }

    /**
	 *  Description of the Method
	 *
	 *@param  sql                   Description of Parameter
	 *@param  resultSetType         Description of Parameter
	 *@param  resultSetConcurrency  Description of Parameter
	 *@return                       Description of the Returned Value
	 *@exception  SQLException      Description of Exception
	 */
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        if (isClosed) {
            throw new SQLException("Pooled connection is closed");
        }
        return realConn.prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    /**
	 *  Description of the Method
	 *
	 *@exception  SQLException  Description of Exception
	 */
    public void rollback() throws SQLException {
        if (isClosed) {
            throw new SQLException("Pooled connection is closed");
        }
        realConn.rollback();
    }
}
