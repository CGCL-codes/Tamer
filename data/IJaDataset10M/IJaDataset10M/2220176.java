package com.mycila.log;

import static com.mycila.log.Level.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class AbstractLogger implements Logger {

    public boolean canTrace() {
        return canLog(TRACE);
    }

    public final void trace(Object message, Object... args) {
        if (canLog(TRACE)) doLog(TRACE, null, message, args);
    }

    public final void trace(Throwable throwable, Object message, Object... args) {
        if (canLog(TRACE)) doLog(TRACE, throwable, message, args);
    }

    public final boolean canDebug() {
        return canLog(DEBUG);
    }

    public final void debug(Object message, Object... args) {
        if (canLog(DEBUG)) doLog(DEBUG, null, message, args);
    }

    public final void debug(Throwable throwable, Object message, Object... args) {
        if (canLog(DEBUG)) doLog(DEBUG, throwable, message, args);
    }

    public final boolean canInfo() {
        return canLog(INFO);
    }

    public final void info(Object message, Object... args) {
        if (canLog(INFO)) doLog(INFO, null, message, args);
    }

    public final void info(Throwable throwable, Object message, Object... args) {
        if (canLog(INFO)) doLog(INFO, throwable, message, args);
    }

    public final boolean canWarn() {
        return canLog(WARN);
    }

    public final void warn(Object message, Object... args) {
        if (canLog(WARN)) doLog(WARN, null, message, args);
    }

    public final void warn(Throwable throwable, Object message, Object... args) {
        if (canLog(WARN)) doLog(WARN, throwable, message, args);
    }

    public final boolean canError() {
        return canLog(ERROR);
    }

    public final void error(Object message, Object... args) {
        if (canLog(ERROR)) doLog(ERROR, null, message, args);
    }

    public final void error(Throwable throwable, Object message, Object... args) {
        if (canLog(ERROR)) doLog(ERROR, throwable, message, args);
    }

    public final void log(Level level, Object message, Object... args) {
        if (canLog(level)) doLog(level, null, message, args);
    }

    public final void log(Level level, Throwable throwable, Object message, Object... args) {
        if (canLog(level)) doLog(level, throwable, message, args);
    }

    public void debug(Object message) {
        if (canLog(DEBUG)) doLog(DEBUG, null, message);
    }

    public void debug(Throwable throwable, Object message) {
        if (canLog(DEBUG)) doLog(DEBUG, throwable, message);
    }

    public void error(Object message) {
        if (canLog(ERROR)) doLog(ERROR, null, message);
    }

    public void error(Throwable throwable, Object message) {
        if (canLog(ERROR)) doLog(ERROR, throwable, message);
    }

    public void info(Object message) {
        if (canLog(INFO)) doLog(INFO, null, message);
    }

    public void info(Throwable throwable, Object message) {
        if (canLog(INFO)) doLog(INFO, throwable, message);
    }

    public void log(Level level, Object message) {
        if (canLog(level)) doLog(level, null, message);
    }

    public void log(Level level, Throwable throwable, Object message) {
        if (canLog(level)) doLog(level, throwable, message);
    }

    public void trace(Object message) {
        if (canLog(TRACE)) doLog(TRACE, null, message);
    }

    public void trace(Throwable throwable, Object message) {
        if (canLog(TRACE)) doLog(TRACE, throwable, message);
    }

    public void warn(Object message) {
        if (canLog(WARN)) doLog(WARN, null, message);
    }

    public void warn(Throwable throwable, Object message) {
        if (canLog(WARN)) doLog(WARN, throwable, message);
    }

    protected abstract void doLog(Level level, Throwable throwable, Object message, Object... args);
}
