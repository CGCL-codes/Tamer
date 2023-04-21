package net.bull.javamelody;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Handler pour les logs de java.util.logging, configuré automatiquement par {@link MonitoringFilter}.
 * @author Emeric Vernat
 */
public class LoggingHandler extends Handler {

    private static final Level THRESHOLD = Level.WARNING;

    private static final Counter LOG_COUNTER = new Counter(Counter.LOG_COUNTER_NAME, "log.png");

    static {
        LOG_COUNTER.setMaxRequestsCount(500);
    }

    private static final LoggingHandler SINGLETON = new LoggingHandler();

    /**
	 * Constructeur.
	 */
    public LoggingHandler() {
        super();
    }

    static LoggingHandler getSingleton() {
        return SINGLETON;
    }

    static Counter getLogCounter() {
        return LOG_COUNTER;
    }

    static void addErrorLogToCounter(String message, Throwable throwable) {
        if (throwable == null) {
            addErrorLogToCounter(message, (String) null);
        } else {
            final StringWriter stackTrace = new StringWriter(200);
            throwable.printStackTrace(new PrintWriter(stackTrace));
            addErrorLogToCounter(message, stackTrace.toString());
        }
    }

    static void addErrorLogToCounter(String message, String throwableStackTrace) {
        LOG_COUNTER.addRequestForSystemError(message, -1, -1, throwableStackTrace);
    }

    void register() {
        for (final String name : Collections.list(LogManager.getLogManager().getLoggerNames())) {
            Logger.getLogger(name).addHandler(this);
        }
    }

    void deregister() {
        for (final String name : Collections.list(LogManager.getLogManager().getLoggerNames())) {
            Logger.getLogger(name).removeHandler(this);
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void publish(LogRecord record) {
        if (record.getLevel().intValue() < THRESHOLD.intValue()) {
            return;
        }
        addErrorLogToCounter(record.getLevel().getName() + ": " + record.getMessage(), record.getThrown());
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void close() {
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void flush() {
    }
}
