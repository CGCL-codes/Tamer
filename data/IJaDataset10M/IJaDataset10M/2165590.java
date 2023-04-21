package org.plazmaforge.framework.util;

import java.lang.reflect.InvocationTargetException;
import org.plazmaforge.framework.core.exception.ApplicationException;

public class ErrorUtils {

    public static Throwable getCause(Throwable t) {
        if (t == null) {
            return null;
        }
        if (t instanceof ApplicationException) {
            return privateGetApplicationExceptionCause(t);
        }
        if (t instanceof InvocationTargetException) {
            return privateGetCause(t);
        }
        Throwable cause = privateGetCause(t);
        if (cause instanceof ApplicationException) {
            cause = privateGetApplicationExceptionCause(cause);
        }
        return cause;
    }

    private static Throwable privateGetCause(Throwable t) {
        if (t == null) {
            return null;
        }
        if (t.getCause() != null) {
            return t.getCause();
        }
        return t;
    }

    private static Throwable privateGetApplicationExceptionCause(Throwable t) {
        if (t == null) {
            return null;
        }
        Throwable cause = t.getCause();
        if (cause == null) {
            return t;
        }
        if (cause instanceof InvocationTargetException) {
            return privateGetCause(cause);
        }
        return cause;
    }

    public static String getMessage(Throwable ex) {
        if (ex == null) {
            return "";
        }
        Throwable cause = getCause(ex);
        String message = cause.getMessage();
        if (message == null) {
            message = cause.toString();
        }
        return message;
    }
}
