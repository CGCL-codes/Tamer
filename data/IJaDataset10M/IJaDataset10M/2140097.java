package org.cvj.mirror;

import javax.swing.*;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import javax.swing.plaf.metal.MetalLookAndFeel;

/**
 * This class provides functionality to synchronize files from one location to
 * another. When this class' main method is run, a frame will be opened to
 * provide the GUI to the program. I plan to later on implement more commandline
 * functionality.
 *
 * @author Charl van Jaarsveldt <charlvj@gmail.com>
 */
public class CvJMirror {

    private static CvJMirrorFrame frame;

    private static CvJMirror singleton = null;

    private static PrintStream logFile;

    private static boolean debugMode = false;

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy 'at' HH:mm:ss");

    private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    public static final int TIMESTAMP_LEVEL_NONE = 0;

    public static final int TIMESTAMP_LEVEL_TIME = 1;

    public static final int TIMESTAMP_LEVEL_TIMEDATE = 2;

    /**
     * This class is implemented as a singleton class, and this method returns
     * the singelton object. If the singleton is not yet instantiated, it will
     * create a new instance, and load the necessary properties.
     *
     * @return Returns the singleton instance of this class.
     */
    public static CvJMirror getInstance() {
        if (singleton == null) {
            singleton = new CvJMirror();
        }
        return singleton;
    }

    private CvJMirror() {
    }

    public static void setDebugMode(boolean debug) {
        debugMode = debug;
    }

    public boolean getDebugMode() {
        return debugMode;
    }

    public static void resetLogFile() {
        logFile = null;
    }

    /**
     * 
     * @return The logfile for CvJMirror
     */
    public static PrintStream getLogFile() {
        return null;
    }

    public static void debug(String s) {
        log(s, true);
    }

    /**
     * Writes a log entry with a standard timestamp (Just the time)
     * @param s
     */
    public static void log(String s) {
        log(s, TIMESTAMP_LEVEL_TIME);
    }

    /**
     * Writes a Debug log - will be ignored if in DebugMode.
     * @param s
     * @param debugLog
     */
    public static void log(String s, boolean debugLog) {
        log(s, TIMESTAMP_LEVEL_TIME, debugLog);
    }

    /**
     * Writes a log entry with the given timestanp level
     * @param s
     * @param timestampLevel
     */
    public static void log(String s, int timestampLevel) {
        log(s, timestampLevel, false);
    }

    /**
     * Writes a Log entry with the given timestamp level.
     * @param s
     * @param timestampLevel
     * @param debugLog If this is a debug log
     */
    public static void log(String s, int timestampLevel, boolean debugLog) {
        if (debugMode || !debugLog) {
            if (debugLog) s = "DEBUG:  " + s;
            Date date = new Date();
            switch(timestampLevel) {
                case TIMESTAMP_LEVEL_TIME:
                    s = timeFormat.format(date) + "   " + s;
                    break;
                case TIMESTAMP_LEVEL_TIMEDATE:
                    s = s + "   " + dateFormat.format(date);
                    break;
            }
            PrintStream log = getLogFile();
            if (log != null) {
                log.println(s);
                log.flush();
            }
            if (frame == null) {
                System.out.println(s);
            } else {
                frame.log(s);
            }
        }
    }

    /**
     * shows an error - the Frame should show a message box.
     * @param s
     */
    public static void error(String s) {
        if (frame == null) {
            System.out.println("Error: " + s);
        } else {
            frame.error(s);
        }
    }

    public static void main(String[] args) throws IOException, SecurityException {
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        try {
            UIManager.setLookAndFeel(new MetalLookAndFeel());
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        if (args.length > 0 && args[0].equals("--debug")) {
            setDebugMode(true);
        } else {
            setDebugMode(false);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                frame = new CvJMirrorFrame();
                frame.setVisible(true);
                frame.doAutoStart();
            }
        });
    }
}
