package com.icteam.fiji.persistence.util;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;
import com.icteam.fiji.model.PropCntto;
import com.icteam.fiji.model.PropCnttoEnum;

public class PropCnttoUserType implements UserType {

    private static final int[] SQL_TYPES = { Types.NUMERIC };

    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    public Class<PropCntto> returnedClass() {
        return PropCntto.class;
    }

    public boolean equals(Object x, Object y) {
        return x == y;
    }

    public int hashCode(Object object) throws HibernateException {
        return object == null ? 0 : object.hashCode();
    }

    public Object deepCopy(Object value) {
        return value;
    }

    public boolean isMutable() {
        return false;
    }

    public Serializable disassemble(Object object) throws HibernateException {
        return (PropCntto) object;
    }

    public Object assemble(Serializable serializable, Object object) throws HibernateException {
        return serializable;
    }

    public Object replace(Object object, Object object1, Object object2) throws HibernateException {
        return object;
    }

    @SuppressWarnings("boxing")
    public Object nullSafeGet(ResultSet resultSet, String[] names, Object owner) throws HibernateException, SQLException {
        Long value = resultSet.getLong(names[0]);
        return resultSet.wasNull() ? null : PropCnttoEnum.getValue(value);
    }

    @SuppressWarnings("boxing")
    public void nullSafeSet(PreparedStatement statement, Object value, int index) throws HibernateException, SQLException {
        if (value == null) {
            statement.setNull(index, Types.NUMERIC);
        } else {
            statement.setLong(index, ((PropCntto) value).getCPropCntto());
        }
    }
}
