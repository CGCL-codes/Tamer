package org.mili.core.properties;

import java.util.*;
import org.apache.commons.lang.*;

/**
 * This class defines methods for access properties with default values.
 *
 * @author Michael Lieshoff
 */
abstract class GenericPropsWithDefault extends GenericProps {

    int getInteger(Object object, String key, int value) {
        Validate.notNull(object, "object cannot be null!");
        return getInteger(object.getClass(), key, value);
    }

    int getInteger(Class<?> clazz, String key, int value) {
        return getInteger(Props.createName(clazz, key), value);
    }

    int getInteger(String key, int value) {
        return get(key, Integer.class, value);
    }

    long getLong(Object object, String key, long value) {
        Validate.notNull(object, "object cannot be null!");
        return getLong(object.getClass(), key, value);
    }

    long getLong(Class<?> clazz, String key, long value) {
        return getLong(Props.createName(clazz, key), value);
    }

    long getLong(String key, long value) {
        return get(key, Long.class, value);
    }

    String get(Object object, String key, String value) {
        Validate.notNull(object, "object cannot be null!");
        return get(object.getClass(), key, value);
    }

    String get(Class<?> clazz, String key, String value) {
        return get(Props.createName(clazz, key), value);
    }

    String get(String key, String value) {
        return get(key, String.class, value);
    }

    @Override
    <T> T getParameter(Map<String, ?> parameters, Properties properties, String key, Class<T> clazz) {
        return Props.getParameter(parameters, properties, key, Props.Usage.REQUIRED_NOT_NULL_AND_NOT_EMPTY, clazz);
    }

    abstract <T> T get(String key, Class<T> clazz, T value);

    abstract <T> T get(Properties properties, Class<?> clazz, String key, Class<T> typeClass);
}
