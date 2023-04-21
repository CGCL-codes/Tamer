package eu.popeye.network;

import eu.popeye.application.PropertiesLoader;
import java.io.File;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

/**
 * All test classes must extend this class and invoke this 
 * class constructor in their constructors.
 * 
 * @author Gerard Paris Aixala
 *
 */
public class Test {

    private static final String log4jPropertiesFile = "./conf/log4j.properties";

    protected static final Log log = LogFactory.getLog(Test.class);

    public Test(String[] args) {
        PropertiesLoader.getPeerRole();
        File f = new File(log4jPropertiesFile);
        if (f.exists()) {
            log.info("Loading log4j properties from " + log4jPropertiesFile);
            PropertyConfigurator.configure(log4jPropertiesFile);
        } else {
            log.info("File " + log4jPropertiesFile + " is missing. Loading basic configuration.");
            BasicConfigurator.configure();
        }
    }
}
