package druid.core.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Vector;
import druid.core.jdbc.entities.DatabaseEntity;
import druid.core.jdbc.manager.JdbcManager;
import druid.core.modules.ModuleManager;
import druid.interfaces.SqlAdapter;
import druid.util.DefaultLogger;

public class JdbcConnection {

    protected Connection sqlConn;

    private DatabaseMetaData dbMetaData;

    private DatabaseEntity dbEntity;

    private int iMaxRows = 5000;

    private DefaultLogger logger;

    private SqlAdapter sqlAdapter;

    public JdbcConnection() {
    }

    public boolean connect(String url, String user, String password, boolean autoCommit) throws SQLException {
        sqlConn = null;
        dbMetaData = null;
        logger = new DefaultLogger();
        sqlConn = JdbcManager.connect(url, user, password);
        if (sqlConn == null) return false;
        dbMetaData = sqlConn.getMetaData();
        sqlConn.setAutoCommit(autoCommit);
        tryTransactionIsolation();
        dbEntity = new DatabaseEntity(this);
        boolean found = false;
        for (Enumeration e = ModuleManager.getModules(SqlAdapter.class); e.hasMoreElements(); ) {
            SqlAdapter mod = (SqlAdapter) e.nextElement();
            if (url.indexOf(mod.getMatchString()) != -1) {
                sqlAdapter = mod;
                found = true;
                break;
            }
        }
        if (!found) sqlAdapter = new DefaultSqlAdapter();
        sqlAdapter.initializeConnection(this);
        logger.log("Using '" + sqlAdapter.getMatchString() + "' sql adapter V " + sqlAdapter.getVersion());
        return true;
    }

    /**
		* After calling this method, this JdbcConnection instance should not
		* be referenced again.
		*
		* @param timeout how many millis to wait
		*
		* @return true on success, false if the timeout ended and the thread was stopped
		*/
    public boolean disconnect(int timeout) {
        Runnable run = new Runnable() {

            public void run() {
                try {
                    sqlConn.close();
                } catch (SQLException e) {
                }
            }
        };
        Thread thread = new Thread(run);
        thread.start();
        try {
            thread.join(timeout);
        } catch (InterruptedException e) {
        }
        sqlConn = null;
        dbMetaData = null;
        dbEntity = null;
        return !thread.isAlive();
    }

    public boolean isConnected() {
        return (sqlConn != null);
    }

    public DatabaseMetaData getMetaData() {
        return dbMetaData;
    }

    public Connection getConnection() {
        return sqlConn;
    }

    public DatabaseEntity getDatabaseEntity() {
        return dbEntity;
    }

    public int getMaxRows() {
        return iMaxRows;
    }

    public String getLog() {
        return logger.getLog();
    }

    public SqlAdapter getSqlAdapter() {
        return sqlAdapter;
    }

    public void setMaxRows(int maxRows) {
        iMaxRows = maxRows;
    }

    public RecordList retrieveResultSet(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        RecordList records = new RecordList();
        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
            String header = getColumnName(rsmd, i);
            int size = getColumnDisplaySize(rsmd, i);
            records.addColumn(header, size);
        }
        Object field;
        while (rs.next()) {
            records.newRecord();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                field = null;
                if (rsmd.getColumnType(i) != java.sql.Types.OTHER) {
                    try {
                        field = rs.getObject(i);
                    } catch (Throwable e) {
                    }
                }
                records.addToRecord(field);
            }
        }
        Statement st = rs.getStatement();
        rs.close();
        if (st != null) st.close();
        return records;
    }

    private String getColumnName(ResultSetMetaData rsmd, int i) {
        try {
            return rsmd.getColumnName(i);
        } catch (Exception e) {
            return "???";
        }
    }

    private int getColumnDisplaySize(ResultSetMetaData rsmd, int i) {
        try {
            return rsmd.getColumnDisplaySize(i);
        } catch (Exception e) {
            return 20;
        }
    }

    public ResultSet selectUpdate(String query) throws SQLException {
        Statement stmt = tryCreateStatement(sqlConn);
        trySetMaxFieldSize(stmt, 0);
        stmt.setMaxRows(iMaxRows);
        stmt.setEscapeProcessing(false);
        return stmt.executeQuery(query);
    }

    public ResultSet select(String query, Vector args) throws SQLException, IOException {
        PreparedStatement stmt = sqlConn.prepareStatement(query);
        trySetMaxFieldSize(stmt, 0);
        stmt.setMaxRows(0);
        if (args != null) for (int i = 0; i < args.size(); i++) setParam(stmt, i, args.elementAt(i));
        return stmt.executeQuery();
    }

    public int execute(String query, Vector args) throws SQLException, IOException {
        PreparedStatement stmt = sqlConn.prepareStatement(query);
        if (args != null) for (int i = 0; i < args.size(); i++) setParam(stmt, i, args.elementAt(i));
        try {
            return stmt.executeUpdate();
        } finally {
            stmt.close();
        }
    }

    private void setParam(PreparedStatement stmt, int i, Object o) throws SQLException, IOException {
        if (o instanceof InputStream) {
            InputStream is = (InputStream) o;
            stmt.setBinaryStream(i + 1, is, is.available());
        } else if (o instanceof StringBuffer) {
            String s = o.toString();
            stmt.setCharacterStream(i + 1, new StringReader(s), s.length());
        } else {
            stmt.setObject(i + 1, o);
        }
    }

    private Statement tryCreateStatement(Connection conn) throws SQLException {
        try {
            return conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch (SQLException e) {
            return conn.createStatement();
        }
    }

    private void trySetMaxFieldSize(Statement stmt, int value) {
        try {
            stmt.setMaxFieldSize(value);
        } catch (SQLException e) {
        }
    }

    private void tryTransactionIsolation() {
        String sLevel;
        int iLevel;
        try {
            if (dbMetaData.supportsTransactionIsolationLevel(Connection.TRANSACTION_SERIALIZABLE)) {
                sLevel = "SERIALIZABLE (best)";
                iLevel = Connection.TRANSACTION_SERIALIZABLE;
            } else if (dbMetaData.supportsTransactionIsolationLevel(Connection.TRANSACTION_REPEATABLE_READ)) {
                sLevel = "REPEATABLE_READ (very good)";
                iLevel = Connection.TRANSACTION_REPEATABLE_READ;
            } else if (dbMetaData.supportsTransactionIsolationLevel(Connection.TRANSACTION_READ_COMMITTED)) {
                sLevel = "READ_COMMITTED (good)";
                iLevel = Connection.TRANSACTION_READ_COMMITTED;
            } else if (dbMetaData.supportsTransactionIsolationLevel(Connection.TRANSACTION_READ_UNCOMMITTED)) {
                sLevel = "READ_UNCOMMITTED (poor)";
                iLevel = Connection.TRANSACTION_READ_UNCOMMITTED;
            } else {
                sLevel = "NONE (no transactions)";
                iLevel = Connection.TRANSACTION_NONE;
            }
        } catch (SQLException e) {
            logger.log("Cannot check transaction isolation level");
            return;
        }
        logger.log("Transaction isolation found : " + sLevel);
        try {
            sqlConn.setTransactionIsolation(iLevel);
            logger.log("Transaction isolation set");
        } catch (SQLException e) {
            logger.log("Cannot set transaction isolation. Ignored");
        }
    }
}
