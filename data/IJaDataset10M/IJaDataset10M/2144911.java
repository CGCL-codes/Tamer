package com.programiraj.database.postgresql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.programiraj.database.DatabaseConnector;

public class PostgresqlDatabaseConnector implements DatabaseConnector {

    public Connection connect(String url, String username, String password) throws SQLException {
        Connection conn = null;
        try {
            Class.forName("org.h2.Driver");
            conn = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            System.err.println("System error, MySQL driver not found");
        }
        return conn;
    }
}
