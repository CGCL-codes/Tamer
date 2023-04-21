package net.sf.tsngo.eserve.hibernate;

import org.hibernate.usertype.UserType;
import org.hibernate.HibernateException;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.Serializable;

/**
 *
 * @author Chandrasekar T
 */
public class BooleanUserType implements UserType {

    private static final int[] SQL_TYPES = { Types.VARCHAR };

    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    public Class returnedClass() {
        return Boolean.class;
    }

    public Object replace(Object original, Object target, Object owner) {
        return original;
    }

    public Serializable disassemble(Object value) {
        return (Serializable) value;
    }

    public Object assemble(Serializable cached, Object owner) {
        return cached;
    }

    public int hashCode(Object x) {
        return x.hashCode();
    }

    public boolean equals(Object x, Object y) {
        if (x == y) return true; else if (x == null || y == null) return false; else return x.equals(y);
    }

    public Object deepCopy(Object value) {
        return value;
    }

    public boolean isMutable() {
        return false;
    }

    public Object nullSafeGet(ResultSet resultSet, String[] names, Object owner) throws HibernateException, SQLException {
        String value = resultSet.getString(names[0]);
        return (!resultSet.wasNull() && "Y".equals(value)) ? true : false;
    }

    public void nullSafeSet(PreparedStatement statement, Object value, int index) throws HibernateException, SQLException {
        if (value != null && (value instanceof Boolean) && ((Boolean) value).booleanValue()) {
            statement.setString(index, "Y");
        } else {
            statement.setString(index, "N");
        }
    }
}
