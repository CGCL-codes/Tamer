package com.joebertj.helper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DatabaseConnection {

    private static Connection conn = null;

    private DatabaseConnection() {
    }

    public static Connection getConnection() {
        if (conn == null) {
            Properties props = new Properties();
            try {
                props.load(DatabaseConnection.class.getResourceAsStream("/application.properties"));
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            String driver = props.getProperty("driver");
            String host = props.getProperty("host");
            String database = props.getProperty("database");
            String user = props.getProperty("user");
            String password = props.getProperty("password");
            try {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database + "?useUnicode=true&characterEncoding=UTF-8&user=" + user + "&password=" + password);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return conn;
    }
}
