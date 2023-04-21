package com.lovebridge;

import net.sf.persist.Persist;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 */
public class SystemManager {

    private static String jdbcUrl;

    private static String userName;

    private static String password;

    private static Connection connection;

    private static Persist persist;

    private static long lastRenewtime;

    private static final long RENEW_TIME = 1000 * 60 * 10;

    public static void initJDBC(String driver, String jdbcUrl, String userName, String password) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Can't init jdbc driver:" + driver, e);
        }
        SystemManager.jdbcUrl = jdbcUrl;
        SystemManager.userName = userName;
        SystemManager.password = password;
    }

    public static Persist getPersist() {
        if (System.currentTimeMillis() - lastRenewtime > RENEW_TIME || connection == null) {
            renew();
        }
        return persist;
    }

    public static void renew() {
        try {
            if (connection != null) {
                connection.close();
            }
            connection = DriverManager.getConnection(jdbcUrl, userName, password);
        } catch (SQLException e) {
            connection = null;
            throw new RuntimeException("Can't connection to DB:" + jdbcUrl, e);
        }
        persist = new Persist(connection);
        lastRenewtime = System.currentTimeMillis();
    }
}
