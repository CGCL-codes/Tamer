package org.jquantlib.lang.reflect;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.jquantlib.QL;

public class TypeToken {

    public static Type getType(final Class<?> klass) {
        return getType(klass, 0);
    }

    public static Type getType(final Class<?> klass, final int pos) {
        final Type superclass = klass.getGenericSuperclass();
        QL.require(!(superclass instanceof Class), ReflectConstants.SHOULD_BE_ANONYMOUS_OR_EXTENDED);
        final Type[] types = ((ParameterizedType) superclass).getActualTypeArguments();
        QL.require(pos < types.length, ReflectConstants.MISSING_GENERIC_PARAMETER_TYPE);
        return types[pos];
    }

    public static Class<?> getClazz(final Class<?> klass) {
        return getClazz(klass, 0);
    }

    public static Class<?> getClazz(final Class<?> klass, final int pos) {
        final Type type = getType(klass, pos);
        final Class<?> clazz = (type instanceof Class<?>) ? (Class<?>) type : (Class<?>) ((ParameterizedType) type).getRawType();
        QL.require(((clazz.getModifiers() & Modifier.ABSTRACT) == 0), ReflectConstants.GENERIC_PARAMETER_MUST_BE_CONCRETE_CLASS);
        return clazz;
    }
}
