package org.databene.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements a PageListener that logs its invocations in info level.<br/>
 * <br/>
 * Created: 06.09.2007 19:20:58
 * @since 0.3
 * @author Volker Bergmann
 */
public class LoggingPageListener implements PageListener {

    private static final Logger logger = LoggerFactory.getLogger(LoggingPageListener.class);

    public void pageStarting() {
        logger.info("pageStarting()");
    }

    public void pageFinished() {
        logger.info("pageFinished()");
    }
}
