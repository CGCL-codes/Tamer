package jvdrums;

import java.net.URL;
import javax.xml.parsers.FactoryConfigurationError;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * @author egolan
 */
public final class JVDrumsLogger {

    private static final Logger logger = Logger.getLogger(JVDrumsLogger.class);

    static {
        try {
            URL url4j = JVDrumsLogger.class.getResource("log4j.xml");
            DOMConfigurator.configure(url4j);
        } catch (FactoryConfigurationError e) {
            e.printStackTrace();
        }
    }

    private JVDrumsLogger() {
    }

    public static Logger getLogger() {
        return logger;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        JVDrumsLogger.getLogger().info("bla !!!");
    }
}
