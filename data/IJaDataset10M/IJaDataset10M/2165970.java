package tests.jd;

import java.util.logging.*;

/**
 *
 * @author  jdepons
 * @version 
 */
public class LogTest {

    /** Creates new LogTest */
    public LogTest() {
    }

    public static void main(String[] args) {
        Logger logger = Logger.getLogger("com.mycompany.BasicLogging");
        logger.severe("my severe message");
        logger.warning("my warning message");
        logger.info("my info message");
        logger.config("my config message");
        logger.fine("my fine message");
        logger.finer("my finer message");
        logger.finest("my finest message");
        try {
            boolean append = true;
            FileHandler handler = new FileHandler("my.log", append);
            Logger logger = Logger.getLogger("com.mycompany");
            logger.addHandler(handler);
        } catch (IOException e) {
        }
    }
}
