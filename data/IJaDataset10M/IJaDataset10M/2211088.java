package jreceiver.server.db;

import java.sql.*;
import jreceiver.server.util.db.*;

/**
 * settings table routines for JReceiver database
 *
 * @author Reed Esau
 * @version $Revision: 1.4 $ $Date: 2002/07/20 03:04:56 $
 */
public class SettingDB extends BaseDB {

    /**
    * this class is implemented as a singleton
    */
    private static SettingDB singleton;

    /**
     * obtain an instance of this singleton
     * <p>
     * Note that this uses the questionable DCL pattern (search on
     * DoubleCheckedLockingIsBroken for more info)
     * <p>
     * @return the singleton instance for this JVM
     */
    public static SettingDB getInstance() {
        if (singleton == null) {
            synchronized (SettingDB.class) {
                if (singleton == null) singleton = new SettingDB();
            }
        }
        return singleton;
    }

    /**
     * access to the settings table
     */
    protected String get(Connection conn, String user_id, String key, String defaultValue) throws DatabaseException {
        if (conn == null || user_id == null || key == null) throw new IllegalArgumentException();
        final String sql = "SELECT value FROM setting WHERE user_id=? AND id=?";
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, user_id);
            stmt.setString(2, key);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) return defaultValue;
            return rs.getString(1);
        } catch (SQLException e) {
            throw new DatabaseException("unable to get setting value for user_id=" + user_id + " key=" + key, e);
        } finally {
            HelperDB.safeClose(stmt);
        }
    }

    /**
     * access to the settings table and return an int
     */
    protected int getInt(Connection conn, String user_id, String key, int defaultValue) throws DatabaseException {
        try {
            String sDefaultValue = Integer.toString(defaultValue);
            return Integer.parseInt(get(conn, user_id, key, sDefaultValue));
        } catch (NumberFormatException e) {
            throw new DatabaseException("cannot get int setting for user_id=" + user_id + " key=" + key, e);
        }
    }

    /**
     * access to the settings table and return an boolean
     */
    protected boolean getBool(Connection conn, String user_id, String key, boolean defaultValue) throws DatabaseException {
        try {
            String sDefaultValue = defaultValue ? "1" : "0";
            return Integer.parseInt(get(conn, user_id, key, sDefaultValue)) != 0;
        } catch (NumberFormatException e) {
            throw new DatabaseException("cannot get boolean setting for user_id=" + user_id + " key=" + key, e);
        }
    }

    /**
     * insert or update a settings value
     */
    protected void put(Connection conn, String user_id, String key, String value) throws DatabaseException {
        if (conn == null || user_id == null || key == null) throw new IllegalArgumentException();
        if (value == null) delete(conn, user_id, key);
        final String sql_upd = "UPDATE setting SET value=? WHERE user_id=? AND id=?";
        final String sql_ins = "INSERT INTO setting (value, user_id, id) VALUES (?,?,?)";
        String canonized_value = value.trim();
        PreparedStatement stmtUpd = null;
        PreparedStatement stmtIns = null;
        try {
            stmtUpd = conn.prepareStatement(sql_upd);
            stmtUpd.setString(1, canonized_value);
            stmtUpd.setString(2, user_id);
            stmtUpd.setString(3, key);
            int row_count = stmtUpd.executeUpdate();
            if (row_count == 0) {
                stmtIns = conn.prepareStatement(sql_ins);
                stmtIns.setString(1, canonized_value);
                stmtIns.setString(2, user_id);
                stmtIns.setString(3, key);
                stmtIns.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DatabaseException("unable to insert setting record", e);
        } finally {
            HelperDB.safeClose(stmtUpd);
            HelperDB.safeClose(stmtIns);
        }
    }

    /**
     * remove a settings value
     */
    protected void delete(Connection conn, String user_id, String key) throws DatabaseException {
        if (conn == null || user_id == null || key == null) throw new IllegalArgumentException();
        String sql = "DELETE FROM setting WHERE user_id=? AND id=?";
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, user_id);
            stmt.setString(2, key);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("unable to delete setting record", e);
        } finally {
            HelperDB.safeClose(stmt);
        }
    }

    /**
     * access to the settings table
     */
    protected String get(Connection conn, String user_id, String key) throws DatabaseException {
        return get(conn, user_id, key, null);
    }

    /**
     * access to the settings table and return an int
     */
    protected int getInt(Connection conn, String user_id, String key) throws DatabaseException {
        return getInt(conn, user_id, key, 0);
    }

    /**
     * access to the settings table and return an boolean
     */
    protected boolean getBool(Connection conn, String user_id, String key) throws DatabaseException {
        return getBool(conn, user_id, key, false);
    }

    /**
     * insert or update an integer settings value
     */
    protected void put(Connection conn, String user_id, String key, int value) throws DatabaseException {
        put(conn, user_id, key, Integer.toString(value));
    }

    /**
     * insert or update an boolean settings value
     */
    protected void put(Connection conn, String user_id, String key, boolean value) throws DatabaseException {
        put(conn, user_id, key, value ? "1" : "0");
    }

    /**
     * access to the settings table
     */
    public String get(String user_id, String key) throws DatabaseException {
        return get(user_id, key, null);
    }

    /**
     * access to the settings table and return an int
     */
    public int getInt(String user_id, String key) throws DatabaseException {
        return getInt(user_id, key, 0);
    }

    /**
     * access to the settings table and return an boolean
     */
    public boolean getBool(String user_id, String key) throws DatabaseException {
        return getBool(user_id, key, false);
    }

    /**
     * insert or update an integer settings value
     */
    public void put(String user_id, String key, int value) throws DatabaseException {
        put(user_id, key, Integer.toString(value));
    }

    /**
     * insert or update an boolean settings value
     */
    public void put(String user_id, String key, boolean value) throws DatabaseException {
        put(user_id, key, value ? "1" : "0");
    }

    /**
     * insert or update a settings value
     */
    public void put(String user_id, String key, String value) throws DatabaseException {
        ConnectionPool pool = null;
        Connection conn = null;
        try {
            pool = ConnectionPool.getInstance();
            conn = pool.getConnection();
            put(conn, user_id, key, value);
        } finally {
            if (pool != null) pool.releaseConnection(conn);
        }
    }

    /**
     * remove a settings value
     */
    public void delete(String user_id, String key) throws DatabaseException {
        ConnectionPool pool = null;
        Connection conn = null;
        try {
            pool = ConnectionPool.getInstance();
            conn = pool.getConnection();
            delete(conn, user_id, key);
        } finally {
            if (pool != null) pool.releaseConnection(conn);
        }
    }

    /**
     * access to the settings table
     */
    public String get(String user_id, String key, String defaultValue) throws DatabaseException {
        ConnectionPool pool = null;
        Connection conn = null;
        try {
            pool = ConnectionPool.getInstance();
            conn = pool.getConnection();
            return get(conn, user_id, key, defaultValue);
        } finally {
            if (pool != null) pool.releaseConnection(conn);
        }
    }

    /**
     * access to the settings table
     */
    public int getInt(String user_id, String key, int defaultValue) throws DatabaseException {
        ConnectionPool pool = null;
        Connection conn = null;
        try {
            pool = ConnectionPool.getInstance();
            conn = pool.getConnection();
            return getInt(conn, user_id, key, defaultValue);
        } finally {
            if (pool != null) pool.releaseConnection(conn);
        }
    }

    /**
     * access to the settings table
     */
    public boolean getBool(String user_id, String key, boolean defaultValue) throws DatabaseException {
        ConnectionPool pool = null;
        Connection conn = null;
        try {
            pool = ConnectionPool.getInstance();
            conn = pool.getConnection();
            return getBool(conn, user_id, key, defaultValue);
        } finally {
            if (pool != null) pool.releaseConnection(conn);
        }
    }
}
