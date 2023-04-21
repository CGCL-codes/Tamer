package languageacquisition;

import java.sql.*;

public class DBCreation {

    Connection connect = null;

    Statement stmt;

    DBCreation(String serverName, String username, String password) {
        try {
            String driverName = "com.mysql.jdbc.Driver";
            Class.forName(driverName);
            System.out.println("try to connect...;");
            connect = DriverManager.getConnection("jdbc:mysql://" + serverName, username, password);
            System.out.println("connected, create db");
            stmt = connect.createStatement();
            stmt.executeUpdate("DROP DATABASE agentDB");
            stmt.executeUpdate("CREATE DATABASE agentDB");
            stmt.executeUpdate("USE agentDB");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void createTables(int count) {
        try {
            stmt.executeUpdate("CREATE TABLE `Feature` (`Feature_ID` SMALLINT NOT NULL AUTO_INCREMENT, PRIMARY KEY(Feature_ID))");
            stmt.executeUpdate("CREATE TABLE `Symbol` (`Symbol_ID` SMALLINT NOT NULL AUTO_INCREMENT, PRIMARY KEY(Symbol_ID))");
            stmt.executeUpdate("CREATE TABLE `Correlation` (`Correlation_ID` INT NOT NULL AUTO_INCREMENT, `Feature_ID` SMALLINT NOT NULL, `Symbol_ID` SMALLINT NOT NULL, `Value` DOUBLE, PRIMARY KEY(Correlation_ID))");
            for (int i = 0; i < count; i++) {
                stmt.executeUpdate("INSERT INTO `Feature` VALUES (Feature_ID)");
                stmt.executeUpdate("INSERT INTO `Symbol` VALUES (Symbol_ID)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void updateCorrelation(int feature, int symbol, double value) {
        try {
            stmt.executeUpdate("INSERT INTO `Correlation` VALUES (Correlation_ID, '" + feature + "', '" + symbol + "', '" + value + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    double[][] loadCorrelation(int feature, int symbols) {
        ResultSet rs;
        int time = 0;
        try {
            rs = stmt.executeQuery("SELECT MAX(Correlation_ID) FROM Correlation");
            rs.next();
            time = rs.getInt("Max(Correlation_ID)");
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        double[][] tab = new double[time / symbols][symbols];
        try {
            rs = stmt.executeQuery("SELECT * FROM Correlation WHERE Feature_ID = '" + Integer.toString(feature) + "'");
            while (rs.next()) {
                int value1 = rs.getInt("Correlation_ID");
                int value2 = rs.getInt("Symbol_ID");
                double value3 = rs.getDouble("Value");
                tab[(value1 % symbols) - 1][value2 - 1] = value3;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tab;
    }
}
