package com.wondersgroup.award;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MergeExcel {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        String driverName = "sun.jdbc.odbc.JdbcOdbcDriver";
        String dbURL2010 = "jdbc:odbc:driver={Microsoft Excel Driver (*.xls)};DBQ=D:\\2010数据.xls";
        String sql2010 = "select 仪器ID from [2010数据$]";
        String sql2009 = "select * from [2009数据$] where 仪器ID=?";
        String sqlUpdate = "update [2010数据$] set 机时1=?, 样品1=?, 收入1=?, 最终奖励金额1=? where 仪器ID=?";
        Connection conn = null;
        Class.forName(driverName);
        conn = DriverManager.getConnection(dbURL2010, "", "");
        PreparedStatement ps2010 = conn.prepareStatement(sql2010);
        PreparedStatement ps2009 = conn.prepareStatement(sql2009);
        PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);
        ResultSet rs2010 = ps2010.executeQuery();
        while (rs2010.next()) {
            ps2009.setString(1, rs2010.getString(1));
            ResultSet rs2009 = ps2009.executeQuery();
            if (rs2009.next()) {
                psUpdate.setString(1, rs2009.getString(2));
                psUpdate.setString(2, rs2009.getString(3));
                psUpdate.setString(3, rs2009.getString(4));
                psUpdate.setString(4, rs2009.getString(5));
                psUpdate.setString(5, rs2009.getString(1));
                psUpdate.executeUpdate();
            }
        }
        conn.close();
    }
}
