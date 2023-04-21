package fh.bfi.pit.ebanking.application;

import fh.bfi.pit.ebanking.model.User;
import java.sql.*;

/**
 *
 * @author Werner Hoelzl
 */
public class Configuration {

    private static Configuration myInstance;

    static String dbConn;

    private User currentUser;

    private static boolean isConnected = false;

    private Connection dbConnection = null;

    public static Configuration getInstance() {
        if (myInstance == null) {
            myInstance = new Configuration();
        }
        return myInstance;
    }

    public Connection getDBConn() {
        connectToDB();
        return dbConnection;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void connectToDB() {
        String sDbDrv = "com.mysql.jdbc.Driver";
        String sDbUrl = "jdbc:mysql://localhost:3306/ebanking";
        String sUsr = "root";
        String sPwd = "LarojitaS1";
        if (!isConnected) {
            try {
                Class.forName(sDbDrv);
                dbConnection = DriverManager.getConnection(sDbUrl, sUsr, sPwd);
                isConnected = true;
            } catch (Exception e) {
                System.out.println(e);
                isConnected = false;
            }
        }
    }
}
