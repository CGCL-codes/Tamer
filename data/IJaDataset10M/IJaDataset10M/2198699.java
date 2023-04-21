package org.fest.swing.util;

import java.util.Map;
import static org.fest.reflect.core.Reflection.field;

/**
 * Understands utility methods related to reflection.
 *
 * @author Alex Ruiz
 */
public final class ReflectionUtils {

    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> mapField(String fieldName, Object target) {
        Map<?, ?> map = field(fieldName).ofType(Map.class).in(target).get();
        return (Map<K, V>) map;
    }

    private ReflectionUtils() {
    }
}
