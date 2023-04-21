package server;

import java.sql.*;

public class DBDemolish {

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url;
            Connection con;
            Statement stmt;
            url = "jdbc:mysql://localhost:3306/TAUStrategoDB";
            con = DriverManager.getConnection(url, "TAUStratego", "drowssap");
            stmt = con.createStatement();
            stmt.executeUpdate("DROP TABLE games");
            stmt.executeUpdate("DROP TABLE users");
            con.close();
            url = "jdbc:mysql://localhost:3306/mysql";
            con = DriverManager.getConnection(url, "root", "");
            stmt = con.createStatement();
            stmt.executeUpdate("REVOKE ALL PRIVILEGES ON *.* FROM 'TAUStratego'@'localhost'");
            stmt.executeUpdate("REVOKE GRANT OPTION ON *.* FROM 'TAUStratego'@'localhost'");
            stmt.executeUpdate("DELETE FROM mysql.user WHERE User='TAUStratego' and Host='localhost'");
            stmt.executeUpdate("FLUSH PRIVILEGES");
            stmt.executeUpdate("DROP DATABASE TAUStrategoDB");
            con.close();
            System.out.println("TAUStratego DB demolished successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
