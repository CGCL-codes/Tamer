package org.pandcorps.core;

import java.lang.reflect.*;

public final class Reftil {

    private static final Class<?>[] EMPTY_ARRAY_CLASS = {};

    private static final Object[] EMPTY_ARRAY_OBJECT = {};

    private Reftil() {
        throw new Error();
    }

    public static final Object newInstance(final String className) {
        final Class<?> c;
        try {
            c = Class.forName(className);
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return newInstance(c);
    }

    public static final <T> T newInstance(final Class<T> c) {
        try {
            final Constructor<T> constructor = c.getDeclaredConstructor(EMPTY_ARRAY_CLASS);
            constructor.setAccessible(true);
            return constructor.newInstance(EMPTY_ARRAY_OBJECT);
        } catch (final Exception e) {
            throw Pantil.toRuntimeException(e);
        }
    }

    public static final String getClassName(final Object o) {
        return o == null ? null : o.getClass().getName();
    }
}
