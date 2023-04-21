package net.sf.dozer.util.mapping.util;

import org.apache.commons.logging.Log;

/**
 * Internal class that is just a thin wrapper for logging one time dozer initialization messages. These messages will be
 * written to system.out as well as log.info. To enable dual writes to System.out: -Ddozer.debug=true Only intended for
 * internal use.
 * 
 * @author tierney.matt
 */
public class InitLogger {

    private static boolean debugEnabled = false;

    static {
        String sysProp = System.getProperty(MapperConstants.DEBUG_SYS_PROP);
        if (sysProp != null && !sysProp.trim().equals("false")) {
            debugEnabled = true;
        }
    }

    private InitLogger() {
    }

    public static void log(Log log, String msg) {
        if (debugEnabled) {
            System.out.println("dozer:  " + msg);
        }
        log.info(msg);
    }
}
