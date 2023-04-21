package simplespider.simplespider.util;

import java.util.Collection;
import java.util.Dictionary;
import java.util.Map;
import java.util.Properties;

public final class ValidityHelper {

    public static void checkNotEmpty(final String name, final CharSequence value) {
        checkNotEmptyInternal("name", name);
        checkNotEmptyInternal(name, value);
    }

    public static void checkNotEmpty(final String name, final Collection<?> values) {
        checkNotEmptyInternal("name", name);
        checkNotNull(name, values);
        if (isEmpty(values)) {
            throw new IllegalArgumentException(name + " is empty");
        }
    }

    private static void checkNotEmptyInternal(final String name, final CharSequence value) {
        checkNotNullInternal(name, value);
        if (isEmpty(value)) {
            throw new IllegalArgumentException(name + " is empty");
        }
    }

    public static void checkNotNull(final String name, final Object value) {
        checkNotEmptyInternal("name", name);
        if (value == null) {
            throw new NullPointerException(name + " is null");
        }
    }

    private static void checkNotNullInternal(final String name, final Object value) {
        if (value == null) {
            throw new NullPointerException(name + " is null");
        }
    }

    public static boolean isEmpty(final CharSequence value) {
        return value == null || value.length() == 0;
    }

    public static boolean isEmpty(final Collection<?> values) {
        return values == null || values.size() == 0;
    }

    private ValidityHelper() {
    }

    public static <T, S> boolean isEmpty(final Map<T, S> values) {
        return values == null || values.size() == 0;
    }

    public static <T, S> void checkNotEmpty(final String name, final Map<T, S> values) {
        checkNotEmptyInternal("name", name);
        checkNotNull(name, values);
        if (isEmpty(values)) {
            throw new IllegalArgumentException(name + " is empty");
        }
    }

    public static <T, S> boolean isEmpty(final Dictionary<T, S> values) {
        return values == null || values.size() == 0;
    }

    public static <T, S> void checkNotEmpty(final String name, final Dictionary<T, S> values) {
        checkNotEmptyInternal("name", name);
        checkNotNull(name, values);
        if (isEmpty(values)) {
            throw new IllegalArgumentException(name + " is empty");
        }
    }

    public static boolean isEmpty(final Properties values) {
        return values == null || values.size() == 0;
    }

    public static void checkNotEmpty(final String name, final Properties values) {
        checkNotEmptyInternal("name", name);
        checkNotNull(name, values);
        if (isEmpty(values)) {
            throw new IllegalArgumentException(name + " is empty");
        }
    }

    public static boolean isEmpty(final char[] value) {
        return value == null || value.length == 0;
    }

    public static void checkNotEmpty(final String name, final char[] values) {
        checkNotEmptyInternal("name", name);
        checkNotNull(name, values);
        if (isEmpty(values)) {
            throw new IllegalArgumentException(name + " is empty");
        }
    }

    public static boolean isEmpty(final short[] value) {
        return value == null || value.length == 0;
    }

    public static void checkNotEmpty(final String name, final short[] values) {
        checkNotEmptyInternal("name", name);
        checkNotNull(name, values);
        if (isEmpty(values)) {
            throw new IllegalArgumentException(name + " is empty");
        }
    }

    public static boolean isEmpty(final int[] value) {
        return value == null || value.length == 0;
    }

    public static void checkNotEmpty(final String name, final int[] values) {
        checkNotEmptyInternal("name", name);
        checkNotNull(name, values);
        if (isEmpty(values)) {
            throw new IllegalArgumentException(name + " is empty");
        }
    }

    public static boolean isEmpty(final long[] value) {
        return value == null || value.length == 0;
    }

    public static void checkNotEmpty(final String name, final long[] values) {
        checkNotEmptyInternal("name", name);
        checkNotNull(name, values);
        if (isEmpty(values)) {
            throw new IllegalArgumentException(name + " is empty");
        }
    }

    public static boolean isEmpty(final boolean[] value) {
        return value == null || value.length == 0;
    }

    public static void checkNotEmpty(final String name, final boolean[] values) {
        checkNotEmptyInternal("name", name);
        checkNotNull(name, values);
        if (isEmpty(values)) {
            throw new IllegalArgumentException(name + " is empty");
        }
    }

    public static boolean isEmpty(final float[] value) {
        return value == null || value.length == 0;
    }

    public static void checkNotEmpty(final String name, final float[] values) {
        checkNotEmptyInternal("name", name);
        checkNotNull(name, values);
        if (isEmpty(values)) {
            throw new IllegalArgumentException(name + " is empty");
        }
    }

    public static boolean isEmpty(final double[] value) {
        return value == null || value.length == 0;
    }

    public static void checkNotEmpty(final String name, final double[] values) {
        checkNotEmptyInternal("name", name);
        checkNotNull(name, values);
        if (isEmpty(values)) {
            throw new IllegalArgumentException(name + " is empty");
        }
    }

    public static boolean isEmpty(final Object[] value) {
        return value == null || value.length == 0;
    }

    public static void checkNotEmpty(final String name, final Object[] values) {
        checkNotEmptyInternal("name", name);
        checkNotNull(name, values);
        if (isEmpty(values)) {
            throw new IllegalArgumentException(name + " is empty");
        }
    }
}
