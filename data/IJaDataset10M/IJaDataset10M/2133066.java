package nl.dgl.rgb;

import java.util.logging.Logger;

/**
 * @author David G. Lindeijer. 
 */
public class Log {

    private Log() {
    }

    private static Logger packageLogger = Logger.getLogger(new Log().getClass().getPackage().toString());

    public static Logger getLogger() {
        return packageLogger;
    }
}
