package com.lus.sso.handlers;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.dbutils.ResultSetHandler;

public class UserDataResultSetHandler implements ResultSetHandler {

    public Object handle(ResultSet rs) throws SQLException {
        if (rs.next()) {
            Map<String, String> map = new HashMap<String, String>();
            ResultSetMetaData rsmd = rs.getMetaData();
            for (int i = 0; i < rsmd.getColumnCount(); i++) {
                map.put(rsmd.getColumnLabel(i + 1).toLowerCase(), (rs.getString(i + 1) != null ? rs.getString(i + 1) : ""));
            }
            return map;
        }
        return null;
    }
}
