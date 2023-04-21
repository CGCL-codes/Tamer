package com.softtech.jdbc;

import java.sql.Date;
import java.sql.*;

/**
 * This class contains static methods for testing the JDBC framework code.
 * @author Jeff S Smith 
 */
public class SampleCodeUsingFramework {

    /** JDBC driver name */
    private static String driverName;

    /** JDBC connection URL */
    private static String connURL;

    /** JDBC connection username */
    private static String username;

    /** JDBC connection password */
    private static String password;

    /** select statement used by some test methods */
    static String sqlSelect = "SELECT TEST_ID, NOTES, TEST_DT, AMOUNT, CODE " + "FROM JDBC_TEST " + "WHERE TEST_ID < ? " + "AND TEST_DT < ? " + "AND CODE IS NOT NULL";

    /**
     * Sets fields to values required to connect to a sample MySQL database
     */
    private static void setMySQLConnectInfo() {
        driverName = "com.mysql.jdbc.Driver";
        connURL = "jdbc:mysql://localhost/mapp";
        username = "";
        password = "";
    }

    /**
     * Sets fields to values required to connect to a sample PostgreSQL database
     */
    private static void setPostgreSQLConnectInfo() {
        driverName = "org.postgresql.Driver";
        connURL = "jdbc:postgresql://localhost:5432/test";
        username = "username";
        password = "password";
    }

    /**
     * Sets fields to values required to connect to a sample ORACLE database
     */
    private static void setOracleConnectInfo() {
        driverName = "oracle.jdbc.driver.OracleDriver";
        connURL = "jdbc:oracle:thin:@SNOWMASS:1521:WDEV";
        username = "username";
        password = "password";
    }

    /**
     * Dummy method (placeholder) for recovery method
     */
    private static void applyDataIntegrityViolationRecovery() {
        System.out.println("Recovering from Oracle data integrity violation...");
    }

    /**
     * static method executes a standard jdbc query
     */
    public static void testStandardJDBC() {
        Connection con = null;
        try {
            Class.forName(driverName).newInstance();
            con = DriverManager.getConnection(connURL, username, password);
            PreparedStatement ps = con.prepareStatement(sqlSelect);
            ps.setInt(1, 8);
            ps.setDate(2, Date.valueOf("2003-05-10"));
            ResultSet rs = ps.executeQuery();
            String out = "SQL RESULTS:\n";
            while (rs.next()) out += rs.getLong("TEST_ID") + " " + rs.getString("NOTES") + " " + rs.getDate("TEST_DT") + " " + rs.getDouble("AMOUNT") + " " + rs.getString("CODE") + "\n";
            System.out.println(out);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            switch(sqle.getErrorCode()) {
                case 1:
                case 1407:
                case 1722:
                    applyDataIntegrityViolationRecovery();
            }
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }
    }

    /**
     * Creates a new connection pool object
     * @return ConnectionPool
     */
    public static ConnectionPool getConnectionPool() {
        return (new ConnectionPool(1, driverName, connURL, username, password));
    }

    /**
     * static method testing jdbc framework with a simple select statement using parameters
     */
    public static void testSimpleSelectWithParams() {
        try {
            SQLExecutor sqlExec = new SQLExecutor(getConnectionPool());
            sqlExec.addParam(8);
            sqlExec.addParam(Date.valueOf("2003-05-10"));
            SQLResults res = sqlExec.runQueryCloseCon(sqlSelect);
            String out = "SQL Results:\n";
            for (int row = 0; row < res.getRowCount(); row++) out += res.getLong(row, "TEST_ID") + " " + res.getString(row, "NOTES") + " " + res.getDate(row, "TEST_DT") + " " + res.getDouble(row, "AMOUNT") + " " + res.getString(row, "CODE") + "\n";
            System.out.println(out);
        } catch (DatabaseException e) {
            if (e.isDataIntegrityViolation()) applyDataIntegrityViolationRecovery();
        }
    }

    /**
     * static method testing jdbc framework with a simple select statement using parameters
     * and the result set's .toString() method
     */
    public static void testSimpleSelectWithParamsAndToString() {
        try {
            driverName = "oracle.jdbc.driver.OracleDriver";
            connURL = "jdbc:oracle:thin:@SNOWMASS:1521:WDEV";
            username = "wprg24";
            password = "wprg24";
            ConnectionPool conPool = new ConnectionPool(1, driverName, connURL, username, password);
            SQLExecutor sqlExec = new SQLExecutor(conPool);
            sqlExec.addParam(8);
            sqlExec.addParam(Date.valueOf("2003-05-10"));
            SQLResults res = sqlExec.runQueryCloseCon("SELECT * FROM UTILITY_STATUS");
            String out = "SQL Results:\n" + res.toString();
            System.out.println(out);
        } catch (DatabaseException e) {
            if (e.isDataIntegrityViolation()) applyDataIntegrityViolationRecovery();
        }
    }

