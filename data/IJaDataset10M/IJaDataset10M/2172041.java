package net.sf.japi.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import static sun.misc.Service.providers;

/**
 * Service serves as a Proxy for {@link ServiceLoader}, using {@link sun.misc.Service} as a fallback in case {@link ServiceLoader} is unavailable.
 * This allows programmers to write programs that use {@link ServiceLoader} features even on older Java VMs.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class Service<T> {

    /**
     * Lookup service class implementations using no specific class loader.
     * @param service Service class to look up
     * @return implementations found (lazy iterable)
     * @throws ServiceConfigurationError if running on Mustang and the service isn't configured properly
     */
    @NotNull
    public static <T> Iterable<T> load(@NotNull final Class<T> service) {
        try {
            return (Iterable<T>) getMustangServiceClass().getMethod("load", Class.class).invoke(null, service);
        } catch (final ClassNotFoundException ignore) {
            return new IteratorIterable<T>(providers(service));
        } catch (final NoSuchMethodException ignore) {
            return new IteratorIterable<T>(providers(service));
        } catch (final IllegalAccessException ignore) {
            return new IteratorIterable<T>(providers(service));
        } catch (final InvocationTargetException e) {
            final Throwable t = e.getCause();
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else if (t instanceof Error) {
                throw (Error) t;
            } else {
                final Thread currentThread = Thread.currentThread();
                currentThread.getUncaughtExceptionHandler().uncaughtException(currentThread, t);
                throw new Error(t);
            }
        }
    }

    private static Class<?> getMustangServiceClass() throws ClassNotFoundException {
        return Class.forName("java.util.ServiceLoader");
    }

    /**
     * Lookup service class implementations using a specified class loader.
     * @param service Service class to look up
     * @param loader Class loader to use for look up
     * @return implementations found (lazy iterable)
     * @throws ServiceConfigurationError if running on Mustang and the service isn't configured properly
     */
    @NotNull
    public static <T> Iterable<T> load(@NotNull final Class<T> service, @NotNull final ClassLoader loader) {
        try {
            return (Iterable<T>) getMustangServiceClass().getMethod("load", Class.class, ClassLoader.class).invoke(null, service, loader);
        } catch (final ClassNotFoundException ignore) {
            return new IteratorIterable<T>(providers(service));
        } catch (final NoSuchMethodException ignore) {
            return new IteratorIterable<T>(providers(service));
        } catch (final IllegalAccessException ignore) {
            return new IteratorIterable<T>(providers(service));
        } catch (final InvocationTargetException e) {
            final Throwable t = e.getCause();
            try {
                throw t;
            } catch (final RuntimeException e2) {
                throw e2;
            } catch (final Error e2) {
                throw e2;
            } catch (final Throwable e2) {
                final Thread currentThread = Thread.currentThread();
                currentThread.getUncaughtExceptionHandler().uncaughtException(currentThread, e2);
                throw new Error(e2);
            }
        }
    }

    /**
     * Lookup service class using Mustang.
     * @param service Service class to look up
     * @param loader Class loader to use
     * @return implementations found (lazy iterable)
     * @throws MustangUnavailableException In case Mustang is not available and thus it's not possible to use Mustang services.
     */
    @NotNull
    private static <T> Iterable<T> loadMustang(@NotNull final Class<T> service, @Nullable final ClassLoader loader) throws MustangUnavailableException {
        try {
            return (Iterable<T>) getMustangServiceClass().getMethod("load", Class.class, ClassLoader.class).invoke(null, service, loader);
        } catch (final IllegalAccessException ignore) {
            throw new MustangUnavailableException();
        } catch (final NoSuchMethodException ignore) {
            throw new MustangUnavailableException();
        } catch (final ClassNotFoundException ignore) {
            throw new MustangUnavailableException();
        } catch (final InvocationTargetException ignore) {
            throw new MustangUnavailableException();
        }
    }

    /** Exception thrown when Mustang is unavailable. */
    private static class MustangUnavailableException extends Exception {

        /** Serial version. */
        private static final long serialVersionUID = 1L;
    }
}
