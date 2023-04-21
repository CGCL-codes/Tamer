package org.systemsbiology.apps.corragui.server.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.ResourceBundle;
import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.LoggingEvent;
import org.systemsbiology.apps.corragui.domain.Location;
import org.systemsbiology.apps.corragui.domain.User;
import org.systemsbiology.apps.corragui.server.provider.location.LocationInfoProvider;
import org.systemsbiology.apps.corragui.server.util.StringUtils;

/**
 * Class for logging messages to a user's project directory. 
 * @author Vagisha Sharma
 */
public class CorraLogger extends Logger {

    private enum LogLevel {

        DEBUG, INFO, WARN, ERROR, FATAL
    }

    ;

    private static final DateFormat dateFormat = DateFormat.getDateTimeInstance();

    private final String logFile;

    private final Class clazz;

    public CorraLogger(Class clazz, User user, String projName) {
        super(clazz.getName());
        this.clazz = clazz;
        Location projLocation = LocationInfoProvider.instance().getUserProjectLocation(user, projName);
        String projDir = projLocation.pathForWebServer();
        logFile = projDir + File.separatorChar + "corra.log";
    }

    public void debug(Object message) {
        logMessage(message, LogLevel.DEBUG);
    }

    public void debug(Object message, Throwable t) {
        logMessage(message + "\n" + StringUtils.getStackTrace(t), LogLevel.DEBUG);
    }

    public void info(Object message) {
        logMessage(message, LogLevel.INFO);
    }

    public void info(Object message, Throwable t) {
        logMessage(message + "\n" + StringUtils.getStackTrace(t), LogLevel.INFO);
    }

    public void warn(Object message) {
        logMessage(message, LogLevel.WARN);
    }

    public void warn(Object message, Throwable t) {
        logMessage(message + "\n" + StringUtils.getStackTrace(t), LogLevel.WARN);
    }

    public void error(Object message) {
        logMessage(message, LogLevel.ERROR);
    }

    public void error(Object message, Throwable t) {
        logMessage(message + "\n" + StringUtils.getStackTrace(t), LogLevel.ERROR);
    }

    public void fatal(Object message) {
        logMessage(message, LogLevel.FATAL);
    }

    public void fatal(Object message, Throwable t) {
        logMessage(message + "\n" + StringUtils.getStackTrace(t), LogLevel.FATAL);
    }

    private void logMessage(Object message, LogLevel level) {
        String formattedMsg = getFormattedMessage(message, level);
        writeMessage(formattedMsg);
    }

    private String getFormattedMessage(Object toFormat, LogLevel level) {
        StringBuilder buf = new StringBuilder();
        buf.append(level.toString());
        buf.append("  ");
        buf.append(getDateTime());
        buf.append("  ");
        buf.append(clazz.getName());
        buf.append("\n");
        buf.append(toFormat);
        buf.append("\n");
        return buf.toString();
    }

    private String getDateTime() {
        StringBuilder buf = new StringBuilder();
        buf.append("(");
        buf.append(dateFormat.format(new Date()));
        buf.append(")");
        return buf.toString();
    }

