package org.databene.commons;

import java.util.Collection;

/**
 * Provides utility methods for threading.<br/><br/>
 * Created: 26.03.2010 19:26:07
 * @since 0.5.0
 * @author Volker Bergmann
 */
public class ThreadUtil {

    private ThreadUtil() {
    }

    public static <C extends Collection<T>, T extends ThreadAware> boolean allThreadSafe(C elements) {
        for (ThreadAware element : elements) if (!element.isThreadSafe()) return false;
        return true;
    }

    public static <T extends ThreadAware> boolean allThreadSafe(T[] elements) {
        for (ThreadAware element : elements) if (!element.isThreadSafe()) return false;
        return true;
    }

    public static <C extends Collection<T>, T extends ThreadAware> boolean allParallelizable(C elements) {
        for (ThreadAware element : elements) if (!element.isParallelizable()) return false;
        return true;
    }

    public static <T extends ThreadAware> boolean allParallelizable(T[] elements) {
        for (ThreadAware element : elements) if (!element.isParallelizable()) return false;
        return true;
    }
}
