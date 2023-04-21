package de.saly.javacommonslib.base.exception;

public class ExceptionUtils {

    public static Throwable getRootCause(Throwable e) {
        if (e == null) return null;
        Throwable c = e.getCause();
        if (c == null) return e;
        while (c.getCause() != null) {
            c = c.getCause();
        }
        return c;
    }

    public static boolean isRootRuntimeException(Throwable e) {
        return getRootCause(e) instanceof java.lang.RuntimeException;
    }

    public static boolean isRootExceptionInstanceOf(Throwable e, Class exception) {
        return exception.isInstance(getRootCause(e));
    }
}
