package org.ezim.core;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.ezim.core.Ezim;

public class EzimLogger {

    private Logger logger = null;

    private static EzimLogger ezlogger = null;

    /**
	 * construct an instance of the logger
	 */
    private EzimLogger() {
        FileHandler fhTmp = null;
        this.logger = Logger.getLogger(Ezim.appAbbrev);
        try {
            fhTmp = new FileHandler("%h/." + Ezim.appAbbrev + "/" + Ezim.appAbbrev + ".log", true);
            fhTmp.setFormatter(new SimpleFormatter());
            fhTmp.setEncoding("UTF-8");
            this.logger.addHandler(fhTmp);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    /**
	 * return an EzimLogger object
	 */
    public static EzimLogger getInstance() {
        if (EzimLogger.ezlogger == null) EzimLogger.ezlogger = new EzimLogger();
        return EzimLogger.ezlogger;
    }

    /**
	 * log a SEVERE message
	 * @param strMsg the string message
	 */
    public void severe(String strMsg) {
        this.logger.log(Level.SEVERE, strMsg);
    }

    /**
	 * log a SEVERE message
	 * @param strMsg the string message
	 * @param thwInfo Throwable associated with the message
	 */
    public void severe(String strMsg, Throwable thwInfo) {
        this.logger.log(Level.SEVERE, strMsg, thwInfo);
    }

    /**
	 * log a WARNING message
	 * @param strMsg the string message
	 */
    public void warning(String strMsg) {
        this.logger.log(Level.WARNING, strMsg);
    }

    /**
	 * log a WARNING message
	 * @param strMsg the string message
	 * @param thwInfo Throwable associated with the message
	 */
    public void warning(String strMsg, Throwable thwInfo) {
        this.logger.log(Level.WARNING, strMsg, thwInfo);
    }
}
