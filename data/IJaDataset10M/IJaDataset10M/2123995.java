package com.iparelan.util.logging.impl;

import static java.util.logging.Level.CONFIG;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.FINER;
import static java.util.logging.Level.FINEST;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import com.iparelan.util.annotations.Copyright;
import com.iparelan.util.annotations.patterns.Decorator;

/**
 * A <a
 * href="http://java.sun.com/javase/6/docs/technotes/guides/logging/">Java
 * Logging API</a> {@link com.iparelan.util.logging.Logger} implementation.
 *
 * @author Greg Mattes
 * @version September 2008
 */
@Decorator
@SuppressWarnings("PMD.TooManyMethods")
@Copyright("Copyright &copy; 2008, Iparelan Solutions, LLC. All rights reserved.")
public final class LoggerImpl implements com.iparelan.util.logging.Logger {

    /**
     * Stores known loggers.
     */
    private static final ConcurrentMap<String, com.iparelan.util.logging.Logger> LOGGER_CACHE = new ConcurrentHashMap<String, com.iparelan.util.logging.Logger>();

    /**
     * The Java platform logger that implements this logger.
     */
    private final java.util.logging.Logger decorated;

    /**
     * Creates a logger.
     *
     * @param name
     *
     *        The name of the logger. May not be {@code null}.
     */
    private LoggerImpl(final String name) {
        this.decorated = java.util.logging.Logger.getLogger(name);
    }

    /**
     * Produces the named logger.
     *
     * @param name
     *
     *        The name of the logger. May not be {@code null}.
     *
     * @return The logger for {@code name}.
     */
    public static com.iparelan.util.logging.Logger getLogger(final String name) {
        final com.iparelan.util.logging.Logger logger;
        final com.iparelan.util.logging.Logger cacheLookup = LOGGER_CACHE.get(name);
        final boolean cacheHitOnLookup = (cacheLookup != null);
        if (cacheHitOnLookup) {
            logger = cacheLookup;
        } else {
            final com.iparelan.util.logging.Logger newLogger = new LoggerImpl(name);
            final com.iparelan.util.logging.Logger cacheInsert = LOGGER_CACHE.putIfAbsent(name, newLogger);
            final boolean cacheHitOnInsert = (cacheInsert != null);
            logger = cacheHitOnInsert ? cacheInsert : newLogger;
        }
        return logger;
    }

    @Override
    public void config(final String msg) {
        decorated.log(CONFIG, msg);
    }

    @Override
    public void config(final String msg, final Throwable throwable) {
        decorated.log(CONFIG, msg, throwable);
    }

    @Override
    public void fine(final String msg) {
        decorated.log(FINE, msg);
    }

    @Override
    public void fine(final String msg, final Throwable throwable) {
        decorated.log(FINE, msg, throwable);
    }

    @Override
    public void finer(final String msg) {
        decorated.log(FINER, msg);
    }

    @Override
    public void finer(final String msg, final Throwable throwable) {
        decorated.log(FINER, msg, throwable);
    }

    @Override
    public void finest(final String msg) {
        decorated.log(FINEST, msg);
    }

    @Override
    public void finest(final String msg, final Throwable throwable) {
        decorated.log(FINEST, msg, throwable);
    }

    @Override
    public void info(final String msg) {
        decorated.log(INFO, msg);
    }

    @Override
    public void info(final String msg, final Throwable throwable) {
        decorated.log(INFO, msg, throwable);
    }

    @Override
    public void severe(final String msg) {
        decorated.log(SEVERE, msg);
    }

    @Override
    public void severe(final String msg, final Throwable throwable) {
        decorated.log(SEVERE, msg, throwable);
    }

    @Override
    public void warning(final String msg) {
        decorated.log(WARNING, msg);
    }

    @Override
    public void warning(final String msg, final Throwable throwable) {
        decorated.log(WARNING, msg, throwable);
    }

    @Override
    public boolean isLoggableConfig() {
        return decorated.isLoggable(CONFIG);
    }

    @Override
    public boolean isLoggableFine() {
        return decorated.isLoggable(FINE);
    }

    @Override
    public boolean isLoggableFiner() {
        return decorated.isLoggable(FINER);
    }

    @Override
    public boolean isLoggableFinest() {
        return decorated.isLoggable(FINEST);
    }

    @Override
    public boolean isLoggableInfo() {
        return decorated.isLoggable(INFO);
    }

    @Override
    public boolean isLoggableSevere() {
        return decorated.isLoggable(SEVERE);
    }

    @Override
    public boolean isLoggableWarning() {
        return decorated.isLoggable(WARNING);
    }
}
