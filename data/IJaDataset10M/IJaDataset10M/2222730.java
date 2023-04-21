package org.shapeoforion.utils.timer;

import org.apache.commons.logging.Log;
import org.shapeoforion.log.MyLogger;

/**
 * Imlementation of the Sleeper interface using JDK5 features.
 *
 * @author jps
 * @version 1.0
 */
public class ThreadSleeperImpl extends AbstractSleeper {

    private static final Log LOG = MyLogger.getInstance(ThreadSleeperImpl.class);

    /**
     * It's package private, so it's only available through the SleeperFactory.
     */
    ThreadSleeperImpl() {
        super();
    }

    protected void doSleep(final long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            LOG.warn("sleep interrupted, wtf?");
        }
    }
}
