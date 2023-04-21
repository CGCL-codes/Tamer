package org.compiere.db;

import java.sql.*;

/**
 *	Test Connection (speed)
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: TestConnection.java,v 1.2 2006/07/30 00:55:13 jjanke Exp $
 */
public class TestConnection {

    /**
	 * 	Test Connection
	 *
	 * 	@param jdbcURL JDBC URL
	 * 	@param uid user
	 * 	@param pwd password
	 */
    public TestConnection(String jdbcURL, String uid, String pwd) {
        System.out.println("Test Connection for " + jdbcURL);
        m_jdbcURL = jdbcURL;
        m_uid = uid;
        m_pwd = pwd;
        init();
        if (m_conn != null) {
            long time = test();
            time += test();
            time += test();
            time += test();
            System.out.println("");
            System.out.println("Total Average (" + m_jdbcURL + ")= " + (time / 4) + "ms");
        }
    }

    private String m_jdbcURL;

    private String m_uid = "adempiere";

    private String m_pwd = "adempiere";

    private String m_sql = "SELECT * FROM AD_Element";

    private Connection m_conn;

    /**
	 * 	Initialize & Open Connection
	 */
    private void init() {
        long start = System.currentTimeMillis();
        Driver driver = null;
        try {
            driver = DriverManager.getDriver(m_jdbcURL);
        } catch (SQLException ex) {
        }
        if (driver == null) {
            try {
                DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            } catch (SQLException ex) {
                System.err.println("Init = register Driver: " + ex);
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("(1) Driver = " + (end - start) + "ms");
        start = System.currentTimeMillis();
        try {
            m_conn = DriverManager.getConnection(m_jdbcURL, m_uid, m_pwd);
        } catch (SQLException ex) {
            System.err.println("Init = get Connection: " + ex);
        }
        end = System.currentTimeMillis();
        System.out.println("(2) Get Connection = " + (end - start) + "ms");
        start = System.currentTimeMillis();
        try {
            if (m_conn != null) m_conn.close();
        } catch (SQLException ex) {
            System.err.println("Init = close Connection: " + ex);
        }
        end = System.currentTimeMillis();
        System.out.println("(3) Close Connection = " + (end - start) + "ms");
    }

    /**
	 * 	Test ResultSet
	 * 	@return time in ms
	 */
    private long test() {
        System.out.println("");
        long totalStart = System.currentTimeMillis();
        long start = System.currentTimeMillis();
        try {
            m_conn = DriverManager.getConnection(m_jdbcURL, m_uid, m_pwd);
        } catch (SQLException ex) {
            System.err.println("Test get Connection: " + ex);
            return -1;
        }
        long end = System.currentTimeMillis();
        System.out.println("(A) Get Connection = " + (end - start) + "ms");
        try {
            start = System.currentTimeMillis();
            Statement stmt = m_conn.createStatement();
            end = System.currentTimeMillis();
            System.out.println("(B) Create Statement = " + (end - start) + "ms");
            start = System.currentTimeMillis();
            ResultSet rs = stmt.executeQuery(m_sql);
            end = System.currentTimeMillis();
            System.out.println("(C) Execute Query = " + (end - start) + "ms");
            int no = 0;
            start = System.currentTimeMillis();
            while (rs.next()) {
                int i = rs.getInt("AD_Client_ID");
                String s = rs.getString("Name");
                i += s.length();
                no++;
            }
            end = System.currentTimeMillis();
            System.out.println("(D) Read ResultSet = " + (end - start) + "ms - per 10 rows " + ((end - start) / (no / 10)) + "ms");
            start = System.currentTimeMillis();
            rs.close();
            end = System.currentTimeMillis();
            System.out.println("(E) Close ResultSet = " + (end - start) + "ms");
            start = System.currentTimeMillis();
            stmt.close();
            end = System.currentTimeMillis();
            System.out.println("(F) Close Statement = " + (end - start) + "ms");
        } catch (SQLException e) {
            System.err.println("Test: " + e);
        }
        start = System.currentTimeMillis();
        try {
            if (m_conn != null) m_conn.close();
        } catch (SQLException ex) {
            System.err.println("Test close Connection: " + ex);
        }
        end = System.currentTimeMillis();
        System.out.println("(G) Close Connection = " + (end - start) + "ms");
        long totalEnd = System.currentTimeMillis();
        System.out.println("Total Test = " + (totalEnd - totalStart) + "ms");
        return (totalEnd - totalStart);
    }

    /**
	 * 	Test Connection.
	 *  java -cp dbPort.jar;oracle.jar org.compiere.db.TestConnection
	 * 	@param args arguments optional <jdbcURL> <uid> <pwd>
	 * 	Example: jdbc:oracle:thin:@dev:1521:dev adempiere adempiere
	 */
    public static void main(String[] args) {
        String url = "jdbc:oracle:thin:@//24.151.26.64:1521/lap11";
        String uid = "adempiere";
        String pwd = "adempiere";
        if (args.length == 0) {
            System.out.println("TestConnection <jdbcUrl> <uid> <pwd>");
            System.out.println("Example: jdbc:oracle:thin:@//dev:1521/dev adempiere adempiere");
            System.out.println("Example: jdbc:oracle:oci8:@dev adempiere adempiere");
        } else if (args.length > 0) url = args[0]; else if (args.length > 1) url = args[1]; else if (args.length > 2) url = args[2];
        System.out.println("");
        TestConnection test = new TestConnection(url, uid, pwd);
    }
}
