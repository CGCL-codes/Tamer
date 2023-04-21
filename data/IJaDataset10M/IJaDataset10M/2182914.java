package org.zxframework.util;

import java.lang.reflect.Method;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

/**
 * @author mbrewer-admin
 *
 */
public class EnumConverter implements Converter {

    /**
	 * @see org.apache.commons.beanutils.Converter#convert(java.lang.Class, java.lang.Object)
	 */
    public Object convert(Class type, Object value) {
        Object convert = null;
        if (value instanceof String) {
            try {
                Method getEnum = type.getMethod("getEnum", new Class[] { String.class });
                getEnum.invoke(null, new Object[] { value });
            } catch (Exception e) {
                throw new ConversionException("Failed to convert : " + value);
            }
        }
        return convert;
    }
}
