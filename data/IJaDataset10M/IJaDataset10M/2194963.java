package org.lokee.punchcard.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import junit.framework.TestCase;
import org.lokee.punchcard.PunchCardManagerFactory;
import org.lokee.punchcard.persistence.jdbc.connection.ConnectionFactory;
import org.lokee.punchcard.persistence.jdbc.connection.ConnectionUtil;

/**
 * @author CLaguerre
 *
 */
public class PersistenceUtilTest extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(PersistenceUtilTest.class);
    }

    public void teistFetchPersistenceConfig() {
        PunchCardManagerFactory.initializePunchCard("properties/punchcard");
        String driverClasses[] = ConnectionUtil.fetchAllDriverClassNames();
        for (int i = 0; i < driverClasses.length; i++) {
            System.out.println("Driver Classes:" + driverClasses[i]);
        }
        System.out.println("Global Connection Properties:" + ConnectionUtil.fetchGlobalConnectionConfig());
        System.out.println("Connection Properties:" + ConnectionUtil.fetchConnectionConfig("ods"));
    }

    public void testPopulateCardWithMetaData() {
        PunchCardManagerFactory.initializePunchCard("properties/punchcard");
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection("punchcard_examples");
            ResultSet rs = conn.getMetaData().getExportedKeys(conn.getCatalog(), "edbm_examples", "name_test");
            System.out.println(conn.getCatalog());
            System.out.println(rs.getMetaData().getColumnCount());
            System.out.println(rs.getMetaData().getColumnName(1));
            while (rs.next()) {
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    System.out.println(rs.getMetaData().getColumnName(i) + ":(" + rs.getString(i) + ")");
                }
            }
            rs = conn.getMetaData().getPrimaryKeys("edbm_examples", "edbm_examples", "name_test");
            System.out.println("\n\n\n");
            while (rs.next()) {
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    System.out.println(rs.getMetaData().getColumnName(i) + ":(" + rs.getString(i) + ")");
                }
            }
            rs = conn.getMetaData().getColumns("edbm_examples", "edbm_examples", "company", "");
            System.out.println("\n\n\n");
            while (rs.next()) {
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    System.out.println(rs.getMetaData().getColumnName(i) + ":(" + rs.getString(i) + ")");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionUtil.closeConnection(conn);
        }
    }
}
