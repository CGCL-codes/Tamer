package org.dbe.kb.rdb;

import java.sql.*;
import java.io.*;

/**
 *
 * <p>Database Access Object</p>
 *
 * <p>This is the DB object responsible for multi-threaded database access functionality </p>
 *
 * <p>TUC/MUSIC 2004</p>
 *
 */
public class DBObject {

    public static Connection[] _dbActiveConnections = null;

    public static Thread[] _dbActiveThreads = null;

    public static String _process = "DB";

    public Connection _dbConnection;

    private Statement _stat;

    private PreparedStatement _pstat;

    private ResultSet _res;

    private int _maxRows;

    static final int CONNECTIONS_THRESHOLD = 100;

    public DBObject() {
        _stat = null;
        _pstat = null;
        _res = null;
        _maxRows = 0;
        String tname = Thread.currentThread().getName();
        System.out.println("New DBObject for thread '" + tname + "' pAddr=" + Thread.currentThread());
        int n;
        if (!tname.startsWith(_process)) {
            System.out.println("Using DBObject without active connection for this thread.. Trying to connect");
            try {
                DBObject.newConnection();
            } catch (Exception ex1) {
                ex1.printStackTrace();
            }
        }
        _dbConnection = _dbActiveConnections[Integer.parseInt(Thread.currentThread().getName().substring(_process.length()))];
    }

    public static void checkOpenConnections() throws Exception {
        if (_dbActiveConnections.length > CONNECTIONS_THRESHOLD) {
            int active = 0;
            System.out.println("DB Connections above threshold .. Searching for unnecessary open connections...");
            for (int i = 0; i < _dbActiveThreads.length; i++) if (_dbActiveThreads[i].isAlive()) active++;
            System.out.println("Found " + (_dbActiveThreads.length - active) + " unnecessary open connections...");
            if (active < _dbActiveThreads.length) {
                Connection[] conn = new Connection[active];
                Thread[] ts = new Thread[active];
                int j = 0;
                for (int i = 0; i < _dbActiveThreads.length; i++) {
                    if (_dbActiveThreads[i].isAlive()) {
                        ts[j] = _dbActiveThreads[i];
                        conn[j] = _dbActiveConnections[i];
                        _dbActiveThreads[i].setName(String.valueOf(j));
                        j++;
                    } else {
                        System.out.println("Closing connection " + i + " ...");
                        _dbActiveConnections[i].close();
                    }
                }
                _dbActiveConnections = conn;
                _dbActiveThreads = ts;
            }
        }
    }

    public static void newConnection() throws Exception {
        Connection newconn = null;
        String tname = Thread.currentThread().getName();
        if (tname.startsWith(_process)) {
            System.out.println("DB Connection already exists for thread '" + tname + "'");
            return;
        }
        newconn = DatabaseServer.getEmbeddedConnection(DatabaseServer.DBE_LOCAL_DB, "");
        if (_dbActiveConnections == null) {
            _dbActiveConnections = new Connection[1];
            _dbActiveThreads = new Thread[1];
        } else {
            Connection[] conns = new Connection[_dbActiveConnections.length + 1];
            for (int i = 0; i < _dbActiveConnections.length; i++) conns[i] = _dbActiveConnections[i];
            _dbActiveConnections = conns;
            Thread[] ts = new Thread[_dbActiveThreads.length + 1];
            for (int i = 0; i < _dbActiveThreads.length; i++) ts[i] = _dbActiveThreads[i];
            _dbActiveThreads = ts;
        }
        _dbActiveConnections[_dbActiveConnections.length - 1] = newconn;
        _dbActiveThreads[_dbActiveThreads.length - 1] = Thread.currentThread();
        Thread.currentThread().setName(_process + String.valueOf(_dbActiveConnections.length - 1));
        System.out.println("NEW DB Connection for thread '" + Thread.currentThread().getName() + "'");
        checkOpenConnections();
    }

    public ResultSet getResultSet() {
        return _res;
    }

    public void setMaxRows(int maxRows) throws SQLException {
        _maxRows = maxRows;
    }

    public int getMaxRows() throws SQLException {
        if (_stat != null) return _stat.getMaxRows();
        if (_pstat != null) return _pstat.getMaxRows();
        return 0;
    }

    public void close() throws SQLException {
        if (_stat != null) {
            _stat.close();
            _stat = null;
        }
        if (_pstat != null) {
            _pstat.close();
            _pstat = null;
        }
        if (_res != null) {
            _res.close();
            _res = null;
        }
    }

    public void submitUpdate(String strSQL) throws SQLException {
        String result;
        _stat = _dbConnection.createStatement();
        _stat.executeUpdate(strSQL);
        _stat.close();
        _stat = null;
    }

    public ResultSet submitQuery(String strSQL) throws SQLException {
        _stat = _dbConnection.createStatement();
        _stat.setMaxRows(_maxRows);
        _res = _stat.executeQuery(strSQL);
        return _res;
    }

