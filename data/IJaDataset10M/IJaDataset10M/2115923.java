package com.jonosoft.ftpbrowser.web.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.jonosoft.ftpbrowser.web.client.FTPBrowserFatalException;

/**
 * @author Jkelling
 *
 */
public class DBConnection {

    private Connection conn = null;

    public DBConnection(String driver, String url, String user, String pass) throws FTPBrowserFatalException {
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url, user, pass);
        } catch (InstantiationException e) {
            throw new FTPBrowserFatalException("InstantiationException: " + e.getMessage());
        } catch (IllegalAccessException e) {
            throw new FTPBrowserFatalException("InstantiationException: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new FTPBrowserFatalException("InstantiationException: " + e.getMessage());
        } catch (SQLException e) {
            throw new FTPBrowserFatalException("SQLException: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return conn;
    }

    public void closeConnection() throws SQLException {
        if (conn != null) {
            conn.close();
            conn = null;
        }
    }
}
