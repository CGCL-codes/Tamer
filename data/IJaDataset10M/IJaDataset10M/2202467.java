package org.xfap.util;

public interface LogWriter {

    void log(int level, String message, Exception exception);

    void setLogLevel(int i);
}
