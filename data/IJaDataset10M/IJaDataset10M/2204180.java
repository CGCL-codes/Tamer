package dnl.logging;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * The central light-log class.<br>
 * This logger uses the stack in order to find out what method+class
 * called it and using this data add the caller's info.<br>
 * Note that the use of the stack has impact on performance, therefore this
 * kind of logger is good only for code which is not 'under pressure'.<br>
 * Also, thanks to the above features, this class is used statically.
 * 
 * @author <a href="mailto:daniel.or@gmail.com">Daniel Or</a>
 */
public class LightLog {

    private static final String LOG4JREDIRECTOR_CLASS = "ajul.logging.Log4jRedirector";

    private LightLog() {
    }

    @SuppressWarnings("unused")
    private static boolean printShortClassNames = true;

    private static int thresholdLevel = LightLogLevel.INFO.getValue();

    private static Appender appender = new PrintStreamAppender();

    public static void setThresholdLevel(LightLogLevel level) {
        thresholdLevel = level.getValue();
    }

    /**
	 * Redirects <code>RLogger</code> output to log4j.  
	 */
    public static void useLog4j() {
        try {
            Class<?> clazz = Class.forName(LOG4JREDIRECTOR_CLASS);
            appender = (Appender) clazz.newInstance();
        } catch (Exception e) {
            System.err.println("Failed loading log4j. Reason: " + e.getMessage());
        }
    }

    /**
	 * After this method is alled, no logs will be logged.
	 */
    public static void shutUp() {
        appender = new SilentAppender();
    }

    public static void useSysoutLogger() {
        appender = new PrintStreamAppender();
    }

    /**
	 * 
	 * @param outputFile
	 *            an output file to log to.
	 * @param noConsole
	 *            if <code>true</code> the logs will output only to a file and
	 *            will not appear in the console.
	 * @throws IOException
	 *             for any IO problem.
	 */
    public static void printToFile(File outputFile, boolean outputToConsole) throws IOException {
        FileOutputStream fos = new FileOutputStream(outputFile);
        PrintStream printStream = new PrintStream(fos, true);
        appender = new PrintStreamAppender(printStream);
        if (outputToConsole) {
            appender = new AppendersAddition(appender, new PrintStreamAppender());
        }
    }

    protected static Appender getLogger() {
        return appender;
    }

    /**
	 * 
	 * @param shortNames
	 */
    public static void setShowShortNames(boolean shortNames) {
        printShortClassNames = shortNames;
    }

    public static void debug(String s) {
        if (LightLogLevel.DEBUG.getValue() > thresholdLevel) return;
        CallerData caller = CallerResolver.getCallingMethodData();
        appender.debug(caller, s);
    }

    public static void info(String s) {
        if (LightLogLevel.INFO.getValue() > thresholdLevel) return;
        CallerData caller = CallerResolver.getCallingMethodData();
        appender.info(caller, s);
    }

    public static void warn(String s) {
        if (LightLogLevel.WARN.getValue() > thresholdLevel) return;
        CallerData caller = CallerResolver.getCallingMethodData();
        appender.warn(caller, s);
    }

    public static void error(String s) {
        if (LightLogLevel.ERROR.getValue() > thresholdLevel) return;
        CallerData caller = CallerResolver.getCallingMethodData();
        appender.error(caller, s);
    }

    public static void fatal(String s) {
        if (LightLogLevel.FATAL.getValue() > thresholdLevel) return;
        CallerData caller = CallerResolver.getCallingMethodData();
        appender.fatal(caller, s);
    }
}
