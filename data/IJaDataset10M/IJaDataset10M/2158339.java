package org.avaje.ebean.server.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.avaje.ebean.util.BasicTypeConverter;

/**
 * ScalarType for Float and float.
 */
public class ScalarTypeFloat extends ScalarTypeBase {

    public ScalarTypeFloat() {
        super(Float.class, true, Types.REAL);
    }

    public void bind(PreparedStatement pstmt, int index, Object value) throws SQLException {
        if (value == null) {
            pstmt.setNull(index, Types.REAL);
        } else {
            pstmt.setFloat(index, ((Float) value).floatValue());
        }
    }

    public Object read(ResultSet rset, int index) throws SQLException {
        float i = rset.getFloat(index);
        if (rset.wasNull()) {
            return null;
        }
        return i;
    }

    public Object toJdbcType(Object value) {
        return BasicTypeConverter.toFloat(value);
    }

    public Object toBeanType(Object value) {
        return BasicTypeConverter.toFloat(value);
    }
}
