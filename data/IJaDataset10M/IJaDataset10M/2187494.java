package net.teqlo.jdbc;

import java.sql.*;
import java.util.Random;
import java.util.Stack;
import net.teqlo.TeqloException;
import net.teqlo.management.NanoTimer;
import net.teqlo.management.NanoTimers;
import net.teqlo.util.Loggers;
import net.teqlo.util.TeqloProperties;

public abstract class JDBCDatabase {

    private static final String DRIVER_PROP = "net.teqlo.jdbc.driverClassName";

    private static final String DRIVER_DEFAULT = "org.apache.derby.jdbc.EmbeddedDriver";

    public static final String URL_PROP = "net.teqlo.jdbc.url";

    private static final String URL_DEFAULT = "jdbc:derby:teqloDB;create=true";

    private static final String USER_PROP = "net.teqlo.jdbc.username";

    private static final String USER_DEFAULT = "teqlo";

    private static final String PASSWORD_PROP = "net.teqlo.jdbc.password";

    private static final String PASSWORD_DEFAULT = "teqlo";

    public static final int DB_RETRIES = 5;

    private static Random random = new Random();

    private String propsBase;

    private static Stack<Connection> connections = null;

    private static boolean derby = false;

    private static boolean mySql = false;

    public JDBCDatabase(String propsBase) throws TeqloException {
        this.propsBase = propsBase;
        setupConnections(TeqloProperties.getString(DRIVER_PROP, DRIVER_DEFAULT));
        try {
            checkSchemas();
        } catch (SQLException e) {
            throw new TeqloException(this, propsBase, e, "Could not set up schema in database");
        }
    }

    private synchronized void setupConnections(String driver) throws TeqloException {
        if (connections != null) return;
        connections = new Stack<Connection>();
        try {
            Class.forName(driver).newInstance();
        } catch (Exception e) {
            throw new TeqloException(this, driver, e, "JDBC driver class not found. Please check the class path.");
        }
        derby = driver.startsWith("org.apache.derby");
        mySql = driver.startsWith("com.mysql");
    }

