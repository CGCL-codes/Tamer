package org.jtools.shovel.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import org.jtools.shovel.test.Database;
import org.jtools.shovel.test.SQLTestUtils;
import org.jtools.sql.ConnectionFactory;
import org.jtools.sql.SQLConnection;
import org.jtools.sql.SQLUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class SQLShovelTestExt {

    static {
        SQLTestUtils.register();
    }

    private static final boolean printOut = false;

    private void check(boolean out, String t, ConnectionFactory conn, String solltablename, String isttablename) throws SQLException {
        Connection istconn = conn.newConnection();
        Connection sollconn = conn.newConnection();
        try {
            Statement iststmt = istconn.createStatement();
            Statement sollstmt = sollconn.createStatement();
            try {
                ResultSet istrs = iststmt.executeQuery("select * from " + isttablename + " order by 1");
                ResultSet sollrs = sollstmt.executeQuery("select * from " + solltablename + " order by 1");
                try {
                    int r = 0;
                    boolean nextist;
                    boolean nextsoll;
                    ResultSetMetaData rsmist = istrs.getMetaData();
                    ResultSetMetaData rsmsoll = sollrs.getMetaData();
                    if (out) System.out.println(t + "/cols: soll=" + rsmsoll.getColumnCount() + "; ist=" + rsmist.getColumnCount());
                    assertEquals(t + "/cols", rsmsoll.getColumnCount(), rsmist.getColumnCount());
                    while (true) {
                        r++;
                        nextist = istrs.next();
                        nextsoll = sollrs.next();
                        if (out) System.out.println(t + "/r" + r + ": soll=" + nextsoll + "; ist=" + nextist);
                        assertEquals(t + "/r" + r, nextsoll, nextist);
                        if (!(nextist && nextsoll)) return;
                        for (int c = 1; c <= rsmsoll.getColumnCount(); c++) {
                            Object valsoll = sollrs.getObject(c);
                            Object valist = istrs.getObject(c);
                            if (out) System.out.println(t + "/r" + r + "c" + c + ": soll=" + valsoll + "; ist=" + valist);
                            assertEquals(t + "/r" + r + "c" + c, valsoll, valist);
                        }
                    }
                } finally {
                    SQLUtils.close(istrs);
                    SQLUtils.close(sollrs);
                }
            } finally {
                SQLUtils.close(iststmt);
                SQLUtils.close(sollstmt);
            }
        } finally {
            SQLUtils.close(istconn);
            SQLUtils.close(sollconn);
        }
    }

    @Test
    public void special() throws SQLException {
        String testName = "sqlSpecial";
        SQLConnection conn = new SQLConnection();
        conn.setDriver("org.apache.derby.jdbc.EmbeddedDriver");
        conn.setUrl(Database.url);
        check(printOut, testName, conn, Database.solltablename, Database.isttablename);
    }
}
