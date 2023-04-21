package com.liferay.util.dao.hibernate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.UserType;

/**
 * <a href="IntegerType.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.4 $
 *
 */
public class IntegerType implements UserType {

    public static final int DEFAULT_VALUE = 0;

    public static final int[] SQL_TYPES = new int[] { java.sql.Types.INTEGER };

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
        Integer value = (Integer) Hibernate.INTEGER.nullSafeGet(rs, names[0]);
        if (value == null) {
            return new Integer(DEFAULT_VALUE);
        } else {
            return value;
        }
    }

    public void nullSafeSet(PreparedStatement ps, Object obj, int index) throws HibernateException, SQLException {
        if (obj == null) {
            obj = new Integer(DEFAULT_VALUE);
        }
        Hibernate.INTEGER.nullSafeSet(ps, obj, index);
    }

    public Class returnedClass() {
        return Integer.class;
    }

    public int[] sqlTypes() {
        return SQL_TYPES;
    }
}
