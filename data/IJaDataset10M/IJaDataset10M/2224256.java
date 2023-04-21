package org.granite.messaging.amf.io.convert.impl;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;
import org.granite.messaging.amf.io.convert.Converter;
import org.granite.messaging.amf.io.convert.Converters;
import org.granite.messaging.amf.io.convert.IllegalConverterArgumentException;
import org.granite.util.ClassUtil;

/**
 * @author Franck WOLFF
 */
public class Date2Date extends Converter {

    public Date2Date(Converters converters) {
        super(converters);
    }

    @Override
    protected boolean internalCanConvert(Object value, Type targetType) {
        Class<?> targetClass = ClassUtil.classOfType(targetType);
        return (targetClass.isAssignableFrom(Date.class) || targetClass.isAssignableFrom(Calendar.class) || targetClass.isAssignableFrom(java.sql.Timestamp.class) || targetClass.isAssignableFrom(java.sql.Time.class) || targetClass.isAssignableFrom(java.sql.Date.class)) && (value == null || value instanceof Date);
    }

    @Override
    protected Object internalConvert(Object value, Type targetType) {
        if (value == null) return null;
        Date date = (Date) value;
        Class<?> targetClass = ClassUtil.classOfType(targetType);
        if (targetClass.isAssignableFrom(Date.class)) return value;
        if (targetClass.isAssignableFrom(Calendar.class)) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal;
        }
        if (targetClass.isAssignableFrom(java.sql.Timestamp.class)) return new java.sql.Timestamp(date.getTime());
        if (targetClass.isAssignableFrom(java.sql.Date.class)) return new java.sql.Date(date.getTime());
        if (targetClass.isAssignableFrom(java.sql.Time.class)) return new java.sql.Time(date.getTime());
        throw new IllegalConverterArgumentException(this, value, targetType);
    }
}