    private Connection makeConnection() throws TeqloException {
        String url = TeqloProperties.getString(URL_PROP, URL_DEFAULT);
        String username = TeqloProperties.getString(USER_PROP, USER_DEFAULT);
        String password = TeqloProperties.getString(PASSWORD_PROP, PASSWORD_DEFAULT);
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException e) {
            throw new TeqloException(this, url, e, "Could not log into database. Check url, username, and password properties.");
        }
    }

    public synchronized void close() {
        if (connections != null) {
            for (Connection connection : connections) try {
                connection.close();
            } catch (SQLException e) {
            }
            connections.clear();
        }
    }

    private synchronized Connection getConnection() throws TeqloException {
        if (!connections.empty()) return connections.pop();
        return makeConnection();
    }

    private synchronized void releaseConnection(Connection connection) {
        connections.push(connection);
    }

    public void executeSimpleUpdate(String opName, final String statement) throws SQLException, TeqloException {
        wrapWithDeadlockRetry(opName, new JDBCTransactionWrapper<Object>() {

            public Object run(Connection connection) throws SQLException {
                executeSimpleUpdate(connection, statement);
                return null;
            }
        });
    }

    public void executeSimpleUpdate(Connection connection, String statement) throws SQLException {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            stmt.executeUpdate(statement);
        } finally {
            if (stmt != null) stmt.close();
        }
    }

    public String executeSimpleSelect(String opName, final String statement) throws SQLException, TeqloException {
        return wrapWithDeadlockRetry(opName, new JDBCTransactionWrapper<String>() {

            public String run(Connection connection) throws SQLException {
                return executeSimpleSelect(connection, statement);
            }
        });
    }

    public String executeSimpleSelect(Connection connection, String statement) throws SQLException {
        ResultSet rs = null;
        Statement stmt = null;
        String value = null;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(statement);
            while (rs.next()) {
                value = rs.getString(1);
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        return value;
    }

    private void checkSchemas() throws SQLException, TeqloException {
        boolean hasTqSchema = false;
        Connection connection = getConnection();
        DatabaseMetaData dbm = connection.getMetaData();
        ResultSet tables = dbm.getTables(null, null, "TQ_SCHEMA", null);
        if (tables.next()) hasTqSchema = true;
        tables.close();
        releaseConnection(connection);
        if (!hasTqSchema) {
            executeSimpleUpdate("createSchema", "CREATE TABLE TQ_SCHEMA (schema_name VARCHAR(80), schema_version VARCHAR(10))");
            executeSimpleUpdate("insertSchema", "INSERT INTO TQ_SCHEMA (schema_name, schema_version) VALUES ('teqlo', '1.0')");
        }
        String version = executeSimpleSelect("getSchema", "SELECT schema_version FROM TQ_SCHEMA WHERE schema_name = '" + propsBase + "'");
        if (version == null) {
            executeSimpleUpdate("insertSchema", "INSERT INTO TQ_SCHEMA (schema_name, schema_version) VALUES ('" + propsBase + "', '')");
            version = "";
        }
        while (true) {
            String newVersion = checkSchema(version);
            if (newVersion != null && newVersion.length() > 0 && !newVersion.equals(version)) {
                executeSimpleUpdate("updateSchema", "UPDATE TQ_SCHEMA SET schema_version = '" + newVersion + "' WHERE schema_name = '" + propsBase + "'");
                version = newVersion;
            } else break;
        }
    }

    public boolean isDerby() {
        return derby;
    }

    public abstract String checkSchema(String version) throws SQLException, TeqloException;

    public String getAutoIncrement() {
        if (derby) return " GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) ";
        return " AUTO_INCREMENT ";
    }

    public String getCreateTableSuffix() {
        if (mySql) return " ENGINE = INNODB ";
        return "";
    }

    public String getCLOBType() {
        if (mySql) return "LONGTEXT";
        return "CLOB";
    }

    /**
     * Wraps the run() method of the supplied wrapper object (normally an anonymous local class) in a JDBC transaction.
     * TODO: Unsynchronize this once we have a connection pool
     * @param transactionName for logging and statistics
     * @param txw
     * @throws TeqloException
     */
    public synchronized <T> T wrapWithDeadlockRetry(String transactionName, JDBCTransactionWrapper<T> txw) throws TeqloException, SQLException {
        NanoTimer timer = NanoTimers.start("JDBCTransaction");
        SQLException cause = null;
        for (int i = 0; i < DB_RETRIES; i++) {
            Connection connection = null;
            try {
                connection = getConnection();
                T returnValue = txw.run(connection);
                connection.commit();
                NanoTimers.step(timer, transactionName);
                return returnValue;
            } catch (TeqloException te) {
                if (connection != null) connection.rollback();
                throw te;
            } catch (SQLException e) {
                if (connection != null) {
                    Connection con = connection;
                    connection = null;
                    try {
                        con.rollback();
                    } catch (SQLException e1) {
                    }
                    ;
                    try {
                        con.close();
                    } catch (SQLException e2) {
                    }
                    ;
                }
                if (isTransientError(e)) {
                    Loggers.XML_RUNTIME.info("Transaction " + transactionName + " retry " + (i + 1), e);
                    if (i == 0) Thread.yield(); else try {
                        Thread.sleep(i * i * 10 + random.nextInt(50));
                    } catch (InterruptedException ie) {
                    }
                    ;
                    continue;
                } else throw e;
            } finally {
                if (connection != null) releaseConnection(connection);
            }
        }
        throw new TeqloException(this, txw, cause, "Transaction failed after " + DB_RETRIES + " retries");
    }

    /**
     * Returns true if the supplied SQLException is transient (like a deadlock or a lost connection)
     * and should be retried.
     * 
     * @return true if should retry transaction, false otherwise
     */
    private static boolean isTransientError(SQLException e) {
        Loggers.XML_DATABASE.info("SQL Exception " + e.getErrorCode() + " " + e.getSQLState());
        return true;
    }
}
