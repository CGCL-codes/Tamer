package org.javacoding.upupa.servlet.euringexport.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Used to get a DB connection.
 * @author MC
 */
public class DBConnectionHandler {

    private static Connection connection;

    private DBConnectionHandler() {
    }

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                String url = PropertyParser.DB_URL;
                String username = PropertyParser.DB_USERNAME;
                String password = PropertyParser.DB_PASSWORD;
                connection = DriverManager.getConnection(url, username, password);
            } catch (final ClassNotFoundException cnfE) {
                cnfE.printStackTrace();
            } catch (final SQLException sqlE) {
                sqlE.printStackTrace();
            }
        }
        return connection;
    }
}
