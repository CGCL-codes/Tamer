package net.sf.javareflector.util;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import net.sf.javareflector.Reflector;
import java.util.Map;

/**
 * Utility methods relating to primitive types.
 * <p/>
 * Created: 3/15/11
 */
public class Primitives {

    private static final Map<Class<?>, Class<?>> primitiveWrapperMap = Maps.newHashMap();

    static {
        primitiveWrapperMap.put(Boolean.TYPE, Boolean.class);
        primitiveWrapperMap.put(Byte.TYPE, Byte.class);
        primitiveWrapperMap.put(Character.TYPE, Character.class);
        primitiveWrapperMap.put(Short.TYPE, Short.class);
        primitiveWrapperMap.put(Integer.TYPE, Integer.class);
        primitiveWrapperMap.put(Long.TYPE, Long.class);
        primitiveWrapperMap.put(Double.TYPE, Double.class);
        primitiveWrapperMap.put(Float.TYPE, Float.class);
        primitiveWrapperMap.put(Void.TYPE, Void.class);
    }

    /**
     * Convert the given primitive class to a wrapper class.
     *
     * @param possiblePrimitiveClass the primitive class.
     * @return the corresponding wrapper class.
     */
    public static <T> Class<T> toWrapper(Class<T> possiblePrimitiveClass) {
        return Reflector.uncheckedCast(toWrapperWildcarded(possiblePrimitiveClass));
    }

    private static Class<?> toWrapperWildcarded(Class<?> possiblePrimitiveClass) {
        return Objects.firstNonNull(primitiveWrapperMap.get(possiblePrimitiveClass), possiblePrimitiveClass);
    }
}
