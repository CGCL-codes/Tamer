package org.fao.fenix.services.logging;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class FenixLogger {

    public Logger logger;

    public FenixLogger() {
    }

    public void initLogger() {
        PropertyConfigurator.configure("log4j.properties");
        logger = Logger.getLogger(FenixLogger.class);
        logger.debug("Test DEBUG Level");
        logger.info("Test INFO Level");
        logger.warn("Test WARNING Level");
        logger.error("Test ERROR Level");
        logger.fatal("Test FATAL Level");
    }
}
