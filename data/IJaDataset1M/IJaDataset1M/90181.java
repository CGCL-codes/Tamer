package com.mkk.kenji1016.domain.usertype;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.EnumSet;
import java.util.Properties;

/**
 * User: mkk
 * Date: 11-8-9
 * Time: 下午11:12
 * <p/>
 * Enum user type.
 * <p/>
 * The enum table column type is <i>int</i>.
 */
public class EnumUserType implements UserType, ParameterizedType {

    private static Logger log = Logger.getLogger(EnumUserType.class);

    private String enumClass;

    private Class clazz;

    /**
     * Init enum class
     *
     * @return Whether or not init success
     */
    private boolean initClass() {
        if (clazz == null) {
            try {
                clazz = Class.forName(enumClass);
                return true;
            } catch (ClassNotFoundException e) {
                if (log.isDebugEnabled()) {
                    log.debug("Can not found class: " + enumClass, e);
                }
                return false;
            }
        }
        return true;
    }

    public int[] sqlTypes() {
        return new int[] { Types.INTEGER };
    }

    public Class returnedClass() {
        if (initClass()) {
            return clazz;
        }
        return null;
    }

    public boolean equals(Object o, Object o1) throws HibernateException {
        return o == o1 || !(o == null || o1 == null) && o.equals(o1);
    }

    public int hashCode(Object o) throws HibernateException {
        return o.hashCode();
    }

    public Object nullSafeGet(ResultSet resultSet, String[] strings, Object o) throws HibernateException, SQLException {
        final int val = resultSet.getInt(strings[0]);
        Object obj = null;
        if (!resultSet.wasNull() && initClass()) {
            @SuppressWarnings("unchecked") EnumSet enumSet = EnumSet.allOf(clazz);
            for (Object object : enumSet) {
                if (object instanceof Enum) {
                    Enum e = (Enum) object;
                    if (e.ordinal() == val) {
                        obj = e;
                    }
                }
            }
        }
        return obj;
    }

    public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index) throws HibernateException, SQLException {
        if (null == value) {
            preparedStatement.setNull(index, Types.INTEGER);
        } else {
            preparedStatement.setInt(index, ((Enum) value).ordinal());
        }
    }

    public Object deepCopy(Object o) throws HibernateException {
        return o;
    }

    public boolean isMutable() {
        return false;
    }

    public Serializable disassemble(Object o) throws HibernateException {
        return (Serializable) o;
    }

    public Object assemble(Serializable serializable, Object o) throws HibernateException {
        return serializable;
    }

    public Object replace(Object o, Object o1, Object o2) throws HibernateException {
        return o;
    }

    public String getEnumClass() {
        return enumClass;
    }

    public void setEnumClass(String enumClass) {
        this.enumClass = enumClass;
    }

    public void setParameterValues(Properties properties) {
        enumClass = properties.getProperty("enumClass");
    }
}
