package org.pustefixframework.webservices;

import java.io.File;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.junit.Before;
import de.schlund.pfixxml.config.GlobalConfig;
import de.schlund.pfixxml.config.GlobalConfigurator;

/**
 * 
 * @author mleidig
 *
 */
public abstract class BaseTestCase {

    @Before
    public void setUp() {
        ConsoleAppender appender = new ConsoleAppender(new PatternLayout("%p: %m\n"));
        Logger logger = Logger.getRootLogger();
        logger.setLevel((Level) Level.WARN);
        logger.removeAllAppenders();
        logger.addAppender(appender);
        if (GlobalConfig.getDocroot() == null) GlobalConfigurator.setDocroot(new File("src/test/resources").getAbsoluteFile().getAbsolutePath());
    }
}
