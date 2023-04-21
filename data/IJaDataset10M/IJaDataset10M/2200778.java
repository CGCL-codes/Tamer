package info.eniak.jmwa.util;

/**
 * Utility class for assertions.
 * @author Philip May
 * @since 1.0 M1
 */
public final class Assert {

    private Assert() {
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(Object object) {
        notNull(object, "Assertion failed! This argument is required. It must not be 'null'.");
    }
}
