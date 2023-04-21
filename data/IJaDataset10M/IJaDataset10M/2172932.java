package mecca.db;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * @author Shamsul Bahrin bin Abd Mutalib
 *
 * @version 0.1
 */
public class Database {

    public Connection conn = null;

    public Connection getConnection() throws Exception {
        return conn;
    }

    public Database(String driver, String url, String user, String passw) throws Exception {
        open(driver, url, user, passw);
    }

    public Statement getStatement() throws Exception {
        return conn.createStatement();
    }

    public void open(String driver, String url, String user, String passw) throws Exception {
        Driver d = (Driver) Class.forName(driver).newInstance();
        conn = DriverManager.getConnection(url, user, passw);
    }

    public void close() {
        try {
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
