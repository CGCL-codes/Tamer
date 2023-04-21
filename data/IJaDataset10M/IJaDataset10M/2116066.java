package com.rpc.core.utils.hibernate.datatype;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

/**
 * Maps a SQL integer field to a primitive boolean. 0 or NULL = false. 1 = true
 * 
 * @author Ihor Strutynskyj
 */
public class NullablePrimitiveBooleanIntegerUserType implements UserType {

    /**
     * @see org.hibernate.usertype.UserType#assemble(java.io.Serializable,
     *      java.lang.Object)
     */
    public Object assemble(Serializable arg0, Object arg1) throws HibernateException {
        return arg0;
    }

    /**
     * @see org.hibernate.usertype.UserType#disassemble(java.lang.Object)
     */
    public Serializable disassemble(Object arg0) throws HibernateException {
        return (Double) arg0;
    }

    public int hashCode(Object arg0) throws HibernateException {
        return arg0.hashCode();
    }

    public Object replace(Object arg0, Object arg1, Object arg2) throws HibernateException {
        return arg0;
    }

    /**
     * Return the SQL type codes for the columns mapped by this type. The codes
     * are defined on <tt>java.sql.Types</tt>.
     * 
     * @see java.sql.Types
     * @return int[] the typecodes
     */
    public int[] sqlTypes() {
        return new int[] { Types.CHAR };
    }

    /**
     * The class returned by <tt>nullSafeGet()</tt>.
     * 
     * @return Class
     */
    public Class returnedClass() {
        return Boolean.class;
    }

    /**
     * Compare two instances of the class mapped by this type for persistence
     * "equality". Equality of the persistent state.
     * 
     * @param x
     * @param y
     * @return boolean
     */
    public boolean equals(Object x, Object y) throws HibernateException {
        if (x == null) {
            return y == null;
        }
        if (y == null) {
            return false;
        }
        return x.equals(y);
    }

    /**
     * Retrieve an instance of the mapped class from a JDBC resultset.
     * Implementors should handle possibility of null values.
     * 
     * @param rs
     *            a JDBC result set
     * @param names
     *            the column names
     * @param owner
     *            the containing entity
     * @return Object
     * @throws HibernateException
     * @throws SQLException
     */
    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
        String value = rs.getString(names[0]);
        if (value == null) {
            return Boolean.FALSE;
        }
        value = value.toLowerCase();
        if ("t".equalsIgnoreCase(value)) {
            return Boolean.TRUE;
        }
        if ("f".equalsIgnoreCase(value)) {
            return Boolean.FALSE;
        }
        if ("y".equalsIgnoreCase(value)) {
            return Boolean.TRUE;
        }
        if ("n".equalsIgnoreCase(value)) {
            return Boolean.FALSE;
        }
        if ("yes".equalsIgnoreCase(value)) {
            return Boolean.TRUE;
        }
        if ("no".equalsIgnoreCase(value)) {
            return Boolean.FALSE;
        }
        if ("0".equals(value)) {
            return Boolean.FALSE;
        }
        if ("1".equals(value)) {
            return Boolean.TRUE;
        }
        if ("-1".equals(value)) {
            return Boolean.TRUE;
        }
        if ("true".equalsIgnoreCase(value.toLowerCase())) {
            return Boolean.TRUE;
        }
        if ("false".equalsIgnoreCase(value)) {
            return Boolean.FALSE;
        }
        return Boolean.FALSE;
    }

    /**
     * Write an instance of the mapped class to a prepared statement.
     * Implementors should handle possibility of null values. A multi-column
     * type should be written to parameters starting from <tt>index</tt>.
     * 
     * @param st
     *            a JDBC prepared statement
     * @param value
     *            the object to write
     * @param index
     *            statement parameter index
     * @throws HibernateException
     * @throws SQLException
     */
    public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
        int v = 0;
        if (value instanceof Boolean) {
            v = ((Boolean) value).booleanValue() ? 1 : 0;
        } else if (value instanceof Character) {
            v = ((Character) value).toString().equalsIgnoreCase("t") ? 1 : 0;
        }
        st.setInt(index, v);
    }

    /**
     * Return a deep copy of the persistent state, stopping at entities and at
     * collections.
     * 
     * @return Object a copy
     */
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    /**
     * Are objects of this type mutable?
     * 
     * @return boolean
     */
    public boolean isMutable() {
        return true;
    }
}
