package org.hsqldb.sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Title:        Testdb
 * Description:  simple hello world db example of a
 *               standalone persistent db application
 *
 *               every time it runs it adds four more rows to sample_table
 *               it does a query and prints the results to standard out
 *
 * Author: Karl Meissner karl@meissnersd.com
 */
public class Testdb {

    Connection conn;

    public Testdb(String db_file_name_prefix) throws Exception {
        Class.forName("org.hsqldb.jdbcDriver");
        conn = DriverManager.getConnection("jdbc:hsqldb:" + db_file_name_prefix, "sa", "");
    }

    public void shutdown() throws SQLException {
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

    public static void main(String[] args) {
        Testdb db = null;
        try {
            db = new Testdb("db_file");
        } catch (Exception ex1) {
            ex1.printStackTrace();
            return;
        }
        try {
            db.update("CREATE TABLE sample_table ( id INTEGER IDENTITY, str_col VARCHAR(256), num_col INTEGER)");
        } catch (SQLException ex2) {
        }
        try {
            db.update("INSERT INTO sample_table(str_col,num_col) VALUES('Ford', 100)");
            db.update("INSERT INTO sample_table(str_col,num_col) VALUES('Toyota', 200)");
            db.update("INSERT INTO sample_table(str_col,num_col) VALUES('Honda', 300)");
            db.update("INSERT INTO sample_table(str_col,num_col) VALUES('GM', 400)");
            db.query("SELECT * FROM sample_table WHERE num_col < 250");
            db.shutdown();
        } catch (SQLException ex3) {
            ex3.printStackTrace();
        }
    }
}
