package com.avaje.ebean.server.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.UUID;
import com.avaje.ebean.server.core.BasicTypeConverter;

/**
 * ScalarType for java.util.UUID which converts to and from a VARCHAR database column.
 */
public class ScalarTypeUUID extends ScalarTypeBase {

    public ScalarTypeUUID() {
        super(UUID.class, false, Types.VARCHAR);
    }

    @Override
    public int getLength() {
        return 40;
    }

    public void bind(PreparedStatement pstmt, int index, Object value) throws SQLException {
        if (value == null) {
            pstmt.setNull(index, Types.VARCHAR);
        } else {
            UUID uuid = (UUID) value;
            pstmt.setString(index, uuid.toString());
        }
    }

    public Object read(ResultSet rset, int index) throws SQLException {
        String str = rset.getString(index);
        if (str == null) {
            return null;
        } else {
            return UUID.fromString(str);
        }
    }

    public Object toBeanType(Object value) {
        return BasicTypeConverter.toUUID(value);
    }

    public Object toJdbcType(Object value) {
        return BasicTypeConverter.convert(value, jdbcType);
    }
}
