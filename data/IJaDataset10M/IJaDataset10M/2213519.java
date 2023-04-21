package org.gwm.splice.client.logger;

import java.util.ArrayList;
import java.util.Iterator;

public class Logger implements ILogger {

    private static ILogger instance;

    private ArrayList listeners = new ArrayList();

    public static ILogger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    private Logger() {
    }

    public void addLogListener(ILogListener listener) {
        listeners.add(listener);
    }

    public void removeLogListener(ILogListener listener) {
        listeners.remove(listener);
    }

    public void log(LogItem item) {
        for (Iterator iter = listeners.iterator(); iter.hasNext(); ) {
            ILogListener listener = (ILogListener) iter.next();
            listener.itemLogged(item);
        }
    }

    public void log(int level, String message, Object source) {
        log(new LogItem(level, message, source));
    }

    public void log(int level, String message) {
        log(new LogItem(level, message));
    }

    public void logInfo(String message) {
        log(LogItem.INFO, message);
    }

    public void logInfo(String message, Object source) {
        log(LogItem.INFO, message, source);
    }

    public void logWarning(String message) {
        log(LogItem.WARN, message);
    }

    public void logWarning(String message, Object source) {
        log(LogItem.WARN, message, source);
    }

    public void logError(String message) {
        log(LogItem.ERROR, message);
    }

    public void logError(String message, Object source) {
        log(LogItem.ERROR, message, source);
    }

    public void logSevere(String message) {
        log(LogItem.SEVERE, message);
    }

    public void logSevere(String message, Object source) {
        log(LogItem.SEVERE, message, source);
    }

    public void logFatal(String message) {
        log(LogItem.FATAL, message);
    }

    public void logFatal(String message, Object source) {
        log(LogItem.FATAL, message, source);
    }
}
