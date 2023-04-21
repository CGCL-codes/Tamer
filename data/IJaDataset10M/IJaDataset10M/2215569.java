package pt.gwt.teste.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    static String status = "";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String url = "jdbc:mysql://127.0.0.1:3306/gpe";
            conn = DriverManager.getConnection(url, "root", "root");
            status = "connection opened";
        } catch (SQLException e) {
            status = e.getMessage();
        } catch (ClassNotFoundException e) {
            status = e.getMessage();
        } catch (Exception e) {
            status = e.getMessage();
        }
        return conn;
    }
}
