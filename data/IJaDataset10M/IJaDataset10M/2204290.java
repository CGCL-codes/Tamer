package org.xeustechnologies.bottomline;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class Test {

    public void testBottomline() throws Exception {
        Class.forName("xeus.bottomline.BottomlineDriver", true, Thread.currentThread().getContextClassLoader());
        Properties props = new Properties();
        props.put("user", "root");
        props.put("password", "root");
        props.put("jar", "D:\\software\\stuff 4\\mysql-connector-java-5.0.3\\" + "mysql-connector-java-5.0.3\\mysql-connector-java-5.0.3-bin.jar");
        props.put("class", "com.mysql.jdbc.Driver");
        String jar = "D:\\software\\stuff 4\\mysql-connector-java-5.0.3\\" + "mysql-connector-java-5.0.3\\mysql-connector-java-5.0.3-bin.jar";
        Connection conn = DriverManager.getConnection("jdbc:bottomline:mysql://localhost:3306/timesheet?user=root&password=root&jar=" + jar + "&class=com.mysql.jdbc.Driver");
        Statement s = conn.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM pts_worker");
        while (rs.next()) System.out.println(rs.getString("wname"));
        rs.close();
        s.close();
        Properties props1 = new Properties();
        props1.put("user", "root");
        props1.put("password", "root");
        props1.put("class", "com.mysql.jdbc.Driver");
        props1.put("jar", "D:\\software\\mysql-connector-java-2.0.14\\mysql-connector-java-2.0.14\\" + "mysql-connector-java-2.0.14-bin.jar");
        try {
            Connection conn1 = DriverManager.getConnection("jdbc:bottomline:mysql://localhost:3306/timesheet", props1);
            Statement s1 = conn1.createStatement();
            ResultSet rs1 = s1.executeQuery("SELECT * FROM pts_worker");
            while (rs1.next()) System.out.println(rs1.getString("wname"));
            rs1.close();
            s1.close();
            conn1.close();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        s = conn.createStatement();
        rs = s.executeQuery("SELECT * FROM pts_worker");
        while (rs.next()) System.out.println(rs.getString("wname"));
        rs.close();
        s.close();
        conn.close();
    }
}
