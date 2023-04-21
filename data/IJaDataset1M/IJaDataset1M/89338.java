package org.auroraide.server.database;

import java.sql.*;

public class DAO {

    private static DAO instance = null;

    Connection conn;

    private DAO() {
    }

    protected void finalize() {
        try {
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized DAO getDAO() throws SQLException {
        if (instance == null) {
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
            } catch (Exception E) {
                System.err.println("Unable to load driver.");
                E.printStackTrace();
            }
            instance = new DAO();
            instance.conn = DriverManager.getConnection("jdbc:mysql://localhost/aurora?user=root&password=19841028");
        }
        return instance;
    }

    public int insertPackage(String name) {
        try {
            Statement stmt = conn.createStatement();
            stmt.execute("INSERT INTO Package (name) VALUES (\"" + name + "\")");
            stmt.close();
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState:     " + e.getSQLState());
            System.out.println("VendorError:  " + e.getErrorCode());
        }
        return -1;
    }
}
