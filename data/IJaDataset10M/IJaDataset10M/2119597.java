package org.portablerule.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.portablerule.client.PortableRuleDB;
import org.portablerule.service.PortableRuleDBProvider;
import org.portablerule.service.RuleData;

/**
 * @author Colin Zhao
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestPortable {

    static final String connect_string = "jdbc:oracle:thin:@192.168.0.100:1521:devenv";

    static final String driver_class = "oracle.jdbc.driver.OracleDriver";

    public static void main(String[] args) throws Exception {
        Class.forName(driver_class);
        Connection conn = DriverManager.getConnection(connect_string, "scott", "tiger");
        System.out.println("Oracle Connection created: " + conn);
        System.out.println("Total memory: " + Runtime.getRuntime().totalMemory() + " " + Runtime.getRuntime().maxMemory() + " " + Runtime.getRuntime().freeMemory());
        String[] tables = { "PRODUCT", "SALESCHANNELPRODUCT", "SALESCHANNEL" };
        PortableRuleDBProvider provider = new PortableRuleDBProvider();
        provider.setConnection(conn);
        byte[] ruleData = provider.getRuleData("EVALUATION", tables);
        PortableRuleDB db = new PortableRuleDB("Test", "EVALUATION");
        db.loadRules(ruleData);
        System.out.println("Total memory: " + Runtime.getRuntime().totalMemory() + " " + Runtime.getRuntime().maxMemory() + " " + Runtime.getRuntime().freeMemory());
        Runtime.getRuntime().gc();
        test(db, conn, 2);
        test(db, conn, 5);
        test(db, conn, 10);
        test(db, conn, 15);
        test(db, conn, 20);
        test(db, conn, 30);
        test(db, conn, 31);
        test(db, conn, 32);
        Runtime.getRuntime().gc();
        System.out.println("Total memory: " + Runtime.getRuntime().totalMemory() + " " + Runtime.getRuntime().maxMemory() + " " + Runtime.getRuntime().freeMemory());
    }

    static void test(PortableRuleDB ruleDB, Connection remoteConn, long id) throws SQLException {
        ResultSet rs = null;
        Connection dbConn = ruleDB.getConnection();
        Statement stmt = dbConn.createStatement();
        stmt.setFetchSize(500);
        String prefix = ruleDB.getSchemaName() + ".";
        prefix = "";
        int count = 0;
        rs = stmt.executeQuery("select * from SALESCHANNEL ");
        while (rs.next()) {
            long cid = rs.getLong(1);
            String cname = rs.getString(2);
        }
        rs.close();
        long t1 = System.currentTimeMillis();
        rs = stmt.executeQuery("SELECT * FROM " + prefix + "product t1, " + prefix + "SALESCHANNELPRODUCT t2 WHERE t1.id=t2.productId" + " and channelid=" + id + " order by t2.productid");
        while (rs.next()) {
            count++;
            fetch(rs);
        }
        long t2 = System.currentTimeMillis();
        long lt = t2 - t1;
        stmt.close();
        rs.close();
        count = 0;
        t1 = System.currentTimeMillis();
        stmt = remoteConn.createStatement();
        prefix = "EVALUATION.";
        rs = stmt.executeQuery("SELECT * FROM " + prefix + "product t1, " + prefix + "SALESCHANNELPRODUCT t2 WHERE t1.id=t2.productId" + " and channelid=" + id + " order by t2.productid");
        while (rs.next()) {
            count++;
            fetch(rs);
        }
        t2 = System.currentTimeMillis();
        System.out.println("query time of " + count + " :     " + (t2 - t1) + " " + lt);
        stmt.close();
        rs.close();
    }

    private static void fetch(ResultSet rs) throws SQLException {
        rs.getLong(1);
        rs.getString(2);
        rs.getDate(3);
        rs.getString(4);
        rs.getBigDecimal(5);
        rs.getInt(6);
        rs.getFloat(7);
        rs.getLong(8);
        rs.getLong(9);
        rs.getBigDecimal(10);
    }
}
