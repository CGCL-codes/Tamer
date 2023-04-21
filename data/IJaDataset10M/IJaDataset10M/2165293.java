package com.avaje.ebean.server.type;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import com.avaje.ebean.server.core.BasicTypeConverter;

/**
 * ScalarType for java.math.BigInteger.
 */
public class ScalarTypeMathBigInteger extends ScalarTypeBase {

    public ScalarTypeMathBigInteger() {
        super(BigInteger.class, false, Types.BIGINT);
    }

    public void bind(PreparedStatement pstmt, int index, Object value) throws SQLException {
        if (value == null) {
            pstmt.setNull(index, Types.BIGINT);
        } else {
            BigInteger bigInt = (BigInteger) value;
            pstmt.setLong(index, bigInt.longValue());
        }
    }

    public Object read(ResultSet rset, int index) throws SQLException {
        long l = rset.getLong(index);
        if (rset.wasNull()) {
            return null;
        }
        return new BigInteger(String.valueOf(l));
    }

    public Object toJdbcType(Object value) {
        return BasicTypeConverter.toLong(value);
    }

    public Object toBeanType(Object value) {
        return BasicTypeConverter.toMathBigInteger(value);
    }
}
