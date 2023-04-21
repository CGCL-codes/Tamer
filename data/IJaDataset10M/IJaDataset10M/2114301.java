package com.versant.core.jdbc.sql.conv;

import com.versant.core.jdbc.JdbcConverter;
import com.versant.core.jdbc.JdbcTypeRegistry;
import com.versant.core.jdbc.metadata.JdbcColumn;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import javax.jdo.JDOFatalDataStoreException;
import com.versant.core.common.BindingSupportImpl;

/**
 * This converter throws a JDODatastoreException with a "not supported"
 * message if its methods are invoked. This is used for databases that
 * do not support certain data types e.g Informix SE does not support BLOBS
 * and TEXT columns at all.
 * @keep-all
 */
public class NotSupportedConverter extends JdbcConverterBase {

    public static class Factory extends NoArgJdbcConverterFactory {

        private NotSupportedConverter converter;

        /**
         * Create a converter for col using props as parameters. Return null if
         * no converter is required.
         */
        public JdbcConverter createJdbcConverter(JdbcColumn col, Object args, JdbcTypeRegistry jdbcTypeRegistry) {
            if (converter == null) converter = new NotSupportedConverter();
            return converter;
        }
    }

    /**
     * Get the value of col from rs at position index.
     * @exception SQLException on SQL errors
     * @exception JDOFatalDataStoreException if the ResultSet value is invalid
     */
    public Object get(ResultSet rs, int index, JdbcColumn col) throws SQLException, JDOFatalDataStoreException {
        throw BindingSupportImpl.getInstance().fatalDatastore("Datatype not supported by database");
    }

    /**
     * Set parameter index on ps to value (for col).
     * @exception SQLException on SQL errors
     * @exception JDOFatalDataStoreException if value is invalid
     */
    public void set(PreparedStatement ps, int index, JdbcColumn col, Object value) throws SQLException, JDOFatalDataStoreException {
        throw BindingSupportImpl.getInstance().fatalDatastore("Datatype not supported by database");
    }

    /**
     * Get the type of our expected value objects (e.g. java.util.Locale
     * for a converter for Locale's).
     */
    public Class getValueType() {
        return byte[].class;
    }
}