    /**
     * static method testing jdbc framework with a simple select statement, parameters, and a
     * maximum number of rows
     */
    public static void testSimpleSelectAndMaxRows() {
        SQLExecutor sqlExec = new SQLExecutor(getConnectionPool());
        sqlExec.setMaxRows(5);
        sqlExec.addParam(new Integer(8));
        sqlExec.addParam(Date.valueOf("2003-05-10"));
        SQLResults res = sqlExec.runQueryCloseCon(sqlSelect);
        System.out.println(res.toString());
    }

    /**
     * This method tests the jdbc framework with a call to the following Oracle stored function:
     * 
     * CREATE OR REPLACE FUNCTION jdbc_test1 RETURN NUMBER IS
     * cnt_jdbc_test NUMBER;
     * BEGIN
     *    dbms_output.put_line ('starting jdbc_test1...');
     * 
     *    cnt_jdbc_test := 0;
     *    SELECT COUNT(*) INTO cnt_jdbc_test FROM JDBC_TEST;
     *    RETURN cnt_jdbc_test;
     *    EXCEPTION
     *      WHEN NO_DATA_FOUND THEN
     *        Null;
     *      WHEN OTHERS THEN
     *        -- Consider logging the error and then re-raise
     *        RAISE;
     * END jdbc_test1;
     * /
     */
    public static void testOracleStoredFunction() {
        String sql = "SELECT jdbc_test1() AS TestCount from dual";
        SQLExecutor sqlExec = new SQLExecutor(getConnectionPool());
        SQLResults res = sqlExec.runQueryCloseCon(sql);
        if (res.getRowCount() > 0) System.out.println("SQL Results: " + res.getInt(0, 0));
    }

    /**
     * static method testing jdbc framework with a delete and an insert statement
     */
    public static void testDeleteAndInsert() {
        SQLExecutor sqlExec1 = new SQLExecutor(getConnectionPool());
        sqlExec1.setAutoCommit(true);
        sqlExec1.addParam(new Integer(7));
        sqlExec1.runQueryCloseCon("DELETE FROM JDBC_TEST WHERE TEST_ID = ?");
        System.out.println(sqlExec1.getNumRecordsUpdated() + " record(s) deleted");
        SQLExecutor sqlExec2 = new SQLExecutor(getConnectionPool());
        sqlExec2.setAutoCommit(true);
        String sql = "INSERT INTO JDBC_TEST (TEST_ID, NOTES, TEST_DT, AMOUNT, CODE) " + "VALUES (7, 'seven', SYSDATE+2, 25.245, 'E')";
        sqlExec2.runQueryCloseCon(sql);
        System.out.println(sqlExec2.getNumRecordsUpdated() + " record(s) inserted");
    }

    /**
     * static method testing jdbc framework with an update statement
     */
    public static void testUpdate() {
        SQLExecutor sqlExec = new SQLExecutor(getConnectionPool());
        sqlExec.setAutoCommit(true);
        sqlExec.addParam(new Integer(7));
        sqlExec.runQueryCloseCon("UPDATE JDBC_TEST SET CODE = 'Z' WHERE TEST_ID = ?");
        System.out.println(sqlExec.getNumRecordsUpdated() + " record(s) updated");
    }

    /**
     * static method testing jdbc framework with multiple updates, using transaction management
     */
    public static void testMultipleUpdatesAndTrans() {
        ConnectionPool conPool = getConnectionPool();
        SQLExecutor sqlExec = new SQLExecutor(conPool);
        try {
            sqlExec.setAutoCommit(false);
            sqlExec.addParam(new Integer(7));
            sqlExec.runQuery("UPDATE JDBC_TEST SET CODE = 'Q' WHERE TEST_ID = ?");
            sqlExec.addParam(new Integer(6));
            sqlExec.runQuery("UPDATE JDBC_TEST SET CODE = 'R' WHERE TEST_ID = ?");
            sqlExec.rollbackTrans();
            System.out.println("transaction rolled back");
        } finally {
            sqlExec.closeConnection();
        }
    }

