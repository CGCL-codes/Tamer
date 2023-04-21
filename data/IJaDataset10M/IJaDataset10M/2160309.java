package org.ttalbott.mytelly;

import java.sql.*;

/**
 *
 * @author  Tom Talbott
 * @version 
 */
public class SQLData extends java.lang.Object {

    private static final String m_driver = "org.hsqldb.jdbcDriver";

    private static final String m_url = "jdbc:hsqldb:mytelly";

    private static final String m_urlTest = "jdbc:hsqldb:test";

    private static final String m_user = "sa";

    private static final String m_password = "";

    private static final String m_dropTable = "DROP TABLE ";

    private static final String m_createTable = "CREATE CACHED TABLE ";

    private static final String m_insertRow = "INSERT INTO ";

    private static final String m_select = "SELECT ";

    private static final String m_delete = "DELETE FROM ";

    private static final String m_update = "UPDATE ";

    private static final String m_values = " VALUES(";

    private static final String m_from = " FROM ";

    private static final String m_primaryKey = ",PRIMARY KEY(";

    private static final String m_where = " WHERE ";

    private static final String m_orderedBy = " ORDER BY ";

    private static final String m_groupBy = " GROUP BY ";

    private static final String m_set = " SET ";

    private static StringBuffer m_fixbuf = new StringBuffer();

    private static SQLData m_instance = null;

    private Connection m_conn = null;

    private Statement m_statement = null;

    private boolean m_debug = false;

    private boolean m_needCompaction = false;

    /** Creates new SQLData */
    private SQLData() throws Exception {
        connect();
    }

    public static SQLData getInstance() throws Exception {
        if (m_instance == null) m_instance = new SQLData();
        return m_instance;
    }

    public static void release() throws SQLException {
        m_instance.close();
        m_instance = null;
    }

    public void setDebug(boolean debug) {
        m_debug = debug;
    }

    private void connect() throws Exception {
        Class.forName(m_driver).newInstance();
        String test = System.getProperty("MyTelly.SQLData.test");
        String url = (test != null && test.equals("1") ? m_urlTest : m_url);
        System.out.println("Connecting to: " + url);
        m_conn = DriverManager.getConnection(url, m_user, m_password);
        m_statement = m_conn.createStatement();
    }

    private void close() throws SQLException {
        m_conn.close();
    }

    public void compactDB() throws Exception {
        m_statement.execute("SHUTDOWN COMPACT");
        close();
        m_statement = null;
        m_conn = null;
        System.gc();
        connect();
    }

    public void dropTable(String table) throws SQLException {
        String stmnt = m_dropTable + table;
        if (m_debug) System.out.println(stmnt);
        m_statement.execute(stmnt);
    }

    public void createTable(String table, String[][] columns, String[] primaryKey) throws SQLException {
        StringBuffer stmnt = new StringBuffer(m_createTable + table);
        int colLength = columns.length;
        for (int i = 0; i < colLength; i++) {
            if (i == 0) stmnt.append('('); else stmnt.append(',');
            stmnt.append(fixColName(columns[i][0]));
            stmnt.append(' ');
            stmnt.append(columns[i][1]);
        }
        stmnt.append(m_primaryKey);
        int pkLen = primaryKey.length;
        for (int i = 0; i < pkLen; i++) {
            if (i > 0) stmnt.append(',');
            stmnt.append(fixColName(primaryKey[i]));
        }
        stmnt.append("))");
        if (m_debug) System.out.println(stmnt);
        m_statement.execute(stmnt.toString());
    }

    public void insertRow(String table, String[] cols, String[] values) throws SQLException {
        StringBuffer stmnt = new StringBuffer(m_insertRow + table);
        if (cols != null) {
            stmnt.append(" (");
            int colLen = cols.length;
            for (int i = 0; i < colLen; i++) {
                if (i > 0) stmnt.append(',');
                stmnt.append(fixColName(cols[i]));
            }
            stmnt.append(')');
        }
        stmnt.append(m_values);
        int valLength = values.length;
        for (int i = 0; i < valLength; i++) {
            if (i > 0) stmnt.append(',');
            stmnt.append(values[i]);
        }
        stmnt.append(')');
        if (m_debug) System.out.println(stmnt);
        m_statement.execute(stmnt.toString());
    }

    public void updateRow(String table, String[] cols, String[] values, String where) throws SQLException {
        StringBuffer stmnt = new StringBuffer(m_update + table + m_set);
        if (cols == null || values == null) throw new SQLException("Neither cols nor values may be null");
        if (cols.length != values.length) throw new SQLException("Number of cols must equal values");
        if (where == null || where.length() == 0) throw new SQLException("Where can't be empty");
        int colLen = cols.length;
        for (int i = 0; i < colLen; i++) {
            if (i > 0) stmnt.append(',');
            stmnt.append(fixColName(cols[i]));
            stmnt.append(" = ");
            stmnt.append(values[i]);
        }
        stmnt.append(m_where);
        stmnt.append(where);
        if (m_debug) System.out.println(stmnt);
        m_statement.execute(stmnt.toString());
    }

    public ResultSet selectRows(String table, String[] cols, String where, String orderedBy, String groupBy) throws SQLException {
        StringBuffer stmnt = new StringBuffer(m_select);
        int colLen = cols.length;
        for (int i = 0; i < colLen; i++) {
            if (i > 0) stmnt.append(',');
            stmnt.append(fixColName(cols[i], true, "()"));
        }
        stmnt.append(m_from);
        stmnt.append(table);
        if (where != null) {
            stmnt.append(m_where);
            stmnt.append(where);
        }
        if (groupBy != null) {
            stmnt.append(m_groupBy);
            stmnt.append(fixColName(groupBy, true, "(),"));
        }
        if (orderedBy != null) {
            stmnt.append(m_orderedBy);
            stmnt.append(fixColName(orderedBy));
        }
        if (stmnt.indexOf("*") < 0 && MyTellyMainFrame.getConfig().getDebug()) System.out.println("sql stmt:" + stmnt.toString());
        m_statement.execute(stmnt.toString());
        return m_statement.getResultSet();
    }

    public void deleteAll(String table) throws SQLException {
        StringBuffer stmnt = new StringBuffer(m_delete);
        stmnt.append(table);
        if (m_debug) System.out.println(stmnt);
        m_statement.execute(stmnt.toString());
    }

    public static String fixColName(String oldColName) {
        return fixColName(oldColName, false, "");
    }

    public static String fixColName(String oldColName, boolean allowDigits, String otherAlloweds) {
        if (oldColName.equals("*")) return oldColName;
        m_fixbuf.delete(0, m_fixbuf.length());
        int len = oldColName.length();
        for (int i = 0; i < len; i++) {
            char ch = oldColName.charAt(i);
            if (allowDigits && Character.isLetterOrDigit(ch)) m_fixbuf.append(ch); else if (Character.isLetter(ch)) m_fixbuf.append(ch); else if (otherAlloweds.indexOf(ch) > -1) m_fixbuf.append(ch);
        }
        return m_fixbuf.toString();
    }

    public static String fixType(String value, String colType) {
        if (colType.startsWith("VARCHAR")) {
            m_fixbuf.delete(0, m_fixbuf.length());
            int len = value.length();
            m_fixbuf.append('\'');
            for (int i = 0; i < len; i++) {
                char ch = value.charAt(i);
                if (ch == '\'') m_fixbuf.append('\'');
                m_fixbuf.append(ch);
            }
            m_fixbuf.append('\'');
            return m_fixbuf.toString();
        }
        return value;
    }
}
