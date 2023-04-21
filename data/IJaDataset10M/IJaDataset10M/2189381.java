package com.google.code.nanorm.internal.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import com.google.code.nanorm.TypeHandler;

/**
 * Type handler for {@link String}.
 * 
 * @author Ivan Dubrov
 * @version 1.0 31.05.2008
 */
public class StringTypeHandler implements TypeHandler<String> {

    /**
     * {@inheritDoc}
     */
    public String getValue(ResultSet rs, int column) throws SQLException {
        return rs.getString(column);
    }

    /**
     * {@inheritDoc}
     */
    public String getValue(ResultSet rs, String column) throws SQLException {
        return rs.getString(column);
    }

    /**
     * {@inheritDoc}
     */
    public String getValue(CallableStatement cs, int index) throws SQLException {
        return cs.getString(index);
    }

    /**
     * {@inheritDoc}
     */
    public void setParameter(PreparedStatement st, int column, Object value) throws SQLException {
        if (value == null) {
            st.setNull(column, Types.VARCHAR);
        } else {
            st.setString(column, (String) value);
        }
    }

    /**
     * {@inheritDoc}
     */
    public int getSqlType() {
        return Types.VARCHAR;
    }
}
