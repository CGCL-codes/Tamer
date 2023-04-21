package org.t2framework.daisy.core.types;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.t2framework.commons.util.ConverterUtil;

public class ByteType extends AbstractValueType {

    public ByteType() {
        super(Types.SMALLINT);
    }

    public Object getValue(final ResultSet resultSet, final int index) throws SQLException {
        return ConverterUtil.convertAsByte(resultSet.getObject(index));
    }

    public Object getValue(final ResultSet resultSet, final String columnName) throws SQLException {
        return ConverterUtil.convertAsByte(resultSet.getObject(columnName));
    }

    public Object getValue(final CallableStatement cs, final int index) throws SQLException {
        return ConverterUtil.convertAsByte(cs.getObject(index));
    }

    public Object getValue(final CallableStatement cs, final String parameterName) throws SQLException {
        return ConverterUtil.convertAsByte(cs.getObject(parameterName));
    }

    public void bindValue(final PreparedStatement ps, final int index, final Object value) throws SQLException {
        if (value == null) {
            setNull(ps, index);
        } else {
            ps.setByte(index, ConverterUtil.convertAsPrimitiveByte(value));
        }
    }

    public void bindValue(final CallableStatement cs, final String parameterName, final Object value) throws SQLException {
        if (value == null) {
            setNull(cs, parameterName);
        } else {
            cs.setByte(parameterName, ConverterUtil.convertAsPrimitiveByte(value));
        }
    }
}
