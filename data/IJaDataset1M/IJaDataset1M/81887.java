package javamail.common;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sriram
 */
public class logAllLevels {

    private static Logger logger;

    private static ConsoleHandler ch;

    static {
        logger = Logger.getLogger("com.sriram");
        ch = new ConsoleHandler();
        ch.setLevel(Level.ALL);
        logger.addHandler(ch);
        logger.setLevel(Level.ALL);
        logger.setUseParentHandlers(false);
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void setLevel(Level level) {
        ch.setLevel(level);
        logger.setLevel(level);
    }
}
