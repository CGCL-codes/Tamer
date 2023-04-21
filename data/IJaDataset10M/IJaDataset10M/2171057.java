package utility;

import beans.ProductListBean;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Peforms database related operations
 * @author krishna
 */
public class DBConnection {

    private String jdbcURL = null;

    private Connection connection = null;

    private Statement statement = null;

    private ResultSet resultSet = null;

    public DBConnection(String url) {
        this.jdbcURL = url;
    }

    /**
     * Executes a query 
     * @param query execute query
     * @return ResultSet
     * @throws Exception
     */
    public ResultSet ExecuteQuery(String query) throws Exception {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(this.jdbcURL);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
            } catch (Exception ex) {
            }
            try {
            } catch (Exception ex) {
            }
        }
        return resultSet;
    }

    /**
     * Updates the database
     * @param query Update query
     * @return Number of rows updated
     * @throws Exception
     */
    public int ExecuteUpdate(String query) throws Exception {
        int result = -1;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(this.jdbcURL);
            statement = connection.createStatement();
            result = statement.executeUpdate(query);
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
            } catch (Exception ex) {
            }
            try {
            } catch (Exception ex) {
            }
        }
        return result;
    }

    /**
     * Closes resultset, statement, and connection
     */
    public void close() {
        try {
            this.resultSet.close();
        } catch (Exception ex) {
        }
        try {
            this.statement.close();
        } catch (Exception ex) {
        }
        try {
            this.connection.close();
        } catch (Exception ex) {
        }
    }

    /**
     * Testing purpose
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        DBConnection con = new DBConnection("jdbc:mysql://localhost/dbComputerShopping?user=root&password=00977");
        ResultSet rs = con.ExecuteQuery("SELECT product_id,name,description FROM products");
        while (rs.next()) {
            String test = rs.getString("product_id");
            System.out.println(test);
        }
        rs.close();
        ProductListBean list = new ProductListBean();
        System.out.println(list.getXml());
    }
}
