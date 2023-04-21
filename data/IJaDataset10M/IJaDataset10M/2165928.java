package com.makotan.util.db.valuetype;

import java.io.Reader;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.makotan.exception.SQLRuntimeException;
import com.makotan.util.GenericsUtil;

public class DbCharacterStreamValueType extends AbstractDBValueType {

    @Override
    protected int getSqlType() {
        return java.sql.Types.LONGVARCHAR;
    }

    public Class<?> getTargetClass() {
        return Reader.class;
    }

    public <T> T getValue(ResultSet rs, String columnLabel, T... key) {
        try {
            return GenericsUtil.cast(rs.getCharacterStream(columnLabel));
        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }

    public <T> T getValue(ResultSet rs, int columnIndex, T... key) {
        try {
            return GenericsUtil.cast(rs.getCharacterStream(columnIndex));
        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }

    public <T> T getValue(CallableStatement cs, String columnLabel, T... key) {
        try {
            return GenericsUtil.cast(cs.getCharacterStream(columnLabel));
        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }

    public <T> T getValue(CallableStatement cs, int columnIndex, T... key) {
        try {
            return GenericsUtil.cast(cs.getCharacterStream(columnIndex));
        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }

    public <T> void setValue(PreparedStatement ps, int parameterIndex, T value) {
        if (value == null) {
            setNull(ps, parameterIndex);
            return;
        }
        try {
            ps.setCharacterStream(parameterIndex, (Reader) value);
        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }

    public <T> void setValue(CallableStatement cs, int parameterIndex, T value) {
        if (value == null) {
            setNull(cs, parameterIndex);
            return;
        }
        try {
            cs.setCharacterStream(parameterIndex, (Reader) value);
        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }

    public <T> void setValue(CallableStatement cs, String parameterName, T value) {
        if (value == null) {
            setNull(cs, parameterName);
            return;
        }
        try {
            cs.setCharacterStream(parameterName, (Reader) value);
        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }

    public <T> String getSqlLoggingString(T value) {
        return "(CharacterStream)";
    }

    public <T> StringBuilder addSqlLoggingString(StringBuilder sb, T value) {
        return sb.append("(CharacterStream)");
    }
}
