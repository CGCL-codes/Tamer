package net.sourceforge.javautil.common.logging;

import java.io.Writer;
import net.sourceforge.javautil.common.Refreshable;

/**
 * A logger that can be refreshed and will retreive the 
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public class LoggerContextual implements ILogger, Refreshable {

    protected final String name;

    public LoggerContextual(String name) {
        this.name = name;
    }

    /**
	 * @return The logger that should be used
	 */
    protected ILogger getCurrent() {
        return LoggingContext.getLogger(name);
    }

    @Override
    public void refresh() {
        LoggingContext.getLogger(getCurrent().getFullyQualifiedName());
    }

    public ILoggingFramework getFramework() {
        return getCurrent().getFramework();
    }

    public String getName() {
        return getCurrent().getName();
    }

    public String getFullyQualifiedName() {
        return getCurrent().getFullyQualifiedName();
    }

    public boolean isLogging() {
        return getCurrent().isLogging();
    }

    public boolean isLogging(ILoggerLevel level) {
        return getCurrent().isLogging(level);
    }

    public boolean isDebug() {
        return getCurrent().isDebug();
    }

    public boolean isInfo() {
        return getCurrent().isInfo();
    }

    public boolean isWarn() {
        return getCurrent().isWarn();
    }

    public boolean isError() {
        return getCurrent().isError();
    }

    public boolean isFatal() {
        return getCurrent().isFatal();
    }

    public void log(ILoggerLevel level, String message) {
        getCurrent().log(level, message);
    }

    public void log(ILoggerLevel level, String message, Throwable throwable) {
        getCurrent().log(level, message, throwable);
    }

    public void debug(String message) {
        getCurrent().debug(message);
    }

    public void debug(String message, Throwable throwable) {
        getCurrent().debug(message, throwable);
    }

    public void info(String message) {
        getCurrent().info(message);
    }

    public void info(String message, Throwable throwable) {
        getCurrent().info(message, throwable);
    }

    public void warn(String message) {
        getCurrent().warn(message);
    }

    public void warn(String message, Throwable throwable) {
        getCurrent().warn(message, throwable);
    }

    public void error(String message) {
        getCurrent().error(message);
    }

    public void error(String message, Throwable throwable) {
        getCurrent().error(message, throwable);
    }

    public void fatal(String message) {
        getCurrent().fatal(message);
    }

    public void fatal(String message, Throwable throwable) {
        getCurrent().fatal(message, throwable);
    }

    public Writer getLoggingWriter(ILoggerLevel level) {
        return getCurrent().getLoggingWriter(level);
    }
}
