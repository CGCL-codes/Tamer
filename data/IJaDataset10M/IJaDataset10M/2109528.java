package hszt.tbd.pimp.persistency.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.sql.SQLException;
import java.util.Properties;
import org.hibernate.HibernateException;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

/**
 * Hibernate Converter for numeric columns to Boolean. 
 * Uses Parameters to define the values for true/false:
 * <b>trueValue</b> holds the numeric value which represents TRUE.
 * <b>falseValue</b> holds the numeric value which represents FALSE.
 *  
 * If <i>trueValue</i> is not set, all other values than <i>falseValue</i> are considered to be TRUE.
 * If <i>falseValue</i> is not set, all other values than <i>trueValue are</i> considered to be FALSE.
 * If neither <i>falseValue</i> nor <i>trueValue</i> is set, the returned Boolean is always FALSE.
 * 
 * @author Beat Durrer - dube@sonnenkinder.org
 */
public class NumericBooleanType implements UserType, ParameterizedType {

    private static final int[] sqlTypes = new int[] { Types.NUMERIC };

    private Integer falseValue;

    private Integer trueValue;

    private int getIntValue(Boolean aBoolean) {
        int tempIntValue = 0;
        return tempIntValue;
    }

    /**
	 * There are 3 Properties available:
	 * <b>trueValue</b> holds the numeric value which represents TRUE.
	 * <b>falseValue</b> holds the numeric value which represents FALSE.
	 *  
	 * If <i>trueValue</i> is not set, all other values than <i>falseValue</i> are considered to be TRUE.
	 * If <i>falseValue</i> is not set, all other values than <i>trueValue are</i> considered to be FALSE.
	 * If neither <i>falseValue</i> nor <i>trueValue</i> is set, the returned Boolean is FALSE.
	 *   
	 * @see org.hibernate.usertype.ParameterizedType#setParameterValues(java.util.Properties)
	 */
    private Boolean getBooleanValue(int aInt) {
        if (falseValue.intValue() == aInt) {
            return new Boolean(false);
        }
        if (trueValue.intValue() == aInt) {
            return new Boolean(true);
        }
        if (null == trueValue) {
            return new Boolean(true);
        }
        return new Boolean(false);
    }

    /**
	 * @see org.hibernate.usertype.UserType#sqlTypes()
	 */
    public int[] sqlTypes() {
        return sqlTypes;
    }

    /**
	 * @see org.hibernate.usertype.UserType#returnedClass()
	 */
    public Class<Boolean> returnedClass() {
        return Boolean.class;
    }

    /**
	 * @see org.hibernate.usertype.UserType#equals(java.lang.Object, java.lang.Object)
	 */
    public boolean equals(Object anObj1, Object anObj2) throws HibernateException {
        try {
            return anObj1.equals(anObj2);
        } catch (Exception e) {
            throw new HibernateException("Exception comparing objects in NumericBooleanType", e);
        }
    }

    /**
	 * @see org.hibernate.usertype.UserType#hashCode(java.lang.Object)
	 */
    public int hashCode(Object anObj) throws HibernateException {
        return anObj.hashCode();
    }

    /**
	 * @see org.hibernate.usertype.UserType#nullSafeGet(java.sql.ResultSet, java.lang.String[], java.lang.Object)
	 */
    public Object nullSafeGet(ResultSet aResultSet, String[] names, Object owner) throws HibernateException, SQLException {
        int tempValue = aResultSet.getInt(names[0]);
        if (aResultSet.wasNull()) {
            return null;
        } else {
            return getBooleanValue(tempValue);
        }
    }

    /**
	 * @see org.hibernate.usertype.UserType#nullSafeSet(java.sql.PreparedStatement, java.lang.Object, int)
	 */
    public final void nullSafeSet(PreparedStatement statement, Object value, int index) throws HibernateException, SQLException {
        if (value == null) {
            statement.setNull(index, sqlTypes[0]);
        } else if (!(value instanceof Boolean)) {
            throw new HibernateException("NumericBooleanType: '" + value.toString() + "' is not a Boolean");
        } else {
            statement.setInt(index, getIntValue((Boolean) value));
        }
    }

    /**
	 * @see org.hibernate.usertype.UserType#deepCopy(java.lang.Object)
	 */
    public Object deepCopy(Object anObj) throws HibernateException {
        return anObj;
    }

    /**
	 * @see org.hibernate.usertype.UserType#isMutable()
	 */
    public boolean isMutable() {
        return false;
    }

    /**
	 * @see org.hibernate.usertype.UserType#disassemble(java.lang.Object)
	 */
    public Serializable disassemble(Object anObj) throws HibernateException {
        return (Serializable) anObj;
    }

    /**
	 * @see org.hibernate.usertype.UserType#assemble(java.io.Serializable, java.lang.Object)
	 */
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    /**
	 * @see org.hibernate.usertype.UserType#replace(java.lang.Object, java.lang.Object, java.lang.Object)
	 */
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    /**
	 * There are 3 Properties available:
	 * <b>trueValue</b> holds the numeric value which represents TRUE.
	 * <b>falseValue</b> holds the numeric value which represents FALSE.
	 *  
	 * If <i>trueValue</i> is not set, all other values than <i>falseValue</i> are considered to be TRUE.
	 * If <i>falseValue</i> is not set, all other values than <i>trueValue are</i> considered to be FALSE.
	 * If neither <i>falseValue</i> nor <i>trueValue</i> is set, the returned Boolean is FALSE.
	 *   
	 * @see org.hibernate.usertype.ParameterizedType#setParameterValues(java.util.Properties)
	 */
    public void setParameterValues(Properties aProp) {
        String tempTrueString = (String) aProp.get("trueValue");
        if (null != tempTrueString) {
            try {
                trueValue = Integer.valueOf(tempTrueString);
            } catch (NumberFormatException e) {
                trueValue = null;
            }
        }
        String tempFalseString = (String) aProp.get("falseValue");
        if (null != tempFalseString) {
            try {
                falseValue = Integer.valueOf(tempFalseString);
            } catch (NumberFormatException e) {
                falseValue = null;
            }
        }
    }
}
