package br.com.senai.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseUtil {

    private Connection connection;

    private static DatabaseUtil instance;

    private DatabaseUtil() {
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            connection = DriverManager.getConnection("jdbc:hsqldb:file:database", "SA", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public static DatabaseUtil getInstance() {
        if (instance == null) {
            instance = new DatabaseUtil();
        }
        return instance;
    }
}
