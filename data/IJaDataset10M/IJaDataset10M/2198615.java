package cat.jm.cru.database;

import cat.jm.cru.exception.DataBaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataBaseUtils {

    private static final Logger logger = LoggerFactory.getLogger(DataBaseUtils.class);

    public static Connection getConnection(String driver, String url, String user, String password) throws DataBaseException {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            logger.error("Error instantiating database driver" + driver, e);
            throw new DataBaseException("Error instantiating database driver" + driver, e);
        }
        try {
            if ((user == null || user.length() == 0) && (password == null || password.length() == 0)) return DriverManager.getConnection(url);
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            logger.error("Error opening database connection", e);
            throw new DataBaseException("Error opening database connection", e);
        }
    }

    public static void closeConnection(Connection connection) throws DataBaseException {
        if (connection != null) try {
            connection.close();
        } catch (SQLException e) {
            throw new DataBaseException("Error closing database connection", e);
        }
    }

    public static void testConnection(Connection connection, String query) throws DataBaseException {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            if (!rs.next()) throw new DataBaseException("Query [" + query + "] not returning results");
        } catch (SQLException e) {
            throw new DataBaseException("Error testing connection", e);
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (rs != null) rs.close();
            } catch (SQLException e) {
                logger.error("Error closing connection.", e);
            }
        }
    }
}
