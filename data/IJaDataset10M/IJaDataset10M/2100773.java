package f8ql.jdbc;

import java.sql.SQLException;
import java.util.Map;

class Array implements java.sql.Array {

    public Object getArray() throws SQLException {
        return this;
    }

    public Object getArray(Map map) throws SQLException {
        return null;
    }

    public Object getArray(long l, int i) throws SQLException {
        return null;
    }

    public Object getArray(long l, int i, Map map) throws SQLException {
        return null;
    }

    public int getBaseType() throws SQLException {
        return 0;
    }

    public String getBaseTypeName() throws SQLException {
        return null;
    }

    public java.sql.ResultSet getResultSet() throws SQLException {
        return null;
    }

    public java.sql.ResultSet getResultSet(Map map) throws SQLException {
        return null;
    }

    public java.sql.ResultSet getResultSet(long l, int i) throws SQLException {
        return null;
    }

    public java.sql.ResultSet getResultSet(long l, int i, Map map) throws SQLException {
        return null;
    }
}
