package org.jtestcase.plugin.utils;

import java.io.FileWriter;
import java.io.IOException;

public class Logger {

    public static boolean _debug = false;

    public static int _current_debug_level = Logger.DEBUG_LEVEL;

    public static String logFile = "C:/jtc_log.txt";

    public static int DEBUG_LEVEL = 0;

    public static int WARN_LEVEL = 1;

    public static int ERROR_LEVEL = 2;

    public static void debug(String toDebug) {
        if (Logger._debug && Logger._current_debug_level == 0) produceOutput(toDebug + "\n");
    }

    public static void warn(String toWarn) {
        if (Logger._debug && Logger._current_debug_level <= 1) produceOutput("*****  " + toWarn + "\n");
    }

    public static void error(String toError) {
        if (Logger._debug) produceOutput("!!!!!  " + toError + "\n");
    }

    private static void produceOutput(String output) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(Logger.logFile, true);
            writer.append(output);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
            }
        }
    }
}
