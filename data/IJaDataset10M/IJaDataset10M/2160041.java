package br.com.felix.fwt.log;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.LoggingEvent;

public class FelixConsoleLogger extends Logger {

    private static Map<String, FelixConsoleLogger> mapLoggers = new HashMap<String, FelixConsoleLogger>();

    protected FelixConsoleLogger() {
        super(FelixConsoleLogger.class.getName());
    }

    @SuppressWarnings("unchecked")
    public static FelixConsoleLogger getLogger(Class clazz) {
        FelixConsoleLogger logger = mapLoggers.get(clazz);
        if (logger == null) {
            logger = new FelixConsoleLogger();
            mapLoggers.put(clazz.getCanonicalName(), logger);
        }
        return logger;
    }

    private void log(String severity, Object message, Throwable t) {
        System.out.printf("[%s] %s %s\n", severity, message, t);
    }

    private void log(String string, Object message) {
        System.out.printf("[%s] %s\n", string, message);
    }

    @Override
    public void debug(Object message, Throwable t) {
        log("debug", message, t);
    }

    @Override
    public void debug(Object message) {
        log("debug", message);
    }

    @Override
    public void error(Object message, Throwable t) {
        log("error", message, t);
    }

    @Override
    public void error(Object message) {
        log("error", message);
    }

    @Override
    public void info(Object message, Throwable t) {
        log("info", message, t);
    }

    @Override
    public void info(Object message) {
        log("info", message);
    }

    @Override
    public void warn(Object message, Throwable t) {
        log("warn", message, t);
    }

    @Override
    public void warn(Object message) {
        log("warn", message);
    }

    @Override
    public void fatal(Object message, Throwable t) {
        log("fatal", message, t);
    }

    @Override
    public void fatal(Object message) {
        log("fatal", message);
    }

    @Override
    public synchronized void addAppender(Appender newAppender) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void assertLog(boolean assertion, String msg) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void callAppenders(LoggingEvent event) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void forcedLog(String fqcn, Priority level, Object message, Throwable t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getAdditivity() {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized Enumeration getAllAppenders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized Appender getAppender(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Priority getChainedPriority() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Level getEffectiveLevel() {
        throw new UnsupportedOperationException();
    }

    @Override
    public LoggerRepository getHierarchy() {
        throw new UnsupportedOperationException();
    }

    @Override
    public LoggerRepository getLoggerRepository() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResourceBundle getResourceBundle() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected String getResourceBundleString(String key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isAttached(Appender appender) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDebugEnabled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEnabledFor(Priority level) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isInfoEnabled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void l7dlog(Priority priority, String key, Object[] params, Throwable t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void l7dlog(Priority priority, String key, Throwable t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void log(Priority priority, Object message, Throwable t) {
        System.out.printf("[%s]:::%s:::%s\n", priority, message, t);
    }

    @Override
    public void log(Priority priority, Object message) {
        System.out.printf("[%s]:::%s\n", priority, message);
    }

    @Override
    public void log(String callerFQCN, Priority level, Object message, Throwable t) {
        System.out.printf("<%s>[%s]:::%s:::%s\n", callerFQCN, level, message, t);
    }

    @Override
    public synchronized void removeAllAppenders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized void removeAppender(Appender appender) {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized void removeAppender(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAdditivity(boolean additive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setResourceBundle(ResourceBundle bundle) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException();
    }
}
