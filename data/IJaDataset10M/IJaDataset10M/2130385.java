package com.yeep.objanalyser.common.dao.ibatis.util;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.sql.PreparedStatement;
import org.apache.commons.lang.StringUtils;

public class IbatisUtil {

    /**
	 * get the index for a column in INSERT or UPDATE SQL
	 * @param preparedStatment String 
	 * @param columnName column name
	 * @return column index in INSERT or UPDATE SQL
	 */
    public static int getColumnIndex(String preparedStatement, String columnName) {
        String sql = preparedStatement.toLowerCase();
        if (sql.contains("insert ")) {
            String columnPart = sql.substring(0, sql.indexOf(columnName));
            int index = StringUtils.countMatches(columnPart, ",");
            String valuePart = sql.substring(sql.indexOf(")"), sql.length());
            int columnIndex = 0;
            for (int i = 0; i < index; i++) {
                String temp = valuePart.substring(0, valuePart.indexOf(","));
                valuePart = valuePart.substring(valuePart.indexOf(",") + 1, valuePart.length());
                if (temp.contains("?")) {
                    columnIndex++;
                }
            }
            return columnIndex + 1;
        } else {
            String columnPart = sql.substring(0, sql.indexOf(columnName));
            return StringUtils.countMatches(columnPart, "?") + 1;
        }
    }

    public static String getSql(PreparedStatement ps) {
        String sql = null;
        try {
            if (ps instanceof Proxy) {
                Object obj = Proxy.getInvocationHandler(ps);
                Field field = obj.getClass().getDeclaredField("sql");
                field.setAccessible(true);
                sql = (String) field.get(obj);
            } else {
                Field field = null;
                try {
                    field = ps.getClass().getSuperclass().getSuperclass().getDeclaredField("sql");
                } catch (Exception ex1) {
                    field = ps.getClass().getDeclaredField("sql");
                }
                field.setAccessible(true);
                sql = (String) field.get(ps);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sql;
    }
}
