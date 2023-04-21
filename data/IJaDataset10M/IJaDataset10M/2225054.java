package com.taliasplayground.convert;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import com.taliasplayground.lang.Pair;
import com.taliasplayground.lang.reflect.TypeUtils;

/**
 * <p>
 * </p>
 * @author David M. Sledge
 *
 */
@SuppressWarnings("unchecked")
public class TimeZoneConverter implements Converter {

    public static final Map<? extends Type, ? extends Pair<Type, Boolean>[]> supportedConversions;

    static {
        Map<Type, Pair<Type, Boolean>[]> conversions = new HashMap<Type, Pair<Type, Boolean>[]>();
        conversions.put(TimeZone.class, new Pair[] { new Pair<Type, Boolean>(String.class, true) });
        supportedConversions = Collections.unmodifiableMap(conversions);
    }

    @Override
    public Object convert(Object value, Type toType) throws CannotConvertException {
        if (value == null) {
            return value;
        }
        if (TypeUtils.isAssignable(TimeZone.class, toType)) {
            if (value instanceof String) {
                return TimeZone.getTimeZone((String) value);
            }
        }
        if (TypeUtils.isAssignable(String.class, toType)) {
            if (value instanceof TimeZone) {
                return ((TimeZone) value).getID();
            }
        }
        throw new CannotConvertException();
    }

    @Override
    public Map<? extends Type, ? extends Pair<Type, Boolean>[]> getSupportedConversions() {
        return supportedConversions;
    }
}
