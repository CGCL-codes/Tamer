package org.jwebsocket.plugins.jdbc;

import org.jwebsocket.logging.Logging;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

/**
 * Database connection maintenance.
 * This class handles the different connections to one or more databases.
 * Here also different users with different rights are handled.
 * @since 1.0
 * @author Alexander Schulze
 */
public class DBConnectSingleton {

    private static Logger mLog = Logging.getLogger(DBConnectSingleton.class);

    private static Map<String, Connection> mConnections = new FastMap<String, Connection>();

    private static String mLastRecentException = null;

    /**
	 * Name of the database user for system access
	 * @since 1.0
	 */
    public static final String USR_SYSTEM = "SYS";

    /**
	 * Name of the database user for usual application access
	 * @since 1.0
	 */
    public static final String USR_APPLICATION = "APP";

    /**
	 * Name of the database user for demo access only
	 * @since 1.0
	 */
    public static final String USR_DEMO = "DEMO";

    public static String DB_DRIVER = "com.mysql.jdbc.Driver";

    public static String DB_URL = "jdbc:mysql://localhost:3306/ria-db";

    public static String DB_SYS_USER_ID = "fffSys";

    public static String DB_SYS_USER_PW = "sys_password";

    public static String DB_DEMO_USER_ID = "fffDemo";

    public static String DB_DEMO_USER_PW = "demo_password";

    public static String DB_APP_USER_ID = "fffApp";

    public static String DB_APP_USER_PW = "app_password";

    /**
	 * Returns a connection for the given database user. If a connection not
	 * already has been established or is broken a new one is created.
	 * @param aUsr Name of the database user
	 * @since 1.0
	 */
    private static Connection getConnection(String aUsr, Boolean aAutoConnect) {
        String lUsername;
        String lPassword;
        if (aUsr.equals(USR_SYSTEM)) {
            lUsername = DB_SYS_USER_ID;
            lPassword = DB_SYS_USER_PW;
        } else if (aUsr.equals(USR_DEMO)) {
            lUsername = DB_DEMO_USER_ID;
            lPassword = DB_DEMO_USER_PW;
        } else if (aUsr.equals(USR_APPLICATION)) {
            lUsername = DB_APP_USER_ID;
            lPassword = DB_APP_USER_PW;
        } else {
            return null;
        }
        Connection lConnection = (Connection) mConnections.get(aUsr);
        if (aAutoConnect) {
            try {
                if (lConnection == null || lConnection.isClosed() || !lConnection.isValid(300)) {
                    try {
                        if (mLog.isDebugEnabled()) {
                            mLog.debug("Connecting '" + aUsr + "' to database...");
                        }
                        Class.forName(DB_DRIVER);
                        lConnection = DriverManager.getConnection(DB_URL, lUsername, lPassword);
                        if (mLog.isInfoEnabled()) {
                            mLog.info("'" + aUsr + "' successfully connected to database.");
                        }
                        mConnections.put(aUsr, lConnection);
                        return lConnection;
                    } catch (SQLException lEx) {
                        mLastRecentException = lEx.getClass().getSimpleName() + " " + " connecting '" + aUsr + "', to database" + " details: " + lEx.getSQLState();
                        mLog.error(mLastRecentException);
                    }
                }
            } catch (Exception lEx) {
                mLastRecentException = lEx.getClass().getSimpleName() + " " + " on database connection: " + lEx.getMessage();
                mLog.error(mLastRecentException);
            }
        }
        return lConnection;
    }

    public static Connection checkConnection(String aUsr) {
        return getConnection(aUsr, false);
    }

    public static Connection getConnection(String aUsr) {
        return getConnection(aUsr, true);
    }

    /**
	 * Closes the connection for the given database user. If a connection not
	 * already has been established or is broken nothing happens.
	 * @param aUsr Name of the database user
	 * @since 1.0
	 */
    public static void closeConnection(String aUsr) {
        try {
            if (mLog.isDebugEnabled()) {
                mLog.debug("Disconnecting '" + aUsr + "' from database...");
            }
            Connection lConnection = (Connection) mConnections.get(aUsr);
            if (lConnection != null && !lConnection.isClosed()) {
                lConnection.close();
                if (mLog.isInfoEnabled()) {
                    mLog.info("'" + aUsr + "' successfully disconnected from database.");
                }
            } else {
                if (mLog.isInfoEnabled()) {
                    mLog.info("'" + aUsr + "' not connected to database.");
                }
            }
        } catch (SQLException lEx) {
            mLastRecentException = lEx.getClass().getName() + " disconnecting '" + aUsr + "' from database, details: " + lEx.getMessage();
            mLog.error(mLastRecentException);
        }
    }

    /**
	 * Performs a update command for the given database user.
	 * @param aUsr Name of the database user
	 * @param aSQL SQL statement to be performed on the database.
	 * @since 1.0
	 */
    public static int execSQL(String aUsr, String aSQL) throws Exception {
        Connection lConn = DBConnectSingleton.getConnection(aUsr);
        if (lConn != null) {
            Statement lSQL = lConn.createStatement();
            return lSQL.executeUpdate(aSQL);
        }
        return -1;
    }

    /**
	 * Performs a prepared update command with optional arguments 
	 * for the given database user.
	 * @param aUsr Name of the database user
	 * @param aSQL SQL statement to be performed on the database.
	 * @param aParms Array of additional arguments to the prepared statement.
	 * @since 1.0
	 */
    public static int execSQL(String aUsr, String aSQL, Object[] aParms) throws SQLException {
        Connection lConn = DBConnectSingleton.getConnection(aUsr);
        if (lConn != null) {
            PreparedStatement lSQL = lConn.prepareStatement(aSQL);
            int lParmIdx = 0;
            while (lParmIdx < aParms.length) {
                Object lParm = aParms[lParmIdx];
                lParmIdx++;
                lSQL.setObject(lParmIdx, lParm);
            }
            return lSQL.executeUpdate();
        }
        return -1;
    }

    /**
	 * Performs a query command for the given database user.
	 * @param aUsr Name of the database user
	 * @param aSQL SQL statement to be performed on the database.
	 * @since 1.0
	 */
    public static DBQueryResult querySQL(String aUsr, String aSQL) throws Exception {
        Connection lConn = DBConnectSingleton.getConnection(aUsr);
        DBQueryResult lQRes = new DBQueryResult();
        if (lConn != null) {
            lQRes.sql = lConn.createStatement();
            lQRes.resultSet = lQRes.sql.executeQuery(aSQL);
            lQRes.metaData = lQRes.resultSet.getMetaData();
        }
        return lQRes;
    }

    public static void closeQuery(DBQueryResult aQueryRes) {
        try {
            if (aQueryRes != null && aQueryRes.sql != null) {
                aQueryRes.sql.close();
            }
        } catch (Exception lEx) {
            mLog.error(lEx.getClass().getSimpleName() + " closing query : " + lEx.getMessage());
        }
    }
}
