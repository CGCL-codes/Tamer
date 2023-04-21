package at.fhj.itm10.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbAccess {

    private String dbName;

    private String dbUser;

    private String dbPass;

    private Connection conn;

    public DbAccess(String dbN, String dbU, String dbP) {
        this.dbName = dbN;
        this.dbUser = dbU;
        this.dbPass = dbP;
    }

    public String connectToDatabase() {
        String message = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost/" + dbName, dbUser, dbPass);
            message = "Verbindung zu DB: " + dbName + " erfolgreich!\n\n";
        } catch (Exception e) {
            e.printStackTrace();
            message = e.getMessage();
        }
        return message;
    }

    public ResultSet readSomeData() {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery("select * from kunde");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
}
