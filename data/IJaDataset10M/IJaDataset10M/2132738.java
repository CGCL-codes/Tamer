package org.benetech.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An event listener which logs events.
 * @author Reuben Firmin
 */
public final class LoggingEventListener implements EventListener {

    private Log log;

    /**
     * Constructor.
     * @param logPrefix Use the given logging prefix.
     */
    public LoggingEventListener(final String logPrefix) {
        log = LogFactory.getLog(logPrefix);
    }

    /**
     * {@inheritDoc}
     */
    public void message(final String message) {
        log.info(message);
    }

    /**
     * {@inheritDoc}
     */
    public void error(final String message) {
        log.error(message);
    }
}
