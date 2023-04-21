package org.xmappr.converters;

import java.math.BigInteger;

public class BigIntegerConverter extends ValueConverter {

    public boolean canConvert(Class type) {
        return BigInteger.class.isAssignableFrom(type);
    }

    public Object fromValue(String value, String format, Class targetType, Object targetObject) {
        return new BigInteger(value);
    }

    public String toValue(Object object, String format) {
        return ((BigInteger) object).toString();
    }
}
