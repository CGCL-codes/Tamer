package org.modulefusion.dirinstaller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.util.Date;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

public class Logger {

    private static Logger instance;

    private final ServiceTracker logTracker;

    private final DateFormat dateFormatter;

    private final BundleContext context;

    private Logger(BundleContext context) {
        this.logTracker = new ServiceTracker(context, LogService.class.getName(), null);
        this.logTracker.open();
        dateFormatter = DateFormat.getTimeInstance(DateFormat.MEDIUM);
        this.context = context;
    }

    public static Logger getLogger(BundleContext context) {
        synchronized (context) {
            if (instance == null) {
                synchronized (context) {
                    if (instance == null) {
                        instance = new Logger(context);
                    }
                }
            }
        }
        return instance;
    }

    protected void log(int logLevel, String logLevelName, String message, Throwable t) {
        LogService log = (LogService) logTracker.getService();
        if (log == null) {
            if (t == null) {
                StringBuffer output = new StringBuffer(dateFormatter.format(new Date()));
                output.append(" ").append(logLevelName).append(" ");
                output.append("[").append(context.getBundle().getSymbolicName()).append("] - ").append(message);
                System.out.println(output.toString());
            } else {
                StringWriter writer = new StringWriter();
                t.printStackTrace(new PrintWriter(writer));
                System.out.println(message + "\n" + writer.toString());
            }
        } else {
            log.log(logLevel, message, t);
        }
    }

    public void info(String message) {
        info(message, null);
    }

    public void info(String message, Throwable t) {
        log(LogService.LOG_INFO, "INFO", message, t);
    }

    public void debug(String message) {
        debug(message, null);
    }

    public void debug(String message, Throwable t) {
        log(LogService.LOG_DEBUG, "DEBUG", message, t);
    }

    public void error(String message) {
        error(message, null);
    }

    public void error(String message, Throwable t) {
        log(LogService.LOG_ERROR, "ERROR", message, t);
    }

    public void warn(String message) {
        warn(message, null);
    }

    public void warn(String message, Throwable t) {
        log(LogService.LOG_WARNING, "WARNING", message, t);
    }
}
