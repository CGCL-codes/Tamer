package com.liferay.portal.dao.orm.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

/**
 * <a href="FloatType.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class FloatType implements UserType {

    public static final float DEFAULT_VALUE = 0.0F;

    public static final int[] SQL_TYPES = new int[] { Types.FLOAT };

    public Object assemble(Serializable cached, Object owner) {
        return cached;
    }

    public Object deepCopy(Object obj) {
        return obj;
    }

    public Serializable disassemble(Object value) {
        return (Serializable) value;
    }

    public boolean equals(Object x, Object y) {
        if (x == y) {
            return true;
        } else if (x == null || y == null) {
            return false;
        } else {
            return x.equals(y);
        }
    }

    public int hashCode(Object x) {
        return x.hashCode();
    }

    public boolean isMutable() {
        return false;
    }

    public Object nullSafeGet(ResultSet rs, String[] names, Object obj) throws HibernateException, SQLException {
        Float value = (Float) Hibernate.FLOAT.nullSafeGet(rs, names[0]);
        if (value == null) {
            return new Float(DEFAULT_VALUE);
        } else {
            return value;
        }
    }

    public void nullSafeSet(PreparedStatement ps, Object obj, int index) throws HibernateException, SQLException {
        if (obj == null) {
            obj = new Float(DEFAULT_VALUE);
        }
        Hibernate.FLOAT.nullSafeSet(ps, obj, index);
    }

    public Object replace(Object original, Object target, Object owner) {
        return original;
    }

    public Class<Float> returnedClass() {
        return Float.class;
    }

    public int[] sqlTypes() {
        return SQL_TYPES;
    }
}
