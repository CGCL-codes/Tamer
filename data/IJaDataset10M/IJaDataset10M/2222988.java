package com.spoledge.audao.test.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.spoledge.audao.db.dao.ConnectionProvider;

public class JdbcUtil {

    private String url;

    private String login;

    private String password;

    private Connection connection;

    protected ConnectionProvider connectionProvider;

    public JdbcUtil() {
        String driver = System.getProperty("jdbc.driver");
        url = System.getProperty("jdbc.url");
        login = System.getProperty("jdbc.username");
        password = System.getProperty("jdbc.password");
        try {
            Class.forName(driver);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        connectionProvider = new ConnectionProvider() {

            public Connection getConnection() {
                if (connection == null) {
                    try {
                        connection = DriverManager.getConnection(url, login, password);
                        connection.setAutoCommit(false);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                return connection;
            }
        };
    }

    public void setUp() {
    }

    public void tearDown() {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            connection = null;
        }
    }

    public ConnectionProvider getConnectionProvider() {
        return connectionProvider;
    }
}
