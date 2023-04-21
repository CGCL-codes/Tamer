package com.angel.architecture.hibernate3.converters;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.lang.ObjectUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;
import com.angel.architecture.persistence.ids.ObjectId;
import com.angel.common.helpers.ObjectIdHelper;

/**
 * Convierte java ObjectId a String
 */
public final class ObjectIdConverter implements Serializable, UserType {

    private static final long serialVersionUID = -8028455048802746062L;

    static final int[] SQL_TYPE = new int[] { Hibernate.STRING.sqlType() };

    /**
     * Return the SQL type codes for the columns mapped by this type. The
     * codes are defined on <tt>java.sql.Types</tt>.
     *
     * @return int[] the typecodes
     * @see java.sql.Types
     */
    public int[] sqlTypes() {
        return SQL_TYPE;
    }

    /**
     * The class returned by <tt>nullSafeGet()</tt>.
     *
     * @return Class
     */
    public Class<?> returnedClass() {
        return ObjectId.class;
    }

    /**
     * Compare two instances of the class mapped by this type for persistence "equality".
     * Equality of the persistent state.
     *
     * @param x
     * @param y
     * @return boolean
     */
    public boolean equals(Object x, Object y) {
        return ObjectUtils.equals(x, y);
    }

    /**
     * Retrieve an instance of the mapped class from a JDBC resultset. Implementors
     * should handle possibility of null values.
     *
     * @param rs    a JDBC result set
     * @param names the column names
     * @param owner the containing entity
     * @return Object
     * @throws java.sql.SQLException
     */
    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws SQLException, HibernateException {
        String idValueString = (String) Hibernate.STRING.nullSafeGet(rs, names[0]);
        return idValueString == null ? null : ObjectIdHelper.getIdFrom(idValueString);
    }

    /**
     * Write an instance of the mapped class to a prepared statement. Implementors
     * should handle possibility of null values. A multi-column type should be written
     * to parameters starting from <tt>index</tt>.
     *
     * @param st    a JDBC prepared statement
     * @param value the object to write
     * @param index statement parameter index
     * @throws java.sql.SQLException
     */
    public void nullSafeSet(PreparedStatement st, Object value, int index) throws SQLException, HibernateException {
        Hibernate.STRING.nullSafeSet(st, value == null ? null : value.toString(), index);
    }

    /**
     * Return a deep copy of the persistent state, stopping at entities and at
     * collections.
     *
     * @return Object a copy
     */
    public Object deepCopy(Object value) {
        return value == null ? null : ObjectIdHelper.getIdFrom(((ObjectId) value).toString());
    }

    /**
     * Are objects of this type mutable?
     *
     * @return boolean
     */
    public boolean isMutable() {
        return false;
    }

    public Object getPersistedValue(ObjectId objectId) {
        return objectId.toString();
    }

    /**
     * @param objectId
     * @return
     */
    public ObjectId getValuePersisted(Object objectId) {
        return ObjectIdHelper.getIdFrom(objectId.toString());
    }

    public int hashCode(Object x) throws HibernateException {
        return (x == null) ? 0 : x.hashCode();
    }

    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }
}
