package org.t2framework.daisy.core.types;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.t2framework.commons.util.ConverterUtil;

public class CharacterType extends AbstractValueType {

    public CharacterType() {
        super(Types.CHAR);
    }

    public Object getValue(ResultSet resultSet, int index) throws SQLException {
        return toCharacter(resultSet.getString(index));
    }

    public Object getValue(ResultSet resultSet, String columnName) throws SQLException {
        return toCharacter(resultSet.getString(columnName));
    }

    public Object getValue(CallableStatement cs, int index) throws SQLException {
        return toCharacter(cs.getString(index));
    }

    public Object getValue(CallableStatement cs, String parameterName) throws SQLException {
        return toCharacter(cs.getString(parameterName));
    }

    private Character toCharacter(final String value) {
        if (value == null) {
            return null;
        }
        final char[] chars = value.toCharArray();
        if (chars.length == 1) {
            return new Character(chars[0]);
        }
        if (chars.length == 0) {
            return null;
        }
        throw new IllegalStateException("length of String should be 1." + " actual is [" + value + "]");
    }

    public void bindValue(PreparedStatement ps, int index, Object value) throws SQLException {
        if (value == null) {
            setNull(ps, index);
        } else {
            ps.setString(index, ConverterUtil.convertAsString(value));
        }
    }

    public void bindValue(CallableStatement cs, String parameterName, Object value) throws SQLException {
        if (value == null) {
            setNull(cs, parameterName);
        } else {
            cs.setString(parameterName, ConverterUtil.convertAsString(value));
        }
    }
}
