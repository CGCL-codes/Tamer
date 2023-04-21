package JDBC;

import java.security.interfaces.RSAKey;
import java.sql.*;
import javax.print.attribute.standard.Finishings;

public class DataBase {

    String url;

    String username;

    String password;

    Connection connection;

    public ResultSet lastRs;

    public DataBase(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        connection = null;
        lastRs = null;
    }

    public void initialize() {
        try {
            if (connection != null && connection.isValid(0)) {
                return;
            }
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection(url, username, password);
            connection = conn;
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public ResultSet DoQuery(String query) {
        try {
            if (connection.isClosed() == true) {
                System.out.println("initial first");
                return null;
            }
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            lastRs = rs;
            return rs;
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return null;
        }
    }

    public int DoUpdateQuery(String query) {
        try {
            if (connection.isClosed() == true) {
                System.out.println("initial first");
                return -1;
            }
            Statement stmt = connection.createStatement();
            int updateStatus = stmt.executeUpdate(query);
            return updateStatus;
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return -1;
        }
    }

    public void finish() {
        try {
            connection.close();
            lastRs = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        connection.close();
    }
}
