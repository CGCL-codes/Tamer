package org.avaje.ebean.server.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.avaje.ebean.util.BasicTypeConverter;

/**
 * ScalarType for Short and short.
 */
public class ScalarTypeShort extends ScalarTypeBase {

    public ScalarTypeShort() {
        super(Short.class, true, Types.SMALLINT);
    }

    public void bind(PreparedStatement pstmt, int index, Object value) throws SQLException {
        if (value == null) {
            pstmt.setNull(index, Types.SMALLINT);
        } else {
            pstmt.setShort(index, ((Short) value).shortValue());
        }
    }

    public Object read(ResultSet rset, int index) throws SQLException {
        short i = rset.getShort(index);
        if (rset.wasNull()) {
            return null;
        }
        return i;
    }

    public Object toJdbcType(Object value) {
        return BasicTypeConverter.toShort(value);
    }

    public Object toBeanType(Object value) {
        return BasicTypeConverter.toShort(value);
    }
}
