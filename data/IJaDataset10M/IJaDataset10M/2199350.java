package tbag.core.reader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * DBConnector, connects to and performs queries on a sqlite3 database
 * @author Steve "Uru" West <info@ewolfuk.com>
 * @version 2010-08-19
 */
public class DBConnector {

    public static String jdbcName = "jdbc:sqlite";

    public static int queryTimeout = 30;

    private String dbName;

    private boolean isConnected;

    private Statement stmt;

    private ResultSet rs;

    private int lastUpdateNumRows;

    /**
     * Creates a new DBConnector object
     * @param name the name of the sqlite db to connect to
     */
    public DBConnector(String name) {
        dbName = name;
        isConnected = false;
        lastUpdateNumRows = 0;
    }

    /**
     * Attempts to connect to the database if not already connected
     */
    public void attemptConnection() {
        if (!isConnected) {
            String dbURL = jdbcName + ":" + dbName;
            System.out.println(dbURL);
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (Exception e) {
                System.out.println("Unable to get db driver: " + e.getMessage());
                return;
            }
            Connection conn;
            try {
                conn = DriverManager.getConnection(dbURL);
                stmt = conn.createStatement();
                stmt.setQueryTimeout(queryTimeout);
            } catch (SQLException ex) {
                isConnected = false;
                System.out.println("Unable to connect" + ex.getMessage());
            }
            isConnected = true;
        }
    }

    /**
     * @return the name of the database this DBConnect object is linked to
     */
    public String getName() {
        return dbName;
    }

    /**
     * @return true if there is a connection
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * @return the number of rows that the last query updated
     */
    public int getUpdatedRows() {
        return lastUpdateNumRows;
    }

    /**
     * Runs a query on the connected database.
     * @param query the query to run
     * @param expectReturn set to true if this query is expected to return results (eg, a SELECT query)
     * @return true on a sucuessful query
     */
    public boolean runQuery(String query, boolean expectReturn) {
        checkConenction();
        if (!expectReturn) {
            try {
                lastUpdateNumRows = stmt.executeUpdate(query);
                rs = null;
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                return false;
            }
        } else {
            try {
                rs = stmt.executeQuery(query);
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                return false;
            }
        }
        return true;
    }

    /**
     * Runs a list of queries. This will not stop for errors and it is not
     * possible to get results from the queries. This is only really suitable
     * for a large number of insertion or update queries.
     * @param list The list of queries to run
     */
    public void batchQuery(List<String> list) {
        for (String query : list) {
            runQuery(query, false);
        }
    }

    /**
     * Runs a query that expects no result on the connected database.
     * @param query the query to run
     * @return true on a sucuessful query
     */
    public boolean runQuery(String query) {
        return runQuery(query, false);
    }

    /**
     * @return false if the current results have no more rows
     */
    public boolean hasNext() {
        checkConenction();
        if (rs == null) {
            return false;
        }
        try {
            boolean status = rs.next();
            if (!status) {
                rs = null;
            }
            return status;
        } catch (SQLException ex) {
            return false;
        }
    }

    /**
     * Attemps to get the value of the given field from the current row
     * @param field the name of the field to get
     * @return null if the DBConnector is not connected or the field does not exist
     */
    public String getField(String field) {
        checkConenction();
        if (rs != null) {
            try {
                return rs.getString(field);
            } catch (SQLException ex) {
                return null;
            }
        }
        return null;
    }

    /**
     * Simply checks for a connection, if there is none then a DBNotConnectedException
     * is thrown
     */
    private void checkConenction() {
        if (!isConnected()) {
            throw new DBNotConnectedException();
        }
    }
}
