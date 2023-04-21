package wsmg.msg_box.storage.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcStorage {

    private ConfigurationParamsReader config;

    private String jdbcUrl = null;

    ;

    private String messagePreservationDays;

    private String messagePreservationHours;

    private String messagePreservationMinutes;

    private PreparedStatement stmt = null;

    private ResultSet rs = null;

    private ResultSetMetaData rsmd = null;

    private ConnectionPool connectionPool;

    private String jdbcDriver;

    public JdbcStorage(String fileName, boolean enableTransactions) {
        config = new ConfigurationParamsReader(fileName);
        jdbcUrl = config.getProperty("jdbcUrl");
        jdbcDriver = config.getProperty("jdbcDriver");
        this.messagePreservationDays = config.getProperty("messagePreservationDays");
        this.messagePreservationHours = config.getProperty("messagePreservationHours");
        this.messagePreservationMinutes = config.getProperty("messagePreservationMinutes");
        try {
            if (enableTransactions) {
                connectionPool = new ConnectionPool(jdbcDriver, jdbcUrl, 10, 50, true, false, Connection.TRANSACTION_SERIALIZABLE);
            } else {
                connectionPool = new ConnectionPool(jdbcDriver, jdbcUrl, 10, 50, true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create database connection poll.", e);
        }
    }

    public JdbcStorage(String fileName) {
        this(fileName, false);
    }

    public Connection connect() throws SQLException {
        Connection conn = connectionPool.getConnection();
        return conn;
    }

    public void closeConnection(Connection conn) throws java.sql.SQLException {
        connectionPool.free(conn);
    }

    public int update(String query) throws java.sql.SQLException {
        int result = 0;
        Connection conn = connectionPool.getConnection();
        stmt = conn.prepareStatement(query);
        result = stmt.executeUpdate();
        stmt.close();
        connectionPool.free(conn);
        return result;
    }

    /**
     * This method is provided so that yo can have better control over the
     * statement. For example: You can use stmt.setString to convert quotation
     * mark automatically in an INSERT statement
     */
    public int insert(PreparedStatement stmt) throws java.sql.SQLException {
        int rows = 0;
        rows = stmt.executeUpdate();
        stmt.close();
        return rows;
    }

    public int insert(String query) throws java.sql.SQLException {
        int rows = 0;
        Connection conn = connectionPool.getConnection();
        stmt = conn.prepareStatement(query);
        rows = stmt.executeUpdate();
        stmt.close();
        connectionPool.free(conn);
        return rows;
    }

    public ResultSet query(String query) throws SQLException {
        Connection conn = connectionPool.getConnection();
        Statement lstmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = lstmt.executeQuery(query);
        connectionPool.free(conn);
        return rs;
    }

    public int countRow(String tableName, String columnName) throws java.sql.SQLException {
        String query = new String("SELECT COUNT(" + columnName + ") FROM " + tableName);
        Connection conn = connectionPool.getConnection();
        stmt = conn.prepareStatement(query);
        rs = stmt.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        stmt.close();
        connectionPool.free(conn);
        return count;
    }

    public int getMessagePreservationDays() {
        return Integer.parseInt(messagePreservationDays);
    }

    public int getMessagePreservationHours() {
        return Integer.parseInt(messagePreservationHours);
    }

    public int getMessagePreservationMinutes() {
        return Integer.parseInt(messagePreservationMinutes);
    }

    public long getInterval() {
        long interval = this.getMessagePreservationDays() * 24;
        interval = (interval + this.getMessagePreservationHours()) * 60;
        interval = (interval + this.getMessagePreservationMinutes()) * 60;
        interval = interval * 1000;
        return interval;
    }
}
