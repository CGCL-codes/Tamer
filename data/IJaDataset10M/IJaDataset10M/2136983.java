package net.sourceforge.javabits.lang;

import java.lang.reflect.Constructor;

public class Constructors {

    public static final <T> T newInstance(final Constructor<T> constructor, final Object[] params) {
        try {
            return constructor.newInstance(params);
        } catch (final RuntimeException e) {
            throw e;
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