    private void writeMessage(String formattedMsg) {
        if (formattedMsg == null) return;
        PrintWriter writer = null;
        try {
            FileWriter fw = new FileWriter(logFile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            writer = new PrintWriter(bw);
            writer.write(formattedMsg);
        } catch (FileNotFoundException e) {
            System.err.println(e.getStackTrace());
            System.err.println("CANNOT LOG TO corra.log (" + logFile + ")");
        } catch (IOException e) {
            System.err.println(e.getStackTrace());
            System.err.println("CANNOT LOG TO corra.log (" + logFile + ")");
        } finally {
            if (writer != null) writer.close();
        }
    }

    private String unsupported(String methodName) {
        return CorraLogger.class.getName() + " does not support " + methodName + " method";
    }

    public boolean isTraceEnabled() {
        throw new UnsupportedOperationException(unsupported("isTraceEnabled"));
    }

    public void trace(Object message) {
        throw new UnsupportedOperationException(unsupported("trace"));
    }

    public void trace(Object message, Throwable t) {
        throw new UnsupportedOperationException(unsupported("trace"));
    }

    public synchronized void addAppender(Appender newAppender) {
        throw new UnsupportedOperationException(unsupported("addAppender"));
    }

    public void assertLog(boolean assertion, String msg) {
        throw new UnsupportedOperationException(unsupported("assertLog"));
    }

    public void callAppenders(LoggingEvent event) {
        throw new UnsupportedOperationException(unsupported("callAppenders"));
    }

    protected void forcedLog(String fqcn, Priority level, Object message, Throwable t) {
        throw new UnsupportedOperationException(unsupported("forcedLog"));
    }

    public boolean getAdditivity() {
        throw new UnsupportedOperationException(unsupported("getAdditivity"));
    }

    public synchronized Enumeration getAllAppenders() {
        throw new UnsupportedOperationException(unsupported("getAllAppenders"));
    }

    public synchronized Appender getAppender(String name) {
        throw new UnsupportedOperationException(unsupported("getAppender"));
    }

    public Priority getChainedPriority() {
        throw new UnsupportedOperationException(unsupported("getChainedPriority"));
    }

    public Level getEffectiveLevel() {
        throw new UnsupportedOperationException(unsupported("getEffectiveLevel"));
    }

    public LoggerRepository getHierarchy() {
        throw new UnsupportedOperationException(unsupported("getHierarchy"));
    }

    public LoggerRepository getLoggerRepository() {
        throw new UnsupportedOperationException(unsupported("getLoggerRepository"));
    }

    public ResourceBundle getResourceBundle() {
        throw new UnsupportedOperationException(unsupported("getResourceBundle"));
    }

    protected String getResourceBundleString(String key) {
        throw new UnsupportedOperationException(unsupported("getResourceBundleString"));
    }

    public boolean isAttached(Appender appender) {
        throw new UnsupportedOperationException(unsupported("isAttached"));
    }

    public boolean isDebugEnabled() {
        throw new UnsupportedOperationException(unsupported("isDebugEnabled"));
    }

    public boolean isEnabledFor(Priority level) {
        throw new UnsupportedOperationException(unsupported("isEnabled"));
    }

    public boolean isInfoEnabled() {
        throw new UnsupportedOperationException(unsupported("isInfoEnabled"));
    }

    public void l7dlog(Priority priority, String key, Object[] params, Throwable t) {
        throw new UnsupportedOperationException(unsupported("17dlog"));
    }

    public void l7dlog(Priority priority, String key, Throwable t) {
        throw new UnsupportedOperationException(unsupported("17dlog"));
    }

    public void log(Priority priority, Object message, Throwable t) {
        throw new UnsupportedOperationException(unsupported("log"));
    }

    public void log(Priority priority, Object message) {
        throw new UnsupportedOperationException(unsupported("log"));
    }

    public void log(String callerFQCN, Priority level, Object message, Throwable t) {
        throw new UnsupportedOperationException(unsupported("log"));
    }

    public synchronized void removeAllAppenders() {
        throw new UnsupportedOperationException(unsupported("removeAllAppenders"));
    }

    public synchronized void removeAppender(Appender appender) {
        throw new UnsupportedOperationException(unsupported("removeAppender"));
    }

    public synchronized void removeAppender(String name) {
        throw new UnsupportedOperationException(unsupported("removeAppender"));
    }

    public void setAdditivity(boolean additive) {
        throw new UnsupportedOperationException(unsupported("setAdditivity"));
    }

    public void setLevel(Level level) {
        throw new UnsupportedOperationException(unsupported("setLevel"));
    }

    public void setPriority(Priority priority) {
        throw new UnsupportedOperationException(unsupported("setPriority"));
    }

    public void setResourceBundle(ResourceBundle bundle) {
        throw new UnsupportedOperationException(unsupported("setResourceBundle"));
    }
}
