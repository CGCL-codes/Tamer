package common;

import java.text.*;
import java.util.*;
import java.util.logging.*;
import java.io.File;

/**
 * LogHandler � la classe che si occupa della gestione dei log
 * <p>
 *
 * @author      Andrea Annibali
 * @version     1.0
 */
public class LogHandler {

    private static boolean sysOutEnabled = false;

    private static Logger logLogger;

    private static Logger errLogger;

    public static final int INFO = 1;

    public static final int FINER = 2;

    public static final int FINEST = 3;

    Handler logFh, errFh;

    Calendar calendar = new GregorianCalendar();

    String nazione;

    /**
   * Creazione del file di log ed impostazione del livello di log
   */
    public LogHandler(Level level) {
        String[] str = (System.getProperty("user.dir") + "/log/").split("\\\\");
        String path = "";
        for (int i = 0; i < str.length; i++) {
            path = path + str[i];
            File f = new File(path);
            boolean success = f.exists();
            if (!success) {
                success = f.mkdir();
                if (!success) {
                    System.out.println("Creazione directory fallita");
                }
            }
            path = path + "\\";
        }
        Date dataAttuale = calendar.getTime();
        SimpleDateFormat nuova = new SimpleDateFormat("yyyyMMMMMdd");
        nuova.format(dataAttuale);
        String logFilename = "Log" + nuova.format(dataAttuale) + ".log", errFilename = "Err" + nuova.format(dataAttuale) + ".log";
        logLogger = Logger.getLogger("ArianneViewer_log");
        errLogger = Logger.getLogger("ArianneViewer_err");
        try {
            logFh = new FileHandler(path + logFilename, true);
            logFh.setFormatter(new SimpleFormatter());
            logLogger.addHandler(logFh);
            logLogger.setUseParentHandlers(false);
            errFh = new FileHandler(path + errFilename, true);
            errFh.setFormatter(new SimpleFormatter());
            errLogger.addHandler(errFh);
            errLogger.setUseParentHandlers(false);
            setLevel(level);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLevel(Level level) {
        logLogger.setLevel(level);
        errLogger.setLevel(level);
    }

    public static void closeLogger() {
        for (int i = 0; i < logLogger.getHandlers().length; i++) {
            logLogger.getHandlers()[i].close();
        }
    }

    /**
   * Esegue il logging
   * @param logMsg messaggio da loggare
   * @param level livello di logging
   */
    public static void log(String logMsg, Level level, String piType, boolean logEnabled) {
        if (logEnabled) {
            Logger theLogger = (piType.equals("ERR_MSG") ? errLogger : logLogger);
            if (level.intValue() == Level.INFO.intValue()) {
                if (sysOutEnabled) System.out.println(logMsg);
                theLogger.info(logMsg);
            } else if (level.intValue() == Level.ALL.intValue()) {
                if (sysOutEnabled) System.out.println(logMsg);
                theLogger.info(logMsg);
            } else if (level.intValue() == Level.CONFIG.intValue()) {
                if (sysOutEnabled) System.out.println(logMsg);
                theLogger.config(logMsg);
            } else if (level.intValue() == Level.FINE.intValue()) {
                if (sysOutEnabled) System.out.println(logMsg);
                theLogger.fine(logMsg);
            } else if (level.intValue() == Level.FINER.intValue()) {
                if (sysOutEnabled) System.out.println(logMsg);
                theLogger.finer(logMsg);
            }
            if (level.intValue() == Level.FINEST.intValue()) {
                if (sysOutEnabled) System.out.println(logMsg);
                theLogger.finest(logMsg);
            } else if (level.intValue() == Level.SEVERE.intValue()) {
                if (sysOutEnabled) System.out.println(logMsg);
                theLogger.severe(logMsg);
            } else if (level.intValue() == Level.WARNING.intValue()) {
                if (sysOutEnabled) System.out.println(logMsg);
                theLogger.warning(logMsg);
            }
        }
    }
}
