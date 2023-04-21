package org.jd3lib.compatible;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Compatible Logging infrastructure because the Standard logging doesn't work
 * with the GCJ 4.0 I used.
 * 
 * @author Grunewald
 */
public class Logger {

    public static final Logger global = new Logger();

    private Level currentLevel;

    private static final String name = "JD3lib.compatible Logger";

    private boolean writeDebug;

    private HashSet handlers;

    private Logger() {
        currentLevel = Level.ALL;
    }

    /**
   * The compatible implementation has only the global logger regardless of what
   * the GetLogger argument is.
   */
    public static synchronized Logger getLogger(String inName) {
        return global;
    }

    /**
   * @param all
   */
    public void setLevel(Level newLevel) throws SecurityException {
        currentLevel = newLevel;
    }

    /**
   * @return
   */
    public String getName() {
        return name;
    }

    public void addHandler(Handler handler) throws SecurityException {
        if (handlers == null) handlers = new HashSet(2);
        writeDebug = true;
        handlers.add(handler);
    }

    public void removeHandler(Handler handler) throws SecurityException {
        if (handlers != null) {
            writeDebug = false;
            handlers.remove(handler);
        }
    }

    /**
   * @param info
   * @param string
   */
    public void log(Level level, String msg) {
        if (handlers != null) {
            Date temp = new Date();
            String logString = ">" + temp + "<(" + temp.getTime() + ")\n<" + level.getName() + ">[" + msg + "]";
            System.err.println(logString);
            Iterator writeto = handlers.iterator();
            while (writeto.hasNext()) {
                ((Handler) writeto.next()).publish(logString);
            }
        }
    }

    /**
   * @param string
   */
    public void fine(String msg) {
        log(Level.FINE, msg);
    }

    /**
   * @param string
   */
    public void info(String msg) {
        log(Level.FINE, msg);
    }

    /**
   * @param string
   */
    public void finest(String msg) {
        log(Level.FINEST, msg);
    }

    /**
   * @param header
   */
    public void fine(Object header) {
        this.fine(header.toString());
    }
}
