package org.chess.quasimodo.util;

import org.apache.log4j.Logger;

public class ThreadUtils {

    private static final Logger logger = Logger.getLogger(ThreadUtils.class);

    /**
	 * Quietly sleeps the current thread.(ignoring any error)
	 * @param timemiliseconds  the length of time to sleep in milliseconds.
	 */
    public static void waitFor(long timemiliseconds) {
        try {
            Thread.sleep(timemiliseconds);
        } catch (InterruptedException e) {
            logger.error("Cannot interrupt current thread", e);
        }
    }
}
