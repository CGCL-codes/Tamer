package com.googlecode.project.tests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class CopyDatabase {

    public static void main(String[] args) {
        System.out.println("Copy data from one database table to another!");
        Connection conn = null;
        String url = "jdbc:derby:/DerbyDB/AssetDB";
        String driver = "org.apache.derby.jdbc.EmbeddedDriver";
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url);
            Statement st = conn.createStatement();
            System.out.println("Removing duplicate data from Requests Copy!");
            int rows = st.executeUpdate("DELETE FROM Requests_Copy");
            rows = st.executeUpdate("INSERT INTO Requests_Copy SELECT * FROM Requests");
            if (rows == 0) {
                System.out.println("Don't add any row!");
            } else {
                System.out.println(rows + " row(s)affected.");
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
