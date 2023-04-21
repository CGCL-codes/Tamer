package com.intel.gpe.client2.common.utils;

import java.io.File;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import com.intel.gpe.client2.defaults.IPreferences;
import com.intel.gpe.client2.defaults.preferences.CommonKeys;

/**
 * @version $Id: LoggerTools.java,v 1.3 2007/02/24 14:28:36 nbmalysh Exp $
 * @author Nikolay Malyshev
 */
public class LoggerTools {

    private static Logger logger = Logger.getLogger("com.intel");

    public static Handler loggerInit(String clientName, IPreferences preferences) {
        logger.setLevel(Level.parse(preferences.get(CommonKeys.LOG_LEVEL)));
        Handler[] hs = logger.getHandlers();
        for (int i = 0; i < hs.length; i++) {
            if (hs[i] instanceof FileHandler) {
                FileHandler fh = (FileHandler) hs[i];
                System.out.println("!!!!!!!!!!!!!! Another's FileHandler in a logger.");
                logger.removeHandler(fh);
                fh.close();
            }
        }
        FileHandler fh = null;
        String logDir = preferences.get(CommonKeys.LOGDIR);
        if (new File(logDir).isDirectory()) {
            SimpleDateFormat df = new SimpleDateFormat("-yyyyMMdd-HH-mm-ss.'log'", new DateFormatSymbols(Locale.US));
            String fileName = logDir + "/" + clientName + df.format(new Date());
            try {
                fh = new FileHandler(fileName);
                SimpleFormatter formatter = new SimpleFormatter();
                fh.setFormatter(formatter);
                logger.addHandler(fh);
            } catch (Exception e) {
                if (fh != null) {
                    fh.close();
                    fh = null;
                }
                e.printStackTrace();
            }
        }
        boolean console = preferences.get(CommonKeys.LOG_TO_CONSOLE_ENABLED).equals("true");
        logger.setUseParentHandlers(console);
        return fh;
    }

    public static void removeHandler(Handler h) {
        h.close();
        logger.removeHandler(h);
    }

    public static void setConsole(boolean on) {
        logger.setUseParentHandlers(on);
    }

    public static void setLevel(Level level) {
        logger.setLevel(level);
    }
}
