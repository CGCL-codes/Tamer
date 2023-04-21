package org.columba.core.scripting;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

/**
    @author Celso Pinto (cpinto@yimports.com)
 */
public class ScriptLogger extends Observable {

    public static class LogEntry {

        private String message, details;

        public LogEntry() {
            this("", "");
        }

        public LogEntry(String message) {
            this(message, "");
        }

        public LogEntry(String message, String details) {
            this.message = message;
            this.details = details;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }
    }

    private LinkedList<LogEntry> logger;

    private static ScriptLogger self = null;

    private static final int MAX_LOG_ENTRIES = 200;

    private ScriptLogger() {
        logger = new LinkedList<LogEntry>();
    }

    public static ScriptLogger getInstance() {
        if (self == null) self = new ScriptLogger();
        return self;
    }

    public void append(String message, Exception details) {
        StringWriter writer = new StringWriter();
        details.printStackTrace(new PrintWriter(writer));
        append(message, writer.toString());
    }

    public void append(String message, String details) {
        append(new LogEntry(message, details));
    }

    public void append(String message) {
        append(message, "");
    }

    public void append(LogEntry entry) {
        logger.addFirst(entry);
        if (logger.size() > MAX_LOG_ENTRIES) logger.remove(0);
        setChanged();
        notifyObservers(entry);
    }

    public void clear() {
        logger.clear();
    }

    public List<LogEntry> dumpCurrentLog() {
        return Collections.unmodifiableList(logger);
    }
}
