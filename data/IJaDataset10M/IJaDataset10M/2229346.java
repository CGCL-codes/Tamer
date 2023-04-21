package com.clican.pluto.orm.usertype;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

public class StringSplitType implements UserType {

    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    public boolean equals(Object x, Object y) throws HibernateException {
        if (x == null && y == null) {
            return false;
        }
        return x.equals(y);
    }

    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    public boolean isMutable() {
        return false;
    }

    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
        assert names.length == 1;
        String result = rs.getString(names[0]);
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        return result.split(",");
    }

    public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
        if (value == null) {
            st.setString(index, "");
        } else if (value instanceof String[]) {
            StringBuffer result = new StringBuffer();
            for (String s : ((String[]) value)) {
                result.append(s);
                result.append(",");
            }
            if (result.length() == 0) {
                st.setString(index, "");
            } else {
                st.setString(index, result.toString().substring(0, result.length() - 1));
            }
        } else {
            throw new HibernateException("The property must be a comma-split string.");
        }
    }

    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    public Class<?> returnedClass() {
        return String.class;
    }

    public int[] sqlTypes() {
        return new int[] { Types.VARCHAR };
    }
}
