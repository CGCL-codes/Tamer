package pattern.part1.chapter2.callback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Date: 2009-11-19
 * Time: 0:14:43
 */
public class SimpleJdbcQueryTemplate {

    public <T> T query(String queryString, ResultSetHandler<T> rsHandler) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = ConnectionUtils.getConnection();
            stmt = connection.prepareStatement(queryString);
            ResultSet rs = stmt.executeQuery();
            return rsHandler.handle(rs);
        } catch (SQLException ex) {
            closeStatement(stmt);
            stmt = null;
            releaseConnection(connection);
            connection = null;
            throw new RuntimeException("An sql exception occurred.", ex);
        } finally {
            closeStatement(stmt);
            releaseConnection(connection);
        }
    }

    private void releaseConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
            } catch (Throwable ex) {
            }
        }
    }

    private void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ex) {
            } catch (Throwable ex) {
            }
        }
    }
}
