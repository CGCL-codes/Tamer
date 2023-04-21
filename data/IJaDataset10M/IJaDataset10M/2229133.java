package net.sourceforge.javautil.common.logging.standard;

import java.io.Writer;
import net.sourceforge.javautil.common.logging.ILogger;
import net.sourceforge.javautil.common.logging.ILoggerLevel;
import net.sourceforge.javautil.common.logging.LoggerLevelStandard;
import net.sourceforge.javautil.common.logging.LoggerWriter;
import net.sourceforge.javautil.common.logging.ILoggingFramework;
import net.sourceforge.javautil.common.logging.LoggingFrameworkRouter;

/**
 * A simple no-op implementation stub.
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public class NoOperationLogger implements ILoggingFramework<Object>, ILogger, LoggingFrameworkRouter {

    public ILogger getLogger(String name) {
        return this;
    }

    public ILoggerLevel[] getLoggingLevels() {
        return LoggerLevelStandard.values();
    }

    public void route(String loggerName, long millis, ILoggerLevel level, String message, Throwable throwable) {
    }

    public LoggingFrameworkRouter getRouter() {
        return this;
    }

    public void routeTo(ILoggingFramework framework) {
    }

    public void initialize(Object configuration) {
    }

    public void reconfigure(Object configuration) {
    }

    public void debug(String message, Throwable throwable) {
    }

    public void debug(String message) {
    }

    public void error(String message, Throwable throwable) {
    }

    public void error(String message) {
    }

    public void fatal(String message, Throwable throwable) {
    }

    public void fatal(String message) {
    }

    public ILoggingFramework getFramework() {
        return null;
    }

    public Writer getLoggingWriter(ILoggerLevel level) {
        return new LoggerWriter(this, level);
    }

    public String getFullyQualifiedName() {
        return "NoOp FQN";
    }

    public String getName() {
        return "NoOp";
    }

    public void info(String message, Throwable throwable) {
    }

    public void info(String message) {
    }

    public boolean isDebug() {
        return false;
    }

    public boolean isError() {
        return false;
    }

    public boolean isFatal() {
        return false;
    }

    public boolean isInfo() {
        return false;
    }

    public boolean isLogging() {
        return false;
    }

    public boolean isLogging(ILoggerLevel level) {
        return false;
    }

    public boolean isWarn() {
        return false;
    }

    public void log(ILoggerLevel level, String message, Throwable throwable) {
    }

    public void log(ILoggerLevel level, String message) {
    }

    public void warn(String message, Throwable throwable) {
    }

    public void warn(String message) {
    }
}
