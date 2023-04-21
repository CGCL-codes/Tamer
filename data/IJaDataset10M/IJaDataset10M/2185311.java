package com.makotan.util.db.valuetype;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.makotan.exception.NoSupportedOperationException;
import com.makotan.exception.SQLRuntimeException;

public abstract class AbstractDBValueType implements DbVaueType {

    protected abstract int getSqlType();

    public String getTypeName() {
        return null;
    }

    protected void setNull(PreparedStatement ps, int parameterIndex) {
        try {
            ps.setNull(parameterIndex, getSqlType());
        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }

    protected void setNull(CallableStatement cs, int parameterIndex) {
        try {
            cs.setNull(parameterIndex, getSqlType());
        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }

    protected void setNull(CallableStatement cs, String parameterName) {
        try {
            cs.setNull(parameterName, getSqlType());
        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }

    public <T> String getSqlLoggingString(T value) {
        if (value == null) {
            return "null";
        }
        return value.toString();
    }

    public <T> StringBuilder addSqlLoggingString(StringBuilder sb, T value) {
        if (value == null) {
            return sb.append("null");
        }
        return sb.append(value);
    }

    public String getSqlBindingString() {
        return "?";
    }

    public String getSqlBindingString(int count) {
        return getSqlBindingString(count, new StringBuilder()).toString();
    }

    protected StringBuilder getSqlBindingString(int count, StringBuilder sb) {
        String kanma = "";
        for (int i = 0; i < count; i++) {
            sb.append(kanma).append(getSqlBindingString());
            kanma = ",";
        }
        return sb;
    }

    public void getSqlBindingString(StringBuilder sb) {
        sb.append(getSqlBindingString());
    }

    public void getSqlBindingString(StringBuilder sb, int count) {
        getSqlBindingString(count, sb);
    }

    public <T> T createUniqueKey(String tableName, String fieldName, T... key) {
        throw new NoSupportedOperationException();
    }

    public <T> boolean isEffectiveUniqueKey(T key) {
        throw new NoSupportedOperationException();
    }
}
