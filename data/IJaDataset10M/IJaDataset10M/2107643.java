package utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Logging wrapper class that uses Jakarta Log4J.
 *
 * @author Kimmo Saarinen, Cidercone
 */
public class Log {

    /**
     * Logging logger names
     */
    public static final String GENERAL = "wabongoSuomi.general";

    public static final String DEV = "wabongoSuomi.dev";

    public static final String ADMIN = "wabongoSuomi.admin";

    public static final String DB = "wabongoSuomi.db";

    /**
     * Initializes the Jakarta Log4J logging mechanism by loading a property file
     * that contains the logging configuration information.
     * <p/>
     * <p>Log4J starts a thread to watch the changes of the property file, so
     * the modifications are updated automaticly and there is no need to restart
     * the servlet container.
     *
     * @param filepath absolute path to config properties
     */
    public static void initialize(String filepath) {
        PropertyConfigurator.configureAndWatch(filepath);
        Log.info(Log.GENERAL, "Logging mechamism initialised");
    }

    /** */
    public static void debug(String logger, String text) {
        Logger.getLogger(logger).debug(text);
    }

    /** */
    public static void info(String logger, String text) {
        Logger.getLogger(logger).info(text);
    }

    /** */
    public static void warn(String logger, String text) {
        Logger.getLogger(logger).warn(text);
    }

    /** */
    public static void error(String logger, String text) {
        Logger.getLogger(logger).error(text);
        sendEmail("ERROR", text);
    }

    /** */
    public static void error(String logger, String text, Throwable e) {
        Logger.getLogger(logger).error(text, e);
        sendEmail("ERROR", text + "\n\n" + e.toString() + "\n\n" + getStackTrace(e));
    }

    /** */
    public static void fatal(String logger, String text) {
        Logger.getLogger(logger).fatal(text);
        sendEmail("FATAL", text);
    }

    /** */
    public static void fatal(String logger, String text, Throwable e) {
        Logger.getLogger(logger).fatal(text, e);
        sendEmail("FATAL", text + "\n\n" + e.toString() + "\n\n" + getStackTrace(e));
    }

    /**
     * Error email header text
     */
    private static final String errorHeader = "This is automatic email reporting a software error. Do not response to this email.\n" + "\n" + System.getProperty("java.runtime.name") + " " + "version " + System.getProperty("java.runtime.version") + "\n" + System.getProperty("java.vm.name") + " " + "version " + System.getProperty("java.vm.version") + "\n" + System.getProperty("os.name") + " " + System.getProperty("os.version") + " " + System.getProperty("os.arch") + "\n" + "started by " + System.getProperty("user.name") + " " + "from " + System.getProperty("user.dir") + "\n" + "\n";

    /** */
    private static void sendEmail(String level, String text) {
        Config config = Config.getInstance();
        if (config == null) {
            return;
        }
        String errorRecipients = config.getValue("errorEmailToAddresses", (String) null);
        String from = config.getValue("emailFrom", "nobody@localhost");
        String environment = config.getValue("environment", "(environment not defined)");
        String host = config.getValue("smtpHost");
        String message = errorHeader + environment + "\n\n" + text;
        String subject = level;
        if (errorRecipients != null && errorRecipients.length() > 0) {
            MailSender sender = new MailSender(host);
            sender.send(subject, message, from, errorRecipients);
        }
    }

    /** */
    private static String getStackTrace(Throwable t) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintStream stream = new PrintStream(out, true);
            t.printStackTrace(stream);
            String s = out.toString();
            stream.close();
            out.close();
            return s;
        } catch (IOException e) {
            return null;
        }
    }

    public static void warn(String logger, String message, Throwable error) {
        Logger.getLogger(logger).warn(message, error);
    }
}
