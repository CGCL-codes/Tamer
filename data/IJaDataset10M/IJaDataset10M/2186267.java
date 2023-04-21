package com.ryanhirsch.log;

import java.io.StringWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class Log {

    public static void trace(String msg) {
        log(msg, TRACE);
    }

    public static void info(String msg) {
        log(msg, INFO);
    }

    public static void warn(String msg) {
        log(msg, WARN);
    }

    public static void fatal(String msg) {
        log(msg, FATAL);
    }

    public static final int TRACE = 0;

    public static final int INFO = 1;

    public static final int WARN = 2;

    public static final int ERROR = 3;

    public static final int FATAL = 4;

    static int logLevel = 0;

    public static void setLogLevel(int level) {
        logLevel = level;
    }

    public static int getLogLevel() {
        return logLevel;
    }

    static boolean showThread = false;

    public static void setShowingThread(boolean show) {
        showThread = show;
    }

    public static boolean isShowingThread() {
        return showThread;
    }

    static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS ");

    public static void error(String msg, Exception ex) {
        StringWriter writer = new StringWriter();
        ex.printStackTrace(new PrintWriter(writer));
        StringTokenizer tokenizer = new StringTokenizer(writer.toString(), "\n");
        while (tokenizer.hasMoreTokens()) {
            log(msg + tokenizer.nextToken(), ERROR);
        }
    }

    public static void log(String msg, int level) {
        if (level >= logLevel) {
            System.out.print(dateFormat.format(new Date()));
            if (showThread) {
                System.out.print(msg);
                System.out.print(" *** ");
                System.out.println(Thread.currentThread().toString());
            } else {
                System.out.println(msg);
            }
        }
    }
}
