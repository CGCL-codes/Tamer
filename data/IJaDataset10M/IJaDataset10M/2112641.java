package com.bugfree4j.per.test.db;

import java.sql.*;

public class TestDBConnectPostgreSQL {

    public static Connection getConnection() {
        String Driver = "org.postgresql.Driver";
        String URL = "jdbc:postgresql://128.0.0.100:5432/bugfreej";
        String user = "root";
        String password = "aaaa";
        Connection con = null;
        try {
            Class.forName(Driver);
        } catch (Exception e) {
            System.out.println("�޷����������" + Driver);
        }
        try {
            con = DriverManager.getConnection(URL, user, password);
            if (!con.isClosed()) {
                System.out.println("��ݿ����ӳɹ�!");
            }
        } catch (SQLException e) {
            System.out.println("��ݿ�����ʧ�ܣ�" + e.getMessage());
        }
        return con;
    }

    public String testConnection() {
        boolean boError = false;
        String sErrorMsg = "���ӳɹ�";
        try {
            Connection con = getConnection();
            if (!con.isClosed()) {
                System.out.println("��ݿ����ӳɹ�!");
                Statement smt = con.createStatement();
                ResultSet rst = smt.executeQuery("select * from buguser");
                while (rst.next()) {
                    System.out.println("���ֵ:" + rst.getString(1));
                }
                smt.close();
            }
            con.close();
        } catch (SQLException e) {
            System.out.println("��ݿ�ִ��ʧ�ܣ�" + e.getMessage());
            boError = true;
            sErrorMsg = e.getMessage();
        }
        if (boError == false) {
            sErrorMsg = "���ӳɹ�";
        }
        return sErrorMsg;
    }

    public static void main(String args[]) {
        TestDBConnectPostgreSQL cc = new TestDBConnectPostgreSQL();
        cc.testConnection();
    }
}
