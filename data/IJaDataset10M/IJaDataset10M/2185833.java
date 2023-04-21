package com.hs.framework.db.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import com.hs.framework.common.util.LogUtil;

/**
 * 
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: A database utility class to handle all the database stuffs
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: MyVietnam.net
 * </p>
 * 
 * @author: Minh Nguyen minhnn@MyVietnam.net
 * @author: Mai Nguyen mai.nh@MyVietnam.net
 *          ��ݿ�l�ӿ�������,��:��ݿ�l������,�Ƿ�ʹ��datasource,����statement,connection��.
 */
public final class DBUtils {

    public static final int DATABASE_UNKNOWN = 0;

    public static final int DATABASE_GENERAL = 1;

    public static final int DATABASE_NOSCROLL = 2;

    public static final int DATABASE_ORACLE = 10;

    public static final int DATABASE_SQLSERVER = 11;

    public static final int DATABASE_DB2 = 12;

    public static final int DATABASE_SYBASE = 13;

    public static final int DATABASE_IMFORMIX = 14;

    public static final int DATABASE_MYSQL = 15;

    public static final int DATABASE_POSTGRESQL = 16;

    public static final int DATABASE_HSQLDB = 17;

    public static final int DATABASE_ACCESS = 18;

    private static int databaseType = DATABASE_UNKNOWN;

    private static boolean useDatasource = false;

    private static int maxTimeToWait = 2000;

    private static int minutesBetweenRefresh = 30;

    private static DBConnectionManager connectionManager = null;

    private static DataSource dataSource = null;

    private static long lastGetConnectionTime = 0;

    private static long lastCloseAllConnectionsTime = 0;

    static {
        DBOptions option = new DBOptions();
        databaseType = option.databaseType;
        if (databaseType != DATABASE_UNKNOWN) {
            LogUtil.getLogger().info("Set DATABASE_TYPE = " + databaseType);
        }
        if (option.useDatasource) {
            useDatasource = true;
            try {
                javax.naming.Context context = new javax.naming.InitialContext();
                dataSource = (DataSource) context.lookup(option.datasourceName);
                LogUtil.getLogger().info("DBUtils : use datasource = " + option.datasourceName);
            } catch (javax.naming.NamingException e) {
                LogUtil.getLogger().error("Cannot get DataSource: datasource name = " + option.datasourceName, e);
            }
        } else {
            useDatasource = false;
            maxTimeToWait = option.maxTimeToWait;
            minutesBetweenRefresh = option.minutesBetweenRefresh;
            connectionManager = DBConnectionManager.getInstance(option);
            LogUtil.getLogger().info("DBUtils : use built-in DBConnectionManager (MAX_TIME_TO_WAIT = " + maxTimeToWait + ", MINUTES_BETWEEN_REFRESH = " + minutesBetweenRefresh + ")");
        }
    }

    private DBUtils() {
    }

    /**
	 * Use this method to get the database type. This method will automatically
	 * detect the database type. You could override this value by modifying the
	 * value in mvncore_db_DBOptions.properties
	 * 
	 * @return : the database type
	 */
    public static int getDatabaseType() {
        if (databaseType == DATABASE_UNKNOWN) {
            Connection connection = null;
            try {
                connection = DBUtils.getConnection();
                DatabaseMetaData dbmd = connection.getMetaData();
                String databaseName = dbmd.getDatabaseProductName().toLowerCase();
                if (databaseName.indexOf("oracle") != -1) {
                    databaseType = DATABASE_ORACLE;
                } else if (databaseName.indexOf("sql server") != -1) {
                    databaseType = DATABASE_SQLSERVER;
                } else if (databaseName.indexOf("mysql") != -1) {
                    databaseType = DATABASE_MYSQL;
                } else if (databaseName.indexOf("postgresql") != -1) {
                    databaseType = DATABASE_POSTGRESQL;
                } else if (databaseName.indexOf("hsql") != -1) {
                    databaseType = DATABASE_HSQLDB;
                } else {
                    databaseType = DATABASE_GENERAL;
                }
                LogUtil.getLogger().info("Auto detect DATABASE_TYPE = " + databaseType);
            } catch (Exception ex) {
                LogUtil.getLogger().error("Error when running getDatabaseType", ex);
            } finally {
                DBUtils.closeConnection(connection);
            }
        }
        return databaseType;
    }

