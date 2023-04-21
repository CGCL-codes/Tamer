package com.jbk.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class Dbutil {

    public static Connection getConnection(String hostip, String databaseName, String username, String pwd) {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://" + hostip + ":3306/" + databaseName + "?characterEncoding=UTF-8";
            conn = DriverManager.getConnection(url, username, pwd);
            if (conn != null) {
                System.out.println("获取连接成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}
