package org.doot.conf.drivers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.doot.conf.configurator;

/**
 *
 * @author tapan
 */
public abstract class MySQL {

    private static String hostname;

    private static String username;

    private static String password;

    private static String database;

    private static String url;

    private static Connection con;

    /**
     * Connects to the mysql database
     * @param conf A loaded instance of the configurator class
     * @return The connection to the database
     */
    public static Connection getConnection(configurator conf) {
        try {
            hostname = conf.getHost();
            username = conf.getUser();
            password = conf.getPass();
            database = conf.getDBName();
            url = "jdbc:mysql://" + hostname + ":3306/" + database;
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return con;
        }
    }
}
