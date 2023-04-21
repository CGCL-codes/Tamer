package com.paracel.tt.util;

import java.lang.*;
import java.util.*;
import java.io.*;

/** This class redirects the System.err to a file. */
public class ErrorLog {

    private static boolean isOn;

    /** Constructor */
    protected ErrorLog() {
    }

    /**
     * Creates a <code>PrintStream</code> object from the specified file
     * name.  If the print stream was successfully created, redirects
     * System.err to this print stream.
     * @param	logFileName	The name of the specified log file.
     * @since	1.0
     */
    public static void turnOn(String logFileName) {
        try {
            OutputStream outStream = new FileOutputStream(logFileName, true);
            PrintStream printStream = new PrintStream(outStream);
            System.setErr(printStream);
            isOn = true;
        } catch (Exception e) {
            isOn = false;
            return;
        }
    }

    /** Prints the date string to System.err */
    public static void printDate() {
        if (isOn) {
            System.err.println((new Date()).toString());
        }
    }
}
