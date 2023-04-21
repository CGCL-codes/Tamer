package pub.db;

import pub.utils.Log;

public class LoggedConnection implements java.sql.Connection, ConnectionHolderI {

    private java.sql.Connection conn;

    public LoggedConnection(java.sql.Connection conn) {
        this.conn = conn;
    }

    /**
     * Returns the underlying java.sql.Connection class that this Logger wraps around.
     */
    public java.sql.Connection getHeldConnection() {
        return this.conn;
    }

    public java.sql.Statement createStatement() throws java.sql.SQLException {
        return new LoggedStatement(this.conn.createStatement());
    }

    public java.sql.PreparedStatement prepareStatement(java.lang.String stmt) throws java.sql.SQLException {
        Log.getLogger(this.getClass()).debug(stmt);
        return new LoggedPreparedStatement(this.conn.prepareStatement(stmt));
    }

    public java.sql.Statement createStatement(int arg0, int arg1) throws java.sql.SQLException {
        return new LoggedStatement(this.conn.createStatement(arg0, arg1));
    }

    public java.sql.Statement createStatement(int arg0, int arg1, int arg2) throws java.sql.SQLException {
        return new LoggedStatement(this.conn.createStatement(arg0, arg1, arg2));
    }

    public java.sql.PreparedStatement prepareStatement(java.lang.String arg0, int arg1, int arg2) throws java.sql.SQLException {
        return new LoggedPreparedStatement(this.conn.prepareStatement(arg0, arg1, arg2));
    }

    public java.sql.PreparedStatement prepareStatement(java.lang.String arg0, int arg1, int arg2, int arg3) throws java.sql.SQLException {
        return new LoggedPreparedStatement(this.conn.prepareStatement(arg0, arg1, arg2, arg3));
    }

    public java.sql.PreparedStatement prepareStatement(java.lang.String arg0, int arg1) throws java.sql.SQLException {
        return new LoggedPreparedStatement(this.conn.prepareStatement(arg0, arg1));
    }

    public java.sql.PreparedStatement prepareStatement(java.lang.String arg0, int[] arg1) throws java.sql.SQLException {
        return new LoggedPreparedStatement(this.conn.prepareStatement(arg0, arg1));
    }

    public java.sql.PreparedStatement prepareStatement(java.lang.String arg0, java.lang.String[] arg1) throws java.sql.SQLException {
        return new LoggedPreparedStatement(this.conn.prepareStatement(arg0, arg1));
    }

    public void setReadOnly(boolean arg0) throws java.sql.SQLException {
        this.conn.setReadOnly(arg0);
    }

    public boolean isReadOnly() throws java.sql.SQLException {
        return this.conn.isReadOnly();
    }

    public void close() throws java.sql.SQLException {
        this.conn.close();
    }

    public boolean isClosed() throws java.sql.SQLException {
        return this.conn.isClosed();
    }

    public java.sql.CallableStatement prepareCall(java.lang.String arg0) throws java.sql.SQLException {
        return this.conn.prepareCall(arg0);
    }

    public java.sql.CallableStatement prepareCall(java.lang.String arg0, int arg1, int arg2) throws java.sql.SQLException {
        return this.conn.prepareCall(arg0, arg1, arg2);
    }

    public java.sql.CallableStatement prepareCall(java.lang.String arg0, int arg1, int arg2, int arg3) throws java.sql.SQLException {
        return this.conn.prepareCall(arg0, arg1, arg2, arg3);
    }

    public java.lang.String nativeSQL(java.lang.String arg0) throws java.sql.SQLException {
        return this.conn.nativeSQL(arg0);
    }

    public void setAutoCommit(boolean arg0) throws java.sql.SQLException {
        this.conn.setAutoCommit(arg0);
    }

    public boolean getAutoCommit() throws java.sql.SQLException {
        return this.conn.getAutoCommit();
    }

    public void commit() throws java.sql.SQLException {
        this.conn.commit();
    }

    public void rollback() throws java.sql.SQLException {
        this.conn.rollback();
    }

    public void rollback(java.sql.Savepoint arg0) throws java.sql.SQLException {
        this.conn.rollback(arg0);
    }

    public java.sql.DatabaseMetaData getMetaData() throws java.sql.SQLException {
        return this.conn.getMetaData();
    }

    public void setCatalog(java.lang.String arg0) throws java.sql.SQLException {
        this.conn.setCatalog(arg0);
    }

    public java.lang.String getCatalog() throws java.sql.SQLException {
        return this.conn.getCatalog();
    }

    public void setTransactionIsolation(int arg0) throws java.sql.SQLException {
        this.conn.setTransactionIsolation(arg0);
    }

    public int getTransactionIsolation() throws java.sql.SQLException {
        return this.conn.getTransactionIsolation();
    }

    public java.sql.SQLWarning getWarnings() throws java.sql.SQLException {
        return this.conn.getWarnings();
    }

    public void clearWarnings() throws java.sql.SQLException {
        this.conn.clearWarnings();
    }

    public java.util.Map getTypeMap() throws java.sql.SQLException {
        return this.conn.getTypeMap();
    }

    public void setTypeMap(java.util.Map arg0) throws java.sql.SQLException {
        this.conn.setTypeMap(arg0);
    }

    public void setHoldability(int arg0) throws java.sql.SQLException {
        this.conn.setHoldability(arg0);
    }

    public int getHoldability() throws java.sql.SQLException {
        return this.conn.getHoldability();
    }

    public java.sql.Savepoint setSavepoint() throws java.sql.SQLException {
        return this.conn.setSavepoint();
    }

    public java.sql.Savepoint setSavepoint(java.lang.String arg0) throws java.sql.SQLException {
        return this.conn.setSavepoint(arg0);
    }

    public void releaseSavepoint(java.sql.Savepoint arg0) throws java.sql.SQLException {
        this.conn.releaseSavepoint(arg0);
    }
}