    /**
     * static method testing jdbc framework with multiple updates and intentional sql exceptions
     * that are trapped in appropriate exception handling blocks
     */
    public static void testMultipleUpdatesAndTransWithException() {
        ConnectionPool conPool = getConnectionPool();
        SQLExecutor sqlExec = new SQLExecutor(conPool);
        try {
            sqlExec.setAutoCommit(false);
            sqlExec.addParam(new Integer(7));
            sqlExec.runQuery("UPDATE JDBC_TEST SET CODE = 'Z' WHERE TEST_ID = ?");
            sqlExec.addParam(new Integer(6));
            sqlExec.runQuery("UPDATE JDBC_TEST SET TEST_ID = NULL WHERE TEST_ID = ?");
            sqlExec.commitTrans();
            System.out.println("transaction committed");
        } catch (DatabaseException e) {
            System.out.println("Error code=" + e.getSQLErrorCode() + ",  SQLState=" + e.getSQLState());
            if (e.isDataIntegrityViolation()) System.out.println("data integrity violation"); else if (e.isBadSQLGrammar()) System.out.println("bad SQL grammar"); else if (e.isNonExistentTableOrViewOrCol()) System.out.println("Non existent table or view");
            System.out.println(e.getMessage());
            sqlExec.rollbackTrans();
            System.out.println("transaction rolled back");
        } finally {
            sqlExec.closeConnection();
        }
    }

    /**
     * static method testing jdbc framework with a simple select statement that returns no rows
     */
    public static void testSelectWhichReturnsNoRows() {
        SQLExecutor sqlExec = new SQLExecutor(getConnectionPool());
        SQLResults res = sqlExec.runQueryCloseCon("select * from JDBC_TEST where CODE = '4'");
        System.out.println(res.toString());
    }

    /**
     * static method testing jdbc framework with a select for update statement (that locks a record)
     * and an update statement that generates a deadlock.
     */
    public static void testDeadlockException() {
        ConnectionPool conPool = getConnectionPool();
        SQLExecutor sqlExec1 = new SQLExecutor(conPool);
        try {
            sqlExec1.setAutoCommit(false);
            sqlExec1.addParam(new Integer(2));
            sqlExec1.runQuery("SELECT CODE FROM JDBC_TEST WHERE TEST_ID = ? FOR UPDATE");
            System.out.println("Attempting to update a record locked by another connection...");
            SQLExecutor sqlExec2 = new SQLExecutor(getConnectionPool());
            sqlExec2.setTimeoutInSec(5);
            sqlExec2.addParam(new Integer(2));
            sqlExec2.runQueryCloseCon("UPDATE JDBC_TEST SET CODE = 'X' WHERE TEST_ID = ?");
        } catch (DatabaseException e) {
            System.out.println("Error code=" + e.getSQLErrorCode() + ", " + e.getMessage());
            if (e.isRowlockOrTimedOut()) System.out.println("Rowlock exception!");
        } finally {
            conPool.closeAllConnections();
        }
    }

    /**
     * Illustrates creating a database connection using standard JDBC and then using this
     * connection to create a ConnectionPool and execute a select statement.
     */
    public static void testCreatingOwnConnection() {
        Connection con = null;
        try {
            Class.forName(driverName).newInstance();
            con = DriverManager.getConnection(connURL, username, password);
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage() + "\n" + "SQL State: " + sqle.getSQLState());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        SQLExecutor sqlExec = new SQLExecutor(new ConnectionPool(con));
        SQLResults res = sqlExec.runQueryCloseCon("SELECT COUNT(*) FROM JDBC_TEST");
        System.out.println("Record count=" + res.getInt(0, 0));
    }

    /**
     * Does a simple select from a MySQL database.
     */
    public static void testMySQL() {
        String driverName = "com.mysql.jdbc.Driver";
        String connURL = "jdbc:mysql://localhost/test";
        String username = "test";
        String password = "";
        ConnectionPool conPool = new ConnectionPool(1, driverName, connURL, username, password);
        SQLExecutor sqlExec = new SQLExecutor(conPool);
        SQLResults res = sqlExec.runQueryCloseCon("SELECT * FROM JDBC_TEST");
        System.out.println(res.toString());
    }

