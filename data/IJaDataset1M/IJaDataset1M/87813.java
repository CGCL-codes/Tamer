package net.sourceforge.smokestack.jdbc.ex01;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * A very simple class that shows how to load the driver, create a database,
 * create a table, and insert some data.
 */
public class HelloWorld {

    /**
     * Called when ran from command line.
     *
     * @param args ignored
     */
    public static void main(String... args) throws Exception {
        Class.forName("org.hsqldb.jdbcDriver");
        Connection conn = DriverManager.getConnection("jdbc:hsqldb:mem:inmemory");
        Statement stat = conn.createStatement();
        stat.execute("create table message(id int primary key, message varchar(255))");
        stat.execute("insert into message values(1, 'Hello World')");
        ResultSet rs;
        rs = stat.executeQuery("select * from message");
        while (rs.next()) {
            System.out.println(rs.getString("message"));
        }
        rs.close();
        stat.close();
        conn.close();
    }
}
