package org.openuss.newsletter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.hibernate.HibernateException;

/**
 * 
 */
public final class MailingStatusEnum extends MailingStatus implements java.io.Serializable, java.lang.Comparable, org.hibernate.usertype.EnhancedUserType {

    private static final int[] SQL_TYPES = { Types.VARCHAR };

    private static final long serialVersionUID = -7143088818636163286L;

    /**
     * Default constructor.  Hibernate needs the default constructor
     * to retrieve an instance of the enum from a JDBC resultset.
     * The instance will be converted to the correct enum instance
     * in {@link #nullSafeGet(java.sql.ResultSet, java.lang.String[], java.lang.Object)}.
     */
    public MailingStatusEnum() {
        super();
    }

    /**
     *  @see org.hibernate.usertype.UserType#sqlTypes()
     */
    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    /**
     *  @see org.hibernate.usertype.UserType#deepCopy(java.lang.Object)
     */
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    /**
     *  @see org.hibernate.usertype.UserType#isMutable()
     */
    public boolean isMutable() {
        return false;
    }

    /**
     *  @see org.hibernate.usertype.UserType#equals(java.lang.Object, java.lang.Object)
     */
    public boolean equals(Object x, Object y) throws HibernateException {
        return (x == y) || (x != null && y != null && y.equals(x));
    }

    /**
     * @see org.hibernate.usertype.UserType#returnedClass()
     */
    public Class returnedClass() {
        return MailingStatus.class;
    }

    /**
     *  @see org.hibernate.usertype.UserType#nullSafeGet(java.sql.ResultSet, java.lang.String[], java.lang.Object)
     */
    public Object nullSafeGet(ResultSet resultSet, String[] values, Object owner) throws HibernateException, SQLException {
        final java.lang.Integer value = (java.lang.Integer) resultSet.getObject(values[0]);
        return resultSet.wasNull() ? null : fromInteger(value);
    }

    /**
     * @see org.hibernate.usertype.UserType#nullSafeSet(java.sql.PreparedStatement, java.lang.Object, int)
     */
    public void nullSafeSet(PreparedStatement statement, Object value, int index) throws HibernateException, SQLException {
        if (value == null) {
            statement.setNull(index, Types.VARCHAR);
        } else {
            statement.setObject(index, java.lang.Integer.valueOf(java.lang.String.valueOf(value)));
        }
    }

    /**
     * @see org.hibernate.usertype.UserType#replace(Object original, Object target, Object owner)
     */
    public Object replace(Object original, Object target, Object owner) {
        return original;
    }

    /**
     * @see org.hibernate.usertype.UserType#assemble(java.io.Serializable cached, Object owner)
     */
    public Object assemble(java.io.Serializable cached, Object owner) {
        return cached;
    }

    /**
     * @see org.hibernate.usertype.UserType#disassemble(Object value)
     */
    public java.io.Serializable disassemble(Object value) {
        return (java.io.Serializable) value;
    }

    /**
     * @see org.hibernate.usertype.UserType#hashCode(Object value)
     */
    public int hashCode(Object value) {
        return value.hashCode();
    }

    /**
     * @see org.hibernate.usertype.EnhancedUserType#objectToSQLString(Object object)
     */
    public String objectToSQLString(Object object) {
        return java.lang.String.valueOf(((org.openuss.newsletter.MailingStatus) object).getValue());
    }

    /**
     * @see org.hibernate.usertype.EnhancedUserType#toXMLString(Object object)
     */
    public String toXMLString(Object object) {
        return java.lang.String.valueOf(((org.openuss.newsletter.MailingStatus) object).getValue());
    }

    /**
     * @see org.hibernate.usertype.EnhancedUserType#fromXMLString(String string)
     */
    public Object fromXMLString(String string) {
        return org.openuss.newsletter.MailingStatus.fromInteger(java.lang.Integer.valueOf(string));
    }
}
