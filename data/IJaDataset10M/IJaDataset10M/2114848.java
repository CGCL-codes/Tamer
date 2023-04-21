package com.jsystem.j2autoit.logger;

/**
 * @author Kobi Gana
 *
 */
public class Log {

    private static final LoggerThread report = new LoggerThread();

    private Log() {
    }

    public static void setLogMode(boolean silent, boolean debug) {
        report.setLogMode((silent ? 1 : 0) + (debug ? 0 : 2));
    }

    public static void initLog() {
        report.start();
    }

    public static String getCurrentLogs() {
        return report.getCurrentLogs();
    }

    public static void messageLog(String message) {
        report.messageLog(message);
    }

    public static void infoLog(String info) {
        report.infoLog(info);
    }

    public static void warningLog(String warning) {
        report.warningLog(warning);
    }

    public static void errorLog(String error) {
        report.errorLog(error);
    }

    public static void throwableLog(String error, Throwable throwable) {
        report.throwableLog(error, throwable);
    }

    public static void message(String message) {
        report.message(message);
    }

    public static void info(String info) {
        report.info(info);
    }

    public static void warning(String warning) {
        report.warning(warning);
    }

    public static void error(String error) {
        report.error(error);
    }

    public static void throwable(String error, Throwable throwable) {
        report.throwable(error, throwable);
    }

    public static void flushLog() {
        report.flushLog();
    }

    public static void closeLog() {
        report.closeLog();
    }
}
