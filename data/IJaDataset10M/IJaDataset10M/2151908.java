package com.aytul.janissary.util;

import java.net.URL;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * @author Yalim Aytul
 * <p>
 * LogManager is used to configure log4j.
 * </p>
 */
public class LogManager {

    /**
	 * Configures log4j.
	 * @throws Exception
	 */
    public void configure() throws Exception {
        URL log4jresource = ResourceLoader.loadFileAsURL(ConfigCache.getProperty("log4j.xml"));
        DOMConfigurator.configure(log4jresource);
        log.info("Log4j configured successfully from url " + log4jresource.toString());
    }

    /**
	 * Logger
	 */
    private static final Logger log = Logger.getLogger(LogManager.class);
}
