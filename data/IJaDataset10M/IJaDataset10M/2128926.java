package org.t2framework.daisy.core.types;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class NullType implements ValueType {

    public void bindValue(CallableStatement cs, String parameterName, Object value) throws SQLException {
        throw new SQLException("not supported");
    }

    public void bindValue(PreparedStatement ps, int index, Object value) throws SQLException {
        throw new SQLException("not supported");
    }

    public Object getValue(CallableStatement cs, int index) throws SQLException {
        throw new SQLException("not supported");
    }

    public Object getValue(CallableStatement cs, String parameterName) throws SQLException {
        throw new SQLException("not supported");
    }

    public Object getValue(ResultSet resultSet, int index) throws SQLException {
        throw new SQLException("not supported");
    }

    public Object getValue(ResultSet resultSet, String columnName) throws SQLException {
        throw new SQLException("not supported");
    }

    public void registerOutParameter(CallableStatement cs, int index) throws SQLException {
        throw new SQLException("not supported");
    }

    public void registerOutParameter(CallableStatement cs, String parameterName) throws SQLException {
        throw new SQLException("not supported");
    }

    public String toText(Object value) {
        throw new UnsupportedOperationException("toText");
    }

    public int getSqlType() {
        return Types.NULL;
    }
}
