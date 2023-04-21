package com.luzan.db.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

/**
 * This class allows strings to be stored in an escaped form, so that they will never be
 * automatically converted to NULL values by the database, should they be empty.
 * Note that this class will not allow you to use NULL value strings when they are not allowed by
 * Hibernate (such as in Maps).
 *
 * Version for Hibernate 3 that does not add quotes to non-empty strings but escapes by a keyword.
 * This seems more economic in cases where empty strings are rare.
 *
 * @author Erik Visser, Chess-iT B.V.
 * @author Mika Goeckel, cyber:con gmbh
 *
 */
public class HibernateEscapedString implements UserType {

    private static final String MARK_EMPTY = "<EmptyString/>";

    private static final int[] TYPES = { Types.VARCHAR };

    public int[] sqlTypes() {
        return TYPES;
    }

    public Class returnedClass() {
        return String.class;
    }

    public boolean equals(Object x, Object y) {
        if (x == y) return true;
        if (x == null || y == null) return false;
        return x.equals(y);
    }

    public Object deepCopy(Object x) {
        return x;
    }

    public boolean isMutable() {
        return false;
    }

    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
        String dbValue = (String) Hibernate.STRING.nullSafeGet(rs, names[0]);
        if (dbValue != null) {
            return unescape(dbValue);
        } else {
            return null;
        }
    }

    public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
        if (value != null) {
            String v = escape((String) value);
            Hibernate.STRING.nullSafeSet(st, v, index);
        } else {
            Hibernate.STRING.nullSafeSet(st, value, index);
        }
    }

    public Object assemble(Serializable arg0, Object arg1) throws HibernateException {
        return deepCopy(arg0);
    }

    public Serializable disassemble(Object value) {
        return (Serializable) deepCopy(value);
    }

    /**
     * Escape a string by quoting the string.
     */
    private String escape(String string) {
        return ((string == null) || (string.length() == 0)) ? MARK_EMPTY : string;
    }

    /**
     * Unescape by removing the quotes
     */
    private Object unescape(String string) throws HibernateException {
        if ((string == null) || (MARK_EMPTY.equals(string))) {
            return "";
        }
        return string;
    }

    public int hashCode(Object arg0) throws HibernateException {
        return arg0.hashCode();
    }

    public Object replace(Object arg0, Object arg1, Object arg2) throws HibernateException {
        return deepCopy(arg0);
    }
}
