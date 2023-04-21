package com.quesofttech.web.common;

public class ExceptionUtil {

    public static String getRootCause(Throwable t) {
        Throwable cause = t;
        Throwable subCause = cause.getCause();
        while (subCause != null && !subCause.equals(cause)) {
            cause = subCause;
            subCause = cause.getCause();
        }
        return cause.getMessage();
    }
}
