package com.zhongkai.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public abstract class BaseJDBCDao extends JdbcDaoSupport {

    public ArrayList select(String sql) throws Exception {
        Connection conn = null;
        try {
            conn = this.getConnection();
            Statement stmt = conn.createStatement();
            java.sql.ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            int mc = meta.getColumnCount();
            ArrayList list = new ArrayList();
            while (rs.next()) {
                HashMap map = new HashMap();
                String tempValue;
                String columnName;
                for (int i = 1; i <= mc; i++) {
                    tempValue = rs.getString(i) == null ? "" : rs.getString(i);
                    columnName = meta.getColumnName(i);
                    columnName = columnName.toLowerCase();
                    map.put(columnName, tempValue);
                }
                list.add(map);
            }
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            try {
                if (conn != null && !conn.isClosed()) conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public int count(String tableName, String sqlWhere) throws Exception {
        int count = 0;
        String sql = "select count(*) from " + tableName;
        if (sqlWhere != null && !sqlWhere.matches("\\s*")) sql = sql + sqlWhere;
        Connection conn = null;
        try {
            conn = this.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } finally {
            try {
                if (conn != null && !conn.isClosed()) conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return count;
    }
}
