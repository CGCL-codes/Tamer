package addons;

import java.sql.*;

public class MysqlConnect {

    private Connection conn = null;

    public MysqlConnect() {
        String url = "jdbc:mysql://localhost:3306/";
        String dbName = "ceha";
        String driver = "com.mysql.jdbc.Driver";
        String userName = "root";
        String password = "";
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url + dbName, userName, password);
            System.out.println("Connected to the database");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConn() {
        return conn;
    }
}
