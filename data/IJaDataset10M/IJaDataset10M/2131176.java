package org.jikesrvm.runtime;

/**
 * Utility class to get at implementation of SysCall interface.
 */
public class SysCallUtil {

    @SuppressWarnings({ "unchecked" })
    public static <T> T getImplementation(Class<T> type) {
        try {
            return (T) Class.forName(type.getName() + "Impl").newInstance();
        } catch (final Exception e) {
            throw new IllegalStateException("Error creating generated implementation of " + type.getName(), e);
        }
    }
}
