package org.jiql.db.jdbc.stat;

import java.util.*;
import java.sql.*;
import java.io.*;
import org.jiql.util.JGNameValuePairs;
import tools.util.EZArrayList;
import tools.util.StringUtil;
import org.jiql.util.Criteria;
import org.jiql.util.SQLParser;
import org.jiql.util.Gateway;
import org.jiql.db.objs.jiqlCellValue;
import tools.util.NameValuePairs;
import org.jiql.util.JGException;
import org.jiql.util.JGUtil;
import org.jiql.db.Row;

public class SQLDumpResultMetaObj extends ResultObj implements Serializable {

    String cn = "INSERT";

    int type = Types.VARCHAR;

    String typeName = "VARCHAR";

    Vector<Row> rows = null;

    public SQLDumpResultMetaObj(org.jiql.jdbc.ResultSet r) {
        super(r);
    }

    public String getColumnName(int column) throws SQLException {
        return cn;
    }

    public int getColumnType(int column) throws SQLException {
        return type;
    }

    public String getColumnTypeName(int column) throws SQLException {
        return typeName;
    }

    public int getColumnCount() throws SQLException {
        return 1;
    }

    public int findColumn(String col) throws SQLException {
        return 1;
    }

    public Object getValue(int indx, String col) throws SQLException {
        return rows.elementAt(indx - 1).get("INSERT");
    }

    public int size() throws SQLException {
        return rows.size();
    }

    public void setRows(Vector<Row> s) {
        rows = s;
    }
}
