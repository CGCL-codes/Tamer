package com.spaceprogram.sql.postgres;

import com.crossdb.sql.*;

public class PostgresUpdateQuery extends DefaultUpdateQuery implements UpdateQuery {

    public PostgresUpdateQuery() {
        super();
    }

    public String toString() {
        SQLDateTimeFormat sqldf = new SQLDateTimeFormat();
        String query2 = "UPDATE " + table + " SET ";
        for (int m = 0; m < columns.size(); m++) {
            ColumnValue col = (ColumnValue) (columns.get(m));
            Object val = col.getValue();
            String in_val;
            if (val == null) {
                in_val = null;
            } else if (val instanceof String) {
                if (col.isNoAlter()) {
                    in_val = (String) val;
                } else {
                    in_val = "'" + SQLFormat.escape((String) val) + "'";
                }
            } else if (val instanceof java.util.Date) {
                in_val = "'" + sqldf.format(val) + "'";
            } else if (val instanceof Boolean) {
                Boolean b = (Boolean) val;
                if (b.booleanValue()) {
                    in_val = "1";
                } else in_val = "0";
            } else {
                in_val = val.toString();
            }
            query2 += col.getName() + " = " + in_val + ",";
        }
        query2 = query2.substring(0, query2.length() - 1);
        if (wclause.hasConditions()) {
            query2 += " WHERE ";
            query2 += wclause.toString();
        }
        return query2;
    }
}
