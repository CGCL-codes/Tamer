package org.granite.messaging.amf.io.convert.impl;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import org.granite.messaging.amf.io.convert.Converter;
import org.granite.messaging.amf.io.convert.Converters;

/**
 * @author Franck WOLFF
 */
public class Number2BigDecimal extends Converter {

    public Number2BigDecimal(Converters converters) {
        super(converters);
    }

    @Override
    protected boolean internalCanConvert(Object value, Type targetType) {
        return targetType.equals(BigDecimal.class) && (value == null || value instanceof Number);
    }

    @Override
    protected Object internalConvert(Object value, Type targetType) {
        return (value == null || value instanceof BigDecimal) ? value : BigDecimal.valueOf(((Number) value).doubleValue());
    }
}
