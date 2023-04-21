package com.rpc.core.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * A singleton for managing JDBC connections. This class supplies database
 * connections to the DatabaseManager class.
 * 
 * This singlton has different implementations for servers and client
 * applications.
 * 
 * @author ted stockwell
 */
public abstract class JDBCConnectionManager {

    protected static final String SQL_MODE = "set session sql_mode='NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';";

    private static JDBCConnectionManager __instance = new DesktopConnectionManager();

    static {
        Logger.info("Using the default JDBC connection manager");
    }

    public static final JDBCConnectionManager getInstance() throws SQLException {
        return __instance;
    }

    public static final void setConnectionManager(JDBCConnectionManager connectionManager) {
        __instance = connectionManager;
        Logger.info("JDBC connection manager set to " + connectionManager);
    }

    protected void setSQLMode(Connection _connection) {
        PreparedStatement sqlMode = null;
        try {
            sqlMode = _connection.prepareStatement(SQL_MODE);
            sqlMode.execute();
            sqlMode.close();
            String rpcUser = System.getProperty("rpc_username");
            if (StringUtils.isBlank(rpcUser)) {
                rpcUser = _connection.getMetaData().getUserName();
            }
            sqlMode = _connection.prepareStatement("set @rpcuser='" + rpcUser + "'");
            sqlMode.execute();
        } catch (SQLException e) {
        } finally {
            if (sqlMode != null) {
                try {
                    sqlMode.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public abstract Connection getConnection() throws SQLException;

    public abstract void closeConnection(Connection connection) throws SQLException;
}
