package org.hsqldb.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import junit.framework.TestCase;
import junit.framework.TestResult;

/**
 * Test sql statements via jdbc against a database with cached tables
 * @author fredt@users
 */
public class TestSqlPersistent extends TestCase {

    String url = "jdbc:hsqldb:/hsql/test/testpersistent";

    String user;

    String password;

    Statement sStatement;

    Connection cConnection;

    public TestSqlPersistent(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        user = "sa";
        password = "";
        sStatement = null;
        cConnection = null;
        TestSelf.deleteDatabase("/hsql/test/testpersistent");
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            cConnection = DriverManager.getConnection(url, user, password);
            sStatement = cConnection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("TestSqlPersistence.setUp() error: " + e.getMessage());
        }
    }

    /**
     *  demonstration of bug fix #482109 - inserting Integers
     *  and Strings with PreparedStatement.setObject() did not work;
     *  String, Integer and Array types are inserted and retrieved<b>
     *
     *  demonstration of retrieving values using different getXXX methods
     */
    public void testInsertObject() {
        Object stringValue = null;
        Object integerValue = null;
        Object arrayValue = null;
        Object bytearrayValue = null;
        Object stringValueResult = null;
        Object integerValueResult = null;
        Object arrayValueResult = null;
        boolean wasNull = false;
        String message = "DB operation completed";
        try {
            String sqlString = "DROP TABLE PREFERENCE IF EXISTS;" + "CREATE CACHED TABLE PREFERENCE (" + "User_Id INTEGER NOT NULL, " + "Pref_Name VARCHAR(30) NOT NULL, " + "Pref_Value OBJECT NOT NULL, " + "DateCreated DATETIME DEFAULT NOW NOT NULL, " + "PRIMARY KEY(User_Id, Pref_Name) )";
            sStatement.execute(sqlString);
            sqlString = "INSERT INTO PREFERENCE " + "(User_Id,Pref_Name,Pref_Value,DateCreated) " + "VALUES (?,?,?,current_timestamp)";
            PreparedStatement ps = cConnection.prepareStatement(sqlString);
            stringValue = "String Value for Preference 1";
            integerValue = new Integer(1000);
            arrayValue = new Double[] { new Double(1), new Double(Double.NaN), new Double(Double.NEGATIVE_INFINITY), new Double(Double.POSITIVE_INFINITY) };
            bytearrayValue = new byte[] { 1, 2, 3, 4, 5, 6 };
            ps.setInt(1, 1);
            ps.setString(2, "String Type Object 1");
            ps.setObject(3, stringValue, Types.OTHER);
            ps.execute();
            ps.setInt(1, 2);
            ps.setString(2, "Integer Type Object 2");
            ps.setObject(3, integerValue);
            ps.execute();
            ps.setInt(1, 3);
            ps.setString(2, "Array Type Object 3");
            ps.setObject(3, arrayValue);
            ps.execute();
            ps.setInt(1, 3);
            ps.setString(2, "byte Array Type Object 3");
            ps.setObject(3, bytearrayValue, Types.OTHER);
            ps.execute();
            ResultSet rs = sStatement.executeQuery("SELECT * FROM PREFERENCE");
            boolean result = rs.next();
            String str = rs.getString(2);
            System.out.println(str);
            InputStream is = rs.getUnicodeStream(2);
            int c;
            while ((c = is.read()) > -1) {
                c = is.read();
                System.out.print((char) c);
            }
            System.out.println();
            is = rs.getAsciiStream(2);
            while ((c = is.read()) > -1) {
                System.out.print((char) c);
            }
            System.out.println();
            stringValueResult = rs.getObject(3);
            rs.next();
            integerValueResult = rs.getObject(3);
            rs.next();
            arrayValueResult = rs.getObject(3);
            wasNull = rs.wasNull();
            String castStringValue = (String) stringValueResult;
            Integer castIntegerValue = (Integer) integerValueResult;
            Double[] castDoubleArrayValue = (Double[]) arrayValueResult;
            {
                sqlString = "DELETE FROM PREFERENCE WHERE user_id = ?";
                PreparedStatement st = cConnection.prepareStatement(sqlString);
                st.setString(1, "2");
                int ret = st.executeUpdate();
                st.close();
                st = cConnection.prepareStatement("SELECT user_id FROM PREFERENCE WHERE user_id=?");
                st.setString(1, "2");
                rs = st.executeQuery();
                while (rs.next()) {
                    System.out.println(rs.getString(1));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (IOException e1) {
        }
        boolean success = true;
        assertEquals(true, success);
    }

    public void testSelectObject() throws IOException {
        String stringValue = null;
        Integer integerValue = null;
        Double[] arrayValue = null;
        byte[] byteArrayValue = null;
        String stringValueResult = null;
        Integer integerValueResult = null;
        Double[] arrayValueResult = null;
        boolean wasNull = false;
        String message = "DB operation completed";
        try {
            String sqlString = "DROP TABLE TESTOBJECT IF EXISTS;" + "CREATE CACHED TABLE TESTOBJECT (" + "ID INTEGER NOT NULL IDENTITY, " + "STOREDOBJECT OTHER, STOREDBIN BINARY )";
            sStatement.execute(sqlString);
            sqlString = "INSERT INTO TESTOBJECT " + "(STOREDOBJECT, STOREDBIN) " + "VALUES (?,?)";
            PreparedStatement ps = cConnection.prepareStatement(sqlString);
            stringValue = "Test String Value";
            integerValue = new Integer(1000);
            arrayValue = new Double[] { new Double(1), new Double(Double.NaN), new Double(Double.NEGATIVE_INFINITY), new Double(Double.POSITIVE_INFINITY) };
            byteArrayValue = new byte[] { 1, 2, 3 };
            ps.setObject(1, stringValue, Types.OTHER);
            ps.setBytes(2, byteArrayValue);
            ps.execute();
            ps.setObject(1, integerValue, Types.OTHER);
            ps.setBinaryStream(2, new ByteArrayInputStream(byteArrayValue), byteArrayValue.length);
            ps.execute();
            ps.setObject(1, arrayValue, Types.OTHER);
            ps.execute();
            ResultSet rs = sStatement.executeQuery("SELECT * FROM TESTOBJECT");
            boolean result = rs.next();
            stringValueResult = (String) rs.getObject(2);
            rs.next();
            integerValueResult = (Integer) rs.getObject(2);
            rs.next();
            arrayValueResult = (Double[]) rs.getObject(2);
            String castStringValue = (String) stringValueResult;
            Integer castIntegerValue = (Integer) integerValueResult;
            Double[] castDoubleArrayValue = (Double[]) arrayValueResult;
            for (int i = 0; i < arrayValue.length; i++) {
                if (!arrayValue[i].equals(arrayValueResult[i])) {
                    System.out.println("array mismatch: " + arrayValue[i] + " : " + arrayValueResult[i]);
                }
            }
            rs.close();
            ps.close();
            sqlString = "SELECT * FROM TESTOBJECT WHERE STOREDOBJECT = ?";
            ps = cConnection.prepareStatement(sqlString);
            ps.setObject(1, new Integer(1000));
            rs = ps.executeQuery();
            rs.next();
            Object returnVal = rs.getObject(2);
            rs.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        boolean success = stringValue.equals(stringValueResult) && integerValue.equals(integerValueResult) && java.util.Arrays.equals((Double[]) arrayValue, (Double[]) arrayValueResult);
        assertEquals(true, success);
        try {
            String sqlString = "drop table objects if exists";
            PreparedStatement ps = cConnection.prepareStatement(sqlString);
            ps.execute();
            sqlString = "create cached table objects (object_id INTEGER IDENTITY," + "object_name VARCHAR(128) NOT NULL,role_name VARCHAR(128) NOT NULL," + "value LONGVARBINARY NOT NULL,description LONGVARCHAR)";
            ps = cConnection.prepareStatement(sqlString);
            ps.execute();
            sqlString = "INSERT INTO objects VALUES(1, 'name','role',?,'description')";
            ps = cConnection.prepareStatement(sqlString);
            ps.setBytes(1, new byte[] { 1, 2, 3, 4, 5 });
            ps.executeUpdate();
            sqlString = "UPDATE objects SET value = ? AND description = ? WHERE " + "object_name = ? AND role_name = ?";
            ps = cConnection.prepareStatement(sqlString);
            ps.setBytes(1, new byte[] { 1, 2, 3, 4, 5 });
            ps.setString(2, "desc");
            ps.setString(3, "new");
            ps.setString(4, "role");
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    protected void tearDown() {
        try {
            cConnection.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("TestSql.tearDown() error: " + e.getMessage());
        }
    }

    public static void main(String[] argv) {
        TestResult result = new TestResult();
        TestCase testC = new TestSqlPersistent("testInsertObject");
        TestCase testD = new TestSqlPersistent("testSelectObject");
        testC.run(result);
        testD.run(result);
        System.out.println("TestSqlPersistent error count: " + result.failureCount());
    }
}
