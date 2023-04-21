package org.hsqldb.jdbc;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.hsqldb.jdbc.testbase.BaseClobTestCase;
import org.hsqldb.testbase.ForSubject;

/**
 * Test of class org.hsqldb.jdbc.jdbcClobClient.
 *
 * @author Campbell Boucher-Burnet (boucherb@users dot sourceforge.net)
 */
@ForSubject(JDBCClobClient.class)
public class JDBCClobClientTest extends BaseClobTestCase {

    public JDBCClobClientTest(String testName) {
        super(testName);
    }

    protected Clob handleCreateClob() throws Exception {
        Connection conn = newConnection();
        Statement stmt = connectionFactory().createStatement(conn);
        stmt.execute("drop table clob_client_test if exists");
        stmt.execute("create table clob_client_test(id int, clob_value clob)");
        stmt.execute("insert into clob_client_test(id ,clob_value) values(1, null)");
        Clob blob = conn.createClob();
        PreparedStatement pstmt = connectionFactory().prepareStatement("update clob_client_test set clob_value = ?", conn);
        pstmt.setClob(1, blob);
        pstmt.execute();
        ResultSet rs = stmt.executeQuery("select clob_value from clob_client_test where id = 1 for update");
        rs.next();
        return rs.getClob(1);
    }

    public static Test suite() {
        return new TestSuite(JDBCClobClientTest.class);
    }

    public static void main(java.lang.String[] argList) {
        junit.textui.TestRunner.run(suite());
    }
}
