package org.visu;

import java.util.logging.*;

public final class VPG {

    private static boolean logInit = false;

    private static boolean useComplexLogger = false;

    private static final String NAME = "org.visu";

    public static void setupLoggers() {
        Logger logger = Logger.getLogger(NAME);
        logger.setLevel(Level.ALL);
        LogManager.getLogManager().addLogger(logger);
        LogManager.getLogManager().getLogger(NAME).info("VPG Logger initialized successfully.");
        LogManager.getLogManager().getLogger(NAME).finest("Debugging...");
    }

    private static Logger getLogger() {
        if (!logInit) {
            setupLoggers();
            logInit = true;
        }
        return LogManager.getLogManager().getLogger(NAME);
    }

    public static void logInfo(String info) {
        System.out.println(info);
    }

    public static void logError(String err) {
        System.err.println(err);
    }

    public static void logError(String err, Throwable th) {
        System.err.println(err);
        th.printStackTrace(System.err);
    }

    public static void logError(Throwable th) {
        logError(th.getMessage(), th);
    }
}
