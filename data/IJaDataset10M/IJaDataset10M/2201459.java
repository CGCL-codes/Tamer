package jse;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author WangShuai
 */
public class LogTest {

    public static void main(String[] args) {
        Logger logger = Logger.getLogger(LogTest.class.getName());
        logger.log(Level.INFO, "日志");
    }
}
