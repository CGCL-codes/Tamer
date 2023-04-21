package net.sf.traser.common;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author Marcell Szathmari
 */
public class TraSer {

    /**
     * This field is used to log messages.
     */
    private static final Logger log = Logger.getLogger("net.sf.traser", "net.sf.traser.common.localization.Log");

    /**
     *
     */
    private static File configFile;

    static {
        log.setLevel(Level.ALL);
    }

    /** Creates a new instance of TraSer */
    private TraSer() {
    }

    /**
     * Sets the level of logging to <code>level</code>.
     * @param level the new level of logging.
     */
    public static void setLogLevel(String level) {
        log.setLevel(Level.parse(level));
    }

    /**
     * Closes all log handlers.
     */
    public static void closeAllHandlers() {
        Handler[] hs = log.getHandlers();
        for (int i = 0; i < hs.length; ++i) {
            closeHandler(hs[i]);
        }
    }

    /**
     * Closes the log handler <code>h</code>.
     * @param h the handler to close.
     */
    private static void closeHandler(Handler h) {
        h.flush();
        h.close();
        log.removeHandler(h);
    }

    /**
     * This method tries to change the file the logs are written into to another
     * one.
     *
     * @param fileName name of file to write logs into
     * @throws RuntimeException
     */
    public static void setLogFile(String fileName) {
        try {
            closeAllHandlers();
            if (fileName == null) return;
            FileHandler h = new FileHandler(fileName, true);
            h.setFormatter(new SimpleFormatter());
            log.addHandler(h);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
