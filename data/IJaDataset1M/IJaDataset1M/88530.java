package org.swiftgantt.core.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logger adapter for Standard(Swing or something) to handle log for SwiftGantt core.
 * @author Yuxing Wang
 * 
 */
public class SLF4jLoggerAdapter extends LogAdapter {

    Logger log;

    public SLF4jLoggerAdapter(Class targetClass) {
        super(targetClass);
        log = LoggerFactory.getLogger(targetClass);
    }

    public SLF4jLoggerAdapter(Logger logger) {
        super();
        log = logger;
    }

    @Override
    public void debug(String message) {
        log.debug(message);
    }

    public void debug(String message, Object... args) {
        log.debug(message, args);
    }

    @Override
    public void info(String message) {
        log.info(message);
    }

    public void info(String message, Object... args) {
        log.debug(message, args);
    }

    @Override
    public void title(String message) {
        log.info(message);
    }

    @Override
    public void warn(String message) {
        log.warn(message);
    }

    @Override
    public void error(String message) {
        log.error(message);
    }
}