    public void createNewStatement(String sql) throws SQLException {
        _pstat = _dbConnection.prepareStatement(sql);
        _pstat.setMaxRows(_maxRows);
    }

    public void executeUpdate() throws SQLException {
        System.out.println("DBO:" + _pstat.toString());
        _pstat.executeUpdate();
    }

    public ResultSet executeQuery() throws SQLException {
        _res = _pstat.executeQuery();
        return _res;
    }

    public void setInt(int index, int value) throws SQLException {
        _pstat.setInt(index, value);
    }

    public void setFloat(int index, float value) throws SQLException {
        _pstat.setFloat(index, value);
    }

    public void setString(int index, String value) throws SQLException {
        _pstat.setString(index, value);
    }

    public void setBoolean(int index, boolean value) throws SQLException {
        _pstat.setBoolean(index, value);
    }

    public void setDate(int index, long value) throws SQLException {
        _pstat.setDate(index, new Date(value));
    }

    public void setTimestamp(int index, long value) throws SQLException {
        _pstat.setTimestamp(index, new Timestamp(value));
    }

    public void setCharacterStream(int index, Reader reader, int len) throws SQLException {
        _pstat.setCharacterStream(index, reader, len);
    }

    public void setNull(int index, int type) throws SQLException {
        _pstat.setNull(index, type);
    }

    public void executeBatch(String[] stats) throws SQLException {
        _stat = _dbConnection.createStatement();
        for (int i = 0; i < stats.length; i++) _stat.addBatch(stats[i]);
        _stat.executeBatch();
        _stat.close();
        _stat = null;
    }

    public void prepareBatch() throws SQLException {
        _stat = _dbConnection.createStatement();
    }

    public void executeBatch() throws SQLException {
        _stat.executeBatch();
    }

    public void addBatch(String stat) throws SQLException {
        _stat.addBatch(stat);
    }

    public void startTransaction() {
        java.lang.System.out.println("-->START TRANSACTION");
        try {
            _dbConnection.setAutoCommit(false);
            _pstat = _dbConnection.prepareStatement("BEGIN");
            _pstat.execute();
            _pstat.close();
            _pstat = null;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void commit() {
        java.lang.System.out.println("-->COMMIT TRANSACTION");
        try {
            _dbConnection.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void rollback() {
        java.lang.System.out.println("-->ROLLBACK TRANSACTION");
        try {
            _dbConnection.rollback();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void executeFile(String fileName) throws SQLException {
        int pos;
        File f = new File(fileName);
        char[] buffer = new char[(int) f.length()];
        try {
            FileReader fr = new FileReader(fileName);
            fr.read(buffer);
            fr.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String strProc = new String(buffer);
        pos = strProc.indexOf(';');
        while (pos != -1) {
            String command = strProc.substring(0, pos + 1);
            System.out.println("|" + command.substring(0, pos) + "|");
            submitUpdate(command.substring(0, pos));
            strProc = strProc.substring(pos + 1);
            pos = strProc.indexOf(';');
        }
    }

    public void executeBuffer(String buffer) throws SQLException {
        int pos = 0;
        pos = buffer.indexOf(';');
        while (pos != -1) {
            String command = buffer.substring(0, pos + 1);
            System.out.println("|" + command.substring(0, pos) + "|");
            submitUpdate(command.substring(0, pos));
            buffer = buffer.substring(pos + 1);
            pos = buffer.indexOf(';');
        }
    }

    public static String formatString(String target, int pos, String value) {
        String strPos = "#" + pos;
        int sz;
        int spos;
        String temp;
        sz = pos / 10 + 2;
        spos = target.indexOf(strPos);
        if (spos < 0) {
            java.lang.System.out.println("formatString: Wrong paramater #" + value + " in " + target);
            return target;
        }
        if (value == null) {
            if (target.startsWith("'", spos - 1)) {
                spos--;
                sz += 2;
            }
            value = "NULL";
        }
        temp = target.substring(spos + sz);
        target = target.substring(0, spos);
        target += value + temp;
        return target;
    }

    public void insertIntoTable(String tableName, int[] values) throws SQLException {
        String strSQL = "insert into " + tableName + " values(#1);";
        String strSQL1 = " delete from " + tableName;
        submitUpdate(strSQL1);
        String strProc;
        for (int i = 0; i < values.length; i++) {
            strProc = strSQL;
            strProc = formatString(strProc, 1, String.valueOf(values[i]));
            submitUpdate(strProc);
        }
    }

    public void insertIntoTable(String tableName, ResultSet rs) throws SQLException {
        String strSQL = "insert into " + tableName + " values(#1)";
        String strSQL1 = "delete from " + tableName;
        String strProc;
        submitUpdate(strSQL1);
        try {
            while (rs.next()) {
                strProc = strSQL;
                formatString(strProc, 1, String.valueOf(rs.getInt(1)));
                submitUpdate(strProc);
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
    }
}
