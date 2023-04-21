package com.versant.core.jdbc.sql.conv;

import com.versant.core.jdbc.JdbcConverter;
import com.versant.core.jdbc.JdbcConverterFactory;
import com.versant.core.jdbc.JdbcTypeRegistry;
import com.versant.core.jdbc.metadata.JdbcColumn;
import javax.jdo.JDOFatalDataStoreException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.math.BigInteger;

/**
 * BigInteger converter for Interbase and Firebird.
 */
public class BigIntegerConverter extends JdbcConverterBase {

    public static class Factory extends NoArgJdbcConverterFactory {

        private BigIntegerConverter converter;

        /**
         * Create a converter for col using props as parameters. Return null if
         * no converter is required.
         */
        public JdbcConverter createJdbcConverter(JdbcColumn col, Object args, JdbcTypeRegistry jdbcTypeRegistry) {
            if (converter == null) converter = new BigIntegerConverter();
            return converter;
        }
    }

    /**
     * Get the value of col from rs at position index.
     * @exception java.sql.SQLException on SQL errors
     * @exception javax.jdo.JDOFatalDataStoreException if the ResultSet value is invalid
     */
    public Object get(ResultSet rs, int index, JdbcColumn col) throws SQLException, JDOFatalDataStoreException {
        String d = rs.getString(index);
        if (rs.wasNull()) return null;
        return new BigInteger(d);
    }

    /**
     * Set parameter index on ps to value (for col).
     * @exception java.sql.SQLException on SQL errors
     * @exception javax.jdo.JDOFatalDataStoreException if value is invalid
     */
    public void set(PreparedStatement ps, int index, JdbcColumn col, Object value) throws SQLException, JDOFatalDataStoreException {
        if (value == null) {
            ps.setNull(index, col.jdbcType);
        } else {
            BigInteger bigInteger = (BigInteger) value;
            ps.setDouble(index, bigInteger.doubleValue());
        }
    }

    /**
     * Get the type of our expected value objects (e.g. java.util.Locale
     * for a converter for Locale's).
     */
    public Class getValueType() {
        return BigInteger.class;
    }
}
