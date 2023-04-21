package com.ice.util;

import java.io.*;
import java.lang.*;
import java.util.*;

public class FileLog extends Object {

    private static final String RCS_ID = "$Id: FileLog.java,v 1.2 2004/03/05 12:20:31 deniger Exp $";

    private static final String RCS_REV = "$Revision: 1.2 $";

    private static final String RCS_NAME = "$Name:  $";

    private static final String DEFAULT_FILENAME = "log.txt";

    private static FileLog defaultLogger = null;

    private String filename;

    private FileWriter file;

    private PrintWriter stream;

    private boolean open;

    private boolean echo;

    private boolean autoFlush;

    public static FileLog getDefaultLogger() {
        return FileLog.defaultLogger;
    }

    public static FileLog setDefaultLogger(FileLog logger) {
        FileLog old = FileLog.defaultLogger;
        FileLog.defaultLogger = logger;
        return old;
    }

    public static void checkDefaultLogger() {
        if (FileLog.defaultLogger == null) {
            FileLog.defaultLogger = new FileLog(FileLog.DEFAULT_FILENAME);
        }
    }

    public FileLog(String filename) {
        this.open = false;
        this.echo = false;
        this.autoFlush = true;
        this.filename = filename;
        this.file = null;
        this.stream = null;
    }

    public void setLogFilename(String filename) {
        this.filename = filename;
    }

    public void setAutoFlush(boolean autoflush) {
        this.autoFlush = autoflush;
    }

    public void checkLogOpen() {
        if (!this.open) {
            this.openLogFile();
        }
    }

    public void openLogFile() {
        boolean isok = true;
        try {
            this.file = new FileWriter(this.filename);
        } catch (Exception ex) {
            System.err.println("error opening log file '" + this.filename + "' - " + ex.getMessage());
            this.file = null;
            isok = false;
        }
        if (isok) {
            this.stream = new PrintWriter(this.file);
            this.open = true;
        }
        this.echo = false;
    }

    public void closeLog() {
        if (this.open) {
            this.open = false;
            if (this.stream != null) {
                this.stream.flush();
                this.stream.close();
                this.stream = null;
            }
        }
    }

    public void setEcho(boolean setting) {
        this.echo = setting;
    }

    public void traceMsg(Throwable thrown, String msg) {
        this.logMsg(msg);
        this.logMsg(thrown.getMessage());
        if (!this.open) thrown.printStackTrace(System.err); else thrown.printStackTrace(this.stream);
        if (this.autoFlush && this.open) this.stream.flush();
    }

    public static void defLogMsg(String msg) {
        FileLog.checkDefaultLogger();
        if (FileLog.defaultLogger != null) FileLog.defaultLogger.logMsg(msg);
    }

    public void logMsg(String msg) {
        this.checkLogOpen();
        if (this.open) {
            this.stream.println(msg);
            if (this.autoFlush && this.open) this.stream.flush();
        }
        if (this.echo) {
            System.out.println(msg);
        }
    }

    public static void defLogMsgStdout(String msg) {
        FileLog.checkDefaultLogger();
        if (FileLog.defaultLogger != null) FileLog.defaultLogger.logMsgStdout(msg);
    }

    public void logMsgStdout(String msg) {
        this.checkLogOpen();
        if (this.open) {
            this.stream.println(msg);
            if (this.autoFlush && this.open) this.stream.flush();
        }
        System.out.println(msg);
    }

    public static void defLogMsgStderr(String msg) {
        FileLog.checkDefaultLogger();
        if (FileLog.defaultLogger != null) FileLog.defaultLogger.logMsgStderr(msg);
    }

    public void logMsgStderr(String msg) {
        this.checkLogOpen();
        if (this.open) {
            this.stream.println(msg);
            if (this.autoFlush && this.open) this.stream.flush();
        }
        System.err.println(msg);
    }
}
