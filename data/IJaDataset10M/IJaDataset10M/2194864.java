package net.sf.twip.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

/**
 * Loads all implementations if a service interface; uses either the Java 1.6
 * <code>java.util.ServiceLoader</code> or the Sun Java 1.5
 * <code>sun.misc.Service</code>. See those classes for details.
 */
public class ServiceLoaderWrapper<T> implements Iterable<T> {

    public static boolean classExists(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (final ClassNotFoundException e) {
            return false;
        }
    }

    public static <T> Iterable<T> load(Class<T> type) {
        return new ServiceLoaderWrapper<T>(type);
    }

    private final Class<T> type;

    ServiceLoaderWrapper(Class<T> type) {
        this.type = type;
    }

    /**
	 * before Java 1.6 on Sun JVMs
	 */
    private Iterator<T> getServiceIterator() {
        final Method loadMethod = getServiceLoadMethod("sun.misc.Service", "providers");
        if (loadMethod == null) return null;
        @SuppressWarnings("unchecked") final Iterator<T> iterator = (Iterator<T>) invoke(loadMethod);
        return iterator;
    }

    Class<?> getServiceLoader(String className) {
        try {
            return Class.forName(className);
        } catch (final ClassNotFoundException e) {
            return null;
        }
    }

    /**
	 * since Java 1.6
	 */
    private Iterator<T> getServiceLoaderIterator() {
        final Method loadMethod = getServiceLoadMethod("java.util.ServiceLoader", "load");
        if (loadMethod == null) return null;
        @SuppressWarnings("unchecked") final Iterable<T> loader = (Iterable<T>) invoke(loadMethod);
        return loader.iterator();
    }

    private Method getServiceLoadMethod(String className, String methodName) {
        final Class<?> loaderClass = getServiceLoader(className);
        if (loaderClass == null) return null;
        try {
            return loaderClass.getMethod(methodName, Class.class);
        } catch (final NoSuchMethodException e) {
            throw new ServiceLoadingException("the " + className + " class has no '" + methodName + "' method.", e);
        }
    }

    private Object invoke(Method load) {
        try {
            return load.invoke(null, type);
        } catch (final IllegalAccessException e) {
            throw new ServiceLoadingException("ServiceLoader.load throws", e);
        } catch (final InvocationTargetException e) {
            throw new ServiceLoadingException("ServiceLoader.load throws", e.getCause());
        } catch (final RuntimeException e) {
            throw new ServiceLoadingException("ServiceLoader.load throws", e);
        }
    }

    public Iterator<T> iterator() {
        Iterator<T> iterator = getServiceLoaderIterator();
        if (iterator == null) iterator = getServiceIterator();
        if (iterator == null) throw new ServiceLoadingException("can't find ServiceLoader class");
        return iterator;
    }
}
