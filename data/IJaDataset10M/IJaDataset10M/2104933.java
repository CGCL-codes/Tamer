package br.com.jenquete;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Denis Dayvison de Freitas
 */
public class Database {

    static Connection connection;

    static {
        try {
            String userName = "root";
            String password = "";
            String url = "jdbc:mysql://localhost/jenquete";
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, userName, password);
        } catch (Exception e) {
        }
    }

    /** Creates a new instance of Database */
    public Database() {
    }

    public Connection getConnection() {
        return connection;
    }

    public void listarEnquetes() {
        try {
            PreparedStatement pstmt = connection.prepareStatement("select * from enquetes");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("Cod. " + rs.getString("CodEnquete"));
                System.out.println("Cod. " + rs.getString("QuestaoEnquete"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Database db = new Database();
        db.listarEnquetes();
    }
}
