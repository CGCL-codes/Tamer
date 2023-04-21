package com.versant.core.jdbc.sql.conv;

import com.versant.core.jdbc.JdbcConverter;
import com.versant.core.jdbc.JdbcConverterFactory;
import com.versant.core.jdbc.JdbcTypeRegistry;
import com.versant.core.jdbc.metadata.JdbcColumn;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Locale;
import javax.jdo.JDOFatalDataStoreException;
import com.versant.core.common.BindingSupportImpl;

/**
 * This converter converts Locale objects to and from SQL. It assumes that
 * the Locale is stored in a column compatible with ResultSet.getString and
 * PreparedStatement.setString. The Locale is stored as a 4 or 6 character
 * String (lang + country [+ variant]).
 * @keep-all
 */
public class LocaleConverter extends JdbcConverterBase {

    public static class Factory extends NoArgJdbcConverterFactory {

        private LocaleConverter converter;

        /**
         * Create a converter for col using args as parameters. Return null if
         * no converter is required.
         */
        public JdbcConverter createJdbcConverter(JdbcColumn col, Object args, JdbcTypeRegistry jdbcTypeRegistry) {
            if (converter == null) converter = new LocaleConverter();
            return converter;
        }
    }

    /**
     * Get the value of col from rs at position index.
     * @exception SQLException on SQL errors
     * @exception JDOFatalDataStoreException if the ResultSet value is invalid
     */
    public Object get(ResultSet rs, int index, JdbcColumn col) throws SQLException, JDOFatalDataStoreException {
        String s = rs.getString(index);
        if (s == null) return null;
        s = s.trim();
        if (s.length() == 0) return null;
        try {
            String lang = s.substring(0, 2);
            String country = null;
            if (s.length() > 2) {
                country = s.substring(2, 4);
            } else {
                country = "";
            }
            String variant = null;
            if (s.length() > 4) {
                variant = s.substring(4, 6);
            } else {
                variant = "";
            }
            return new Locale(lang, country, variant);
        } catch (IndexOutOfBoundsException x) {
            throw BindingSupportImpl.getInstance().fatalDatastore("Invalid Locale value for " + col + ": '" + s + "'", x);
        }
    }

    /**
     * Set parameter index on ps to value (for col).
     * @exception SQLException on SQL errors
     * @exception JDOFatalDataStoreException if value is invalid
     */
    public void set(PreparedStatement ps, int index, JdbcColumn col, Object value) throws SQLException, JDOFatalDataStoreException {
        if (value == null) {
            ps.setNull(index, col.jdbcType);
        } else {
            Locale l = (Locale) value;
            StringBuffer s = new StringBuffer();
            s.append(l.getLanguage());
            s.append(l.getCountry());
            if (col.length >= 6) {
                String v = l.getVariant();
                if (v.length() == 0) v = "  ";
                s.append(v);
            }
            ps.setString(index, s.toString());
        }
    }

    /**
     * Get the type of our expected value objects (e.g. java.util.Locale
     * for a converter for Locale's).
     */
    public Class getValueType() {
        return Locale.class;
    }
}
