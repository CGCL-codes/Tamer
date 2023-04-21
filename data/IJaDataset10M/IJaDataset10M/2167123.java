package com.g0dkar.leet.util.ognl.typeConversion.primitive;

import java.math.BigDecimal;
import java.math.BigInteger;
import com.g0dkar.leet.util.ognl.typeConversion.LeetTypeConverter;
import com.g0dkar.leet.util.ognl.typeConversion.TypeConverter;

/**
 * Executes every {@link Number} primitive wrapper conversion (including {@link BigDecimal} and {@link BigInteger}) and
 * the primitives themselves.
 * 
 * @author Rafael g0dkar Lins
 * 
 */
@LeetTypeConverter
public class NumberConverter implements TypeConverter {

    public String toString(Object value, Class<?>... genericType) throws Exception {
        Class<?> klass = value.getClass();
        if (value.getClass().isPrimitive() || Number.class.isAssignableFrom(klass)) {
            return value.toString();
        } else {
            return null;
        }
    }

    public Object fromString(String value, Class<?> toType, Class<?>... genericTypes) throws Exception {
        if (toType.equals(byte.class)) {
            return Byte.parseByte(value);
        } else if (toType.equals(Byte.class)) {
            return new Byte(value);
        }
        if (toType.equals(short.class)) {
            return Short.parseShort(value);
        } else if (toType.equals(Short.class)) {
            return new Short(value);
        }
        if (toType.equals(int.class)) {
            return Integer.parseInt(value);
        } else if (toType.equals(Integer.class)) {
            return new Integer(value);
        }
        if (toType.equals(long.class)) {
            return Long.parseLong(value);
        } else if (toType.equals(Long.class)) {
            return new Long(value);
        }
        if (toType.equals(float.class)) {
            return Float.parseFloat(value);
        } else if (toType.equals(Float.class)) {
            return new Float(value);
        }
        if (toType.equals(double.class)) {
            return Double.parseDouble(value);
        } else if (toType.equals(Double.class)) {
            return new Double(value);
        }
        if (toType.equals(BigDecimal.class)) {
            return new BigDecimal(value);
        }
        if (toType.equals(BigInteger.class)) {
            return new BigInteger(value);
        }
        return null;
    }

    public boolean canConvert(Class<?> toType) {
        boolean canConvert = false;
        if (Number.class.isAssignableFrom(toType)) {
            canConvert = true;
        } else if (toType.isPrimitive()) {
            canConvert = toType.equals(byte.class) || toType.equals(short.class) || toType.equals(int.class) || toType.equals(long.class) || toType.equals(float.class) || toType.equals(double.class);
        }
        return canConvert;
    }

    public Object newInstance(Class<?> toType) throws Exception {
        return fromString("0", toType, null);
    }

    public String toDisplayableString(Object value, Class<?>... genericTypes) throws Exception {
        if (canConvert(value.getClass())) {
            return value.toString();
        }
        return null;
    }
}
