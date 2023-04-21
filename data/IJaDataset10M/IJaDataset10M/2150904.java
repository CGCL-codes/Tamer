package uk.co.nimp.scard;

import java.io.IOException;

/**
 *
 * @author sebastien.riou
 */
public interface WithBasicLoggers {

    boolean areLogsEnabled();

    void clearLogs();

    void closeAllLogs() throws IOException;

    void flushAllLogs() throws IOException;

    boolean isLoggingOnSystemOut();

    void log(Throwable e);

    void log(int eventType, String msg);

    void logAsWarning(Throwable e);

    void logLine(int eventType, String msg);

    void setLogsEnabled(boolean enable);
}