    /**
     * Creates a connection pool (2 connections) and execututes queries on each. It then tries to 
     * get a third connection.
     */
    public static void testConnectionPooling() {
        ConnectionPool conPool = new ConnectionPool(2, driverName, connURL, username, password);
        conPool.resizeConnectionPool(3);
        String out = "";
        try {
            SQLExecutor sqlExec1 = new SQLExecutor(conPool);
            SQLResults res1 = sqlExec1.runQuery("select dd * from JDBC_TEST where CODE < 'E'");
            res1.setToStringFormatWidth(11);
            out += res1.toString() + "\n\n";
            SQLExecutor sqlExec2 = new SQLExecutor(conPool);
            SQLResults res2 = sqlExec2.runQuery("select * from JDBC_TEST where CODE > 'E'");
            out += res2.toString() + "\n\n";
            SQLExecutor sqlExec3 = new SQLExecutor(conPool);
            SQLResults res3 = sqlExec2.runQuery("select * from JDBC_TEST where CODE > 'E'");
            out += res3.toString();
        } finally {
            conPool.closeAllConnections();
        }
        System.out.println(out);
    }

    public static void testSelectDistinctAndMixedCase() {
        ConnectionPool conPool = getConnectionPool();
        SQLExecutor sqlExec = new SQLExecutor(conPool);
        String sql = "SELECT distinct test_id, test_dt " + "FROM JDBC_TEST " + "WHERE test_dt = (SELECT MAX(test_dt) FROM JDBC_TEST)";
        SQLResults res = sqlExec.runQueryCloseCon(sql);
        String testID = res.getString(0, "test_id");
        Date testDate = res.getDate(0, "TEST_DT");
        System.out.println("test_id = " + testID);
        System.out.println("TEST_DT = " + testDate);
    }

    /**
     * This procecure calls the following Oracle stored procedure which inserts a new row 
     * into the JDBC_TEST table.
     * 
     * CREATE OR REPLACE PROCEDURE jdbc_proc_test1(pTEST_ID NUMBER, pNOTES VARCHAR2)
     * IS
     * BEGIN
     *     INSERT INTO JDBC_TEST(TEST_ID, NOTES, TEST_DT, AMOUNT, CODE)
     *     VALUES (pTEST_ID, pNOTES, SYSDATE, 44.44, 'Z');
     * END;
     * /
     */
    public static void testCallingStoredProc() {
        SQLExecutor sqlExec = new SQLExecutor(getConnectionPool());
        sqlExec.addParam(new Integer(8));
        sqlExec.addParam("This record inserted via stored proc call");
        sqlExec.runStoredProcCloseCon("jdbc_proc_test1");
    }

    /**
     * This procedure calls the following Oracle stored procedure and gets the value from the 
     * OUT param pFULLNAME.
     * 
     * CREATE OR REPLACE PROCEDURE jdbc_proc_test2(
     *     pFIRSTNAME IN VARCHAR2, 
     *     pLASTNAME  IN VARCHAR2, 
     *     pFULLNAME  OUT VARCHAR2)
     * IS
     * BEGIN
     *     pFULLNAME := pFIRSTNAME || ' ' || pLASTNAME;
     * END;
     * 
     */
    public static void testCallingStoredProcWithAnOUTParam() {
        SQLExecutor sqlExec = new SQLExecutor(getConnectionPool());
        sqlExec.addParam("Jeff");
        sqlExec.addParam("Smith");
        sqlExec.addStoredProcOutParam("fullname");
        SQLResults res = sqlExec.runStoredProcCloseCon("jdbc_proc_test2");
        System.out.println("Your full name is " + res.getString(0, 2));
        System.out.println(res.toString());
    }

    /**
     * Run a parameterized query (sql) once with one parameter and then again
     * with another parameter. Since the sql doesn't change from the first call to
     * runQuery() to the second call, the runQuery() method only prepares the SQL
     * statement once (the first time it is called). This was verified with the debugger. 
     */
    public static void testParameterizedQuery() {
        String sql = "SELECT SURROGATE_ID, CLASS_CD FROM INV WHERE SURROGATE_ID = ?";
        SQLExecutor sqlExec = new SQLExecutor(getConnectionPool());
        sqlExec.addParam(840874);
        SQLResults res = sqlExec.runQuery(sql);
        System.out.println(res.toString());
        sqlExec.addParam(925659);
        res = sqlExec.runQuery(sql);
        System.out.println("\n" + res.toString());
    }

    public static void testThis() {
        String sql = "UPDATE JEFF_TEST SET HEIGHT = ? WHERE EMPID < 4";
        SQLExecutor sqlExec = new SQLExecutor(getConnectionPool());
        sqlExec.addParam(55);
        sqlExec.runQueryCloseCon(sql);
        System.out.println(sqlExec.getNumRecordsUpdated() + " records updated");
    }

    /**
     * static main method testing jdbc framework with various method calls
     * @param args
     */
    public static void main(String[] args) {
        setOracleConnectInfo();
        testThis();
    }
}
