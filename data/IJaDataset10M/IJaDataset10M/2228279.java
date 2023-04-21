package com.liferay.util.dao.hibernate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.UserType;

/**
 * <a href="BooleanType.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.4 $
 *
 */
public class BooleanType implements UserType {

    public static final boolean DEFAULT_VALUE = false;

    public static final int[] SQL_TYPES = new int[] { java.sql.Types.BOOLEAN };

    public Object deepCopy(Object obj) {
        return obj;
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

    public boolean isMutable() {
        return false;
    }

    public Object nullSafeGet(ResultSet rs, String[] names, Object obj) throws HibernateException, SQLException {
        Boolean value = (Boolean) Hibernate.BOOLEAN.nullSafeGet(rs, names[0]);
        if (value == null) {
            return new Boolean(DEFAULT_VALUE);
        } else {
            return value;
        }
    }

    public void nullSafeSet(PreparedStatement ps, Object obj, int index) throws HibernateException, SQLException {
        if (obj == null) {
            obj = new Boolean(DEFAULT_VALUE);
        }
        Hibernate.BOOLEAN.nullSafeSet(ps, obj, index);
    }

    public Class returnedClass() {
        return Boolean.class;
    }

    public int[] sqlTypes() {
        return SQL_TYPES;
    }
}
