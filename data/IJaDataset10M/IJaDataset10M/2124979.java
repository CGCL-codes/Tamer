package datadog;

import datadog.test.util.DbSetup;
import datadog.test.util.TrivialSetup;
import junit.framework.TestCase;
import datadog.log.Log;
import datadog.log.LogFactory;
import java.sql.*;

/**
 * Tests to explore the hsqldb installation, making sure it's working correctly.
 */
public class HsqldbTest extends TestCase {

    public static final String USER_ID_COLUMN_ALIAS = "t0.USER_ID";

    Log log = LogFactory.getLog(this.getClass());

    public void testUpdate() throws Exception {
        TrivialSetup.getSimpleUserService();
        PreparedStatement pst = getPst("UPDATE LOCATION_HRAC as t0 SET t0.DESCRIPTION = ? " + "WHERE t0.TYPE_CODE = ?");
        pst.setObject(1, "TREE OF WOE");
        pst.setObject(2, "S");
        int i = pst.executeUpdate();
        assertEquals(1, i);
        Connection conn = pst.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("select * from location_hrac");
        logResultSet(rs);
    }

    public void testFunkyUpdate() throws Exception {
        PreparedStatement pst = getPst("UPDATE LOCATION_HRAC SET DESCRIPTION = ?, TYPE_CODE = ? " + "WHERE TYPE_CODE = ?");
        pst.setObject(1, "TREE OF WOE");
        pst.setObject(2, "S");
        pst.setObject(3, "S");
        int i = pst.executeUpdate();
        assertEquals(1, i);
    }

    PreparedStatement getPst(String s) throws Exception {
        DbSetup dbSetup = DbSetup.freshDatabase();
        Connection conn = dbSetup.getConnection();
        return conn.prepareStatement(s);
    }

    void logResultSet(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        StringBuffer buff = new StringBuffer();
        buff.append("\n");
        while (rs.next()) {
            int columnCount = meta.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = meta.getColumnName(i);
                Object columnValue = rs.getObject(i);
                buff.append(columnName + "=" + columnValue);
                buff.append("\n");
            }
        }
        buff.append("\n");
        log.info(buff.toString());
    }

    public void testUserTable() throws Exception {
        DbSetup dbSetup = DbSetup.freshDatabase();
        Connection conn = dbSetup.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT t0.USER_ID AS \"t0.USER_ID\", t0.PASSWORD " + "FROM USER_TABLE AS t0 " + "WHERE t0.USER_ID = '" + DbSetup.TOBERT_USER_ID + "'");
        ResultSetMetaData rsmd = rs.getMetaData();
        assertTrue(rs.next());
        int idColumnIndex = rs.findColumn(USER_ID_COLUMN_ALIAS);
        assertEquals(rsmd.getColumnName(idColumnIndex), USER_ID_COLUMN_ALIAS);
        assertTrue(idColumnIndex >= 0);
        assertEquals(rs.getObject(idColumnIndex), DbSetup.TOBERT_USER_ID);
        int pwColumnIndex = rs.findColumn("PASSWORD");
        assertTrue(pwColumnIndex >= 0);
        assertEquals(rs.getObject(pwColumnIndex), DbSetup.TOBERT_PASSWORD);
    }

    public void testSelect() throws Exception {
        DbSetup dbSetup = DbSetup.freshDatabase();
        Connection conn = dbSetup.getConnection();
        PreparedStatement pst = conn.prepareStatement("SELECT t0.USER_ID as \"t0.USER_ID\", " + "t0.PASSWORD as \"t0.PASSWORD\" " + "FROM USER_TABLE AS t0 where t0.USER_ID = ?");
        pst.setObject(1, "TOBERT");
        ResultSet rs = pst.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        while (rs.next()) {
            log.info("\nrsmd.getColumnCount()=" + rsmd.getColumnCount());
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                log.info(rsmd.getColumnName(i) + "=" + rs.getObject(i));
            }
            log.info("VALUE FOR t0.USER_ID=" + rs.getObject("t0.USER_ID"));
        }
    }

    public void testBigSelect() throws Exception {
        DbSetup dbSetup = DbSetup.freshDatabase();
        Connection conn = dbSetup.getConnection();
        PreparedStatement pst = conn.prepareStatement("SELECT t0.PASSWORD AS \"t0.PASSWORD\", " + "t0.SSN AS \"t0.SSN\", t0.USER_ID AS \"t0.USER_ID\"," + "t1.JOB_CATEGORY AS \"t1.JOB_CATEGORY\", " + "t0.JOB_GRADE AS \"t0.JOB_GRADE\", " + "t0.HOME_LOCATION_ID AS \"t0.HOME_LOCATION_ID\" " + "FROM USER_TABLE AS t0, JOB_TABLE AS t1 " + "WHERE t0.USER_ID = ? AND t0.JOB_GRADE = t1.JOB_GRADE");
        pst.setObject(1, "TOBERT");
        ResultSet rs = pst.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        StringBuffer buff = new StringBuffer();
        buff.append("THE BIG SELECT!\n");
        while (rs.next()) {
            buff.append("\nrsmd.getColumnCount()=");
            buff.append(rsmd.getColumnCount());
            buff.append("\n");
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                buff.append(rsmd.getColumnName(i) + "=" + rs.getObject(i));
                buff.append("\n");
            }
        }
        log.info(buff.toString());
    }
}
