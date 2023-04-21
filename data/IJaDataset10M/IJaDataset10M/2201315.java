package onto.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import onto.application.OBO_PlainTextAnalyzer;

public class DBClient {

    private static final String MYSQL_DRIVER_NAME = "com.mysql.jdbc.Driver";

    private static final String URL = "jdbc:mysql://10.160.14.132:3306/";

    private static final String DATABASE = "medline";

    private static Connection con;

    /**
	 * A method that prepares a database connection and 
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
    public static Connection getDB_Connection() throws SQLException, ClassNotFoundException {
        StringBuilder dbBuilder = new StringBuilder();
        dbBuilder.append(URL);
        dbBuilder.append(DATABASE);
        Class.forName(MYSQL_DRIVER_NAME);
        con = DriverManager.getConnection(dbBuilder.toString(), "medline_ro", "medline");
        System.out.println(con.getCatalog());
        return con;
    }

    public static ResultSet getMedlineAbstract() throws SQLException, ClassNotFoundException {
        StringBuilder queryString = new StringBuilder();
        ResultSet rs = null;
        Statement stmt = null;
        queryString.append("SELECT pmid, title, abstract from article;");
        stmt = con.createStatement();
        rs = stmt.executeQuery(queryString.toString());
        return rs;
    }

    /**
	 * @param args
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
    public static void main(String[] args) {
        int count = 1;
        try {
            Connection con = DBClient.getDB_Connection();
            ResultSet resSet = DBClient.getMedlineAbstract();
            while (resSet != null && resSet.next()) {
                System.out.println(count++);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage().toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println(e.getMessage().toString());
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println(e.getMessage().toString());
            }
        }
    }
}
