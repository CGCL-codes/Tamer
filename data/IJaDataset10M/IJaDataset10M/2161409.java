package com.hyd.dao.database.connection;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库连接处理帮助类
 */
public class ConnectionUtil {

    public static String getDatabaseType(Connection connection) throws SQLException {
        return connection.getMetaData().getDatabaseProductName() + " " + connection.getMetaData().getDatabaseProductVersion();
    }

    public static boolean isMySql(String dbType) throws SQLException {
        return dbType.contains("MySQL");
    }

    public static boolean isOracle(String dbType) throws SQLException {
        return dbType.contains("Oracle");
    }

    public static boolean isMySql(Connection connection) throws SQLException {
        return getDatabaseType(connection).contains("MySQL");
    }

    public static boolean isOracle(Connection connection) throws SQLException {
        return getDatabaseType(connection).contains("Oracle");
    }

    public static boolean isSequenceSupported(Connection conn) throws SQLException {
        return isOracle(conn);
    }
}
