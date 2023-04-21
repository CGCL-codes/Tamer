package org.jowidgets.util;

import java.util.Collection;

public final class Assert {

    private Assert() {
        super();
    }

    public static void paramNotNull(final Object object, final String name) {
        if (object == null) {
            throw new IllegalArgumentException("The parameter '" + name + "' must not be null!");
        }
    }

    public static void paramNotEmpty(final String string, final String name) {
        if (string == null || string.length() <= 0) {
            throw new IllegalArgumentException("The parameter '" + name + "' must not be empty!");
        }
    }

    public static void paramNotEmpty(final Collection<?> collection, final String name) {
        if (collection == null || collection.size() <= 0) {
            throw new IllegalArgumentException("The parameter '" + name + "' must not be empty!");
        }
    }

    public static void paramNotEmpty(final Iterable<?> iterable, final String name) {
        paramNotNull(iterable, name);
        if (iterable.iterator() == null || !iterable.iterator().hasNext()) {
            throw new IllegalArgumentException("The parameter '" + name + "' must not be empty!");
        }
    }

    public static void paramNotEmpty(final Object[] array, final String name) {
        paramNotNull(array, name);
        if (array.length <= 0) {
            throw new IllegalArgumentException("The parameter '" + name + "' must not be empty!");
        }
    }

    public static void paramAndElementsNotEmpty(final Object[] array, final String name) {
        paramNotNull(array, name);
        if (array.length <= 0) {
            throw new IllegalArgumentException("The parameter '" + name + "' must not be empty!");
        }
        for (final Object obj : array) {
            if (obj == null) {
                throw new IllegalArgumentException("The parameter '" + name + "' must not have empty elements!");
            }
        }
    }

    public static void paramHasType(final Object object, final Class<?> type, final String name) {
        paramNotNull(object, name);
        if (!type.isAssignableFrom(object.getClass())) {
            throw new IllegalArgumentException("The type of the parameter '" + name + "' must be " + type.getName() + "' !");
        }
    }

    public static void paramInBounds(final long rightBoundary, final long value, final String name) {
        paramInBounds(0, rightBoundary, value, name);
    }

    public static void paramInBounds(final long leftBoundary, final long rightBoundary, final long value, final String name) {
        if (value < leftBoundary || value > rightBoundary) {
            throw new IndexOutOfBoundsException("The parameter '" + name + "' must be between '" + leftBoundary + "' and '" + rightBoundary + "' but is '" + value + "'.");
        }
    }
}
