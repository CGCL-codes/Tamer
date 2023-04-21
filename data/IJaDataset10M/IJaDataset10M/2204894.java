package com.android.exchange.utility;

import android.content.Context;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class FileLogger {

    private static FileLogger LOGGER = null;

    private static FileWriter mLogWriter = null;

    public static String LOG_FILE_NAME = "/sdcard/emaillog.txt";

    public static synchronized FileLogger getLogger(Context c) {
        LOGGER = new FileLogger();
        return LOGGER;
    }

    private FileLogger() {
        try {
            mLogWriter = new FileWriter(LOG_FILE_NAME, true);
        } catch (IOException e) {
        }
    }

    public static synchronized void close() {
        if (mLogWriter != null) {
            try {
                mLogWriter.close();
            } catch (IOException e) {
            }
            mLogWriter = null;
        }
    }

    public static synchronized void log(Exception e) {
        if (mLogWriter != null) {
            log("Exception", "Stack trace follows...");
            PrintWriter pw = new PrintWriter(mLogWriter);
            e.printStackTrace(pw);
            pw.flush();
        }
    }

    @SuppressWarnings("deprecation")
    public static synchronized void log(String prefix, String str) {
        if (LOGGER == null) {
            LOGGER = new FileLogger();
            log("Logger", "\r\n\r\n --- New Log ---");
        }
        Date d = new Date();
        int hr = d.getHours();
        int min = d.getMinutes();
        int sec = d.getSeconds();
        StringBuffer sb = new StringBuffer(256);
        sb.append('[');
        sb.append(hr);
        sb.append(':');
        if (min < 10) sb.append('0');
        sb.append(min);
        sb.append(':');
        if (sec < 10) {
            sb.append('0');
        }
        sb.append(sec);
        sb.append("] ");
        if (prefix != null) {
            sb.append(prefix);
            sb.append("| ");
        }
        sb.append(str);
        sb.append("\r\n");
        String s = sb.toString();
        if (mLogWriter != null) {
            try {
                mLogWriter.write(s);
                mLogWriter.flush();
            } catch (IOException e) {
            }
        }
    }
}