    /**
	 * Get a connection from the connection pool. The returned connection must
	 * be closed by calling DBUtils.closeConnection()
	 * 
	 * @return : a new connection from the pool if succeed
	 * @throws SQLException :
	 *             if cannot get a connection from the pool
	 */
    public static Connection getConnection() throws SQLException {
        long now = System.currentTimeMillis();
        lastGetConnectionTime = now;
        if (now - lastCloseAllConnectionsTime > DateUtil.MINUTE * minutesBetweenRefresh) {
            boolean isBalance = closeAllConnections();
            if (isBalance == false) {
                try {
                    Thread.sleep(2000);
                    LogUtil.getLogger().debug("DBUtils: sleep 2 seconds for checked-out connections to returned and closed.");
                } catch (Exception ex) {
                }
            }
        }
        Connection conection = null;
        if (useDatasource) {
            if (dataSource != null) {
                conection = dataSource.getConnection();
            }
        } else {
            if (connectionManager != null) {
                conection = connectionManager.getConnection(maxTimeToWait);
            } else {
                LogUtil.getLogger().fatal("Assertion: DBUtils.connectionManager == null");
            }
        }
        if (conection == null) {
            throw new SQLException("DBUtils: Cannot get connection from Connection Pool.");
        }
        return conection;
    }

    /**
	 * Close all the connections that currently in the pool This method could be
	 * used to refresh the database connection
	 * 
	 * @return true if the pool is empty and balance false if the pool has
	 *         returned some connection to outside
	 */
    public static boolean closeAllConnections() {
        LogUtil.getLogger().debug("DBUtils.closeAllConnections is called.");
        boolean retValue = true;
        lastCloseAllConnectionsTime = System.currentTimeMillis();
        if (useDatasource) {
            if (dataSource != null) {
            }
        } else {
            if (connectionManager != null) {
                retValue = connectionManager.release();
            } else {
                LogUtil.getLogger().fatal("Assertion: DBUtils.connectionManager == null");
            }
        }
        return retValue;
    }

    /**
	 * Use this method to return the connection to the connection pool Do not
	 * use this method to close connection that is not from the connection pool
	 * 
	 * @param connection :
	 *            the connection that needs to be returned to the pool
	 */
    public static void closeConnection(Connection connection) {
        if (connection == null) return;
        if (useDatasource) {
            try {
                connection.close();
            } catch (SQLException e) {
                LogUtil.getLogger().error("DBUtils: Cannot close connection.", e);
            }
        } else {
            connectionManager.freeConnection(connection);
        }
    }

    /**
	 * Use this method to reset the MaxRows and FetchSize of the Statement to
	 * the default values
	 * 
	 * @param statement :
	 *            the statement that needs to be reseted
	 */
    public static void resetStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.setMaxRows(0);
            } catch (SQLException e) {
                LogUtil.getLogger().error("DBUtils: Cannot reset statement MaxRows.", e);
            }
            try {
                statement.setFetchSize(0);
            } catch (SQLException sqle) {
            }
        }
    }

    /**
	 * Use this method to close the Statement
	 * 
	 * @param statement :
	 *            the statement that needs to be closed
	 */
    public static void closeStatement(Statement statement) {
        try {
            if (statement != null) statement.close();
        } catch (SQLException e) {
            LogUtil.getLogger().error("DBUtils: Cannot close statement.", e);
        }
    }

    /**
	 * Use this method to close the ResultSet
	 * 
	 * @param rs :
	 *            the resultset that needs to be closed
	 */
    public static void closeResultSet(ResultSet rs) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            LogUtil.getLogger().error("DBUtils: Cannot close resultset.", e);
        }
    }
}
