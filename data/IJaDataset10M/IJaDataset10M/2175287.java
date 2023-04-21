package com.htwg.routingengine.samples.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import com.htwg.routingengine.basis.Basis;
import com.htwg.routingengine.framework.RoutingEngine;

public class DatabaseManagerDemo extends Basis {

    public DatabaseManagerDemo(RoutingEngine re, String dbFileName) {
        routingEngine = re;
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            String db_file_name_prefix = new String("./db");
            db_file_name_prefix = db_file_name_prefix.concat("/");
            db_file_name_prefix = db_file_name_prefix.concat(dbFileName);
            conn = DriverManager.getConnection("jdbc:hsqldb:" + db_file_name_prefix, "sa", "");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void shutdownDb() throws SQLException {
        Statement st = conn.createStatement();
        st.execute("SHUTDOWN");
        conn.close();
    }

    public synchronized void query(String expression) throws SQLException {
        Statement st = null;
        ResultSet rs = null;
        st = conn.createStatement();
        rs = st.executeQuery(expression);
        dump(rs);
        st.close();
    }

    public synchronized void update(String expression) throws SQLException {
        Statement st = null;
        st = conn.createStatement();
        int i = st.executeUpdate(expression);
        if (i == -1) {
            System.out.println("db error : " + expression);
        }
        st.close();
    }

    public static void dump(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int colmax = meta.getColumnCount();
        int i;
        Object o = null;
        for (; rs.next(); ) {
            for (i = 0; i < colmax; ++i) {
                o = rs.getObject(i + 1);
                System.out.print(o.toString() + " ");
            }
            System.out.println(" ");
        }
    }

    Connection conn;

    private RoutingEngine routingEngine;
}
