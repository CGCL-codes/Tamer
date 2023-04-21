package main.logger;

import java.net.URL;
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.log4j.Level;

public class LoggerSetup {

    /**
   * @param The class logger
   */
    private static final Logger log = Logger.getLogger(LoggerSetup.class);

    /**
   * Configure logger.
   */
    static {
        try {
            URL log4j_resource = LoggerSetup.class.getResource("log4j.pwt.xml");
            if (log4j_resource == null) {
                throw new Exception("Could not load log4j configuration");
            }
            DOMConfigurator.configure(log4j_resource);
        } catch (Exception e) {
            BasicConfigurator.configure();
            log.setLevel(Level.DEBUG);
            log.warn("Could NOT load \"log4j.pwt.xml\" - Make sure it is in the CLASSPATH", e);
        }
    }

    public static void enable() {
    }
}
