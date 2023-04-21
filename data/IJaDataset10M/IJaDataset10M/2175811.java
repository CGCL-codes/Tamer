package com.rpc.core.utils.hibernate.datatype;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

/**
 * An Integer datatype that decements the actual sql value by one. Used for
 * zero-based list indexes that are saved in the DB as one-based indexes.
 * 
 * @author ted stockwell
 */
public class OneBasedIndexUserType implements UserType {

    public Object assemble(Serializable arg0, Object arg1) throws HibernateException {
        return arg0;
    }

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
        return new int[] { Types.INTEGER };
    }

    /**
     * The class returned by <tt>nullSafeGet()</tt>.
     * 
     * @return Class
     */
    public Class returnedClass() {
        return Integer.class;
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
        if (x == null) return y == null;
        if (y == null) return false;
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
        return new Integer(rs.getInt(names[0]) - 1);
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
        if (value == null) {
            st.setNull(index, Types.INTEGER);
            return;
        }
        st.setInt(index, ((Integer) value).intValue() + 1);
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
