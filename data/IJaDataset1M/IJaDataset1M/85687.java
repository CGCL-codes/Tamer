package com.informa.utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

public class LogUtils {

    public static String getStackTrace(Throwable exception) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        exception.printStackTrace(new PrintWriter(out));
        String stackTrace = out.toString();
        return stackTrace;
    }
}
