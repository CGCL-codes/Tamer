package de.freeradical.jpackrat2;

import java.util.ArrayList;
import java.util.List;

public class Logger {

    public class LogEvent {

        private Object sender;

        private String message;

        public String getMessage() {
            return message;
        }

        public Object getSender() {
            return sender;
        }

        public LogEvent(Object sender, String message) {
            this.sender = sender;
            this.message = message;
        }
    }

    public interface LogListener {

        void debug(LogEvent e);

        void info(LogEvent e);

        void error(LogEvent e);
    }

    private List<LogListener> listener;

    private static Logger logger = null;

    public static Logger getLogger() {
        if (logger == null) {
            logger = new Logger();
        }
        return logger;
    }

    private Logger() {
        listener = new ArrayList<LogListener>();
    }

    public void debug(Object cls, String message) {
        LogEvent e = new LogEvent(cls, message);
        for (LogListener l : listener) l.debug(e);
    }

    public void info(Object cls, String message) {
        LogEvent e = new LogEvent(cls, message);
        for (LogListener l : listener) l.info(e);
    }

    public void error(Object cls, String message) {
        LogEvent e = new LogEvent(cls, message);
        for (LogListener l : listener) l.error(e);
    }

    public void addListener(LogListener l) {
        listener.add(l);
    }

    public void removeListener(LogListener l) {
        listener.remove(l);
    }
}
