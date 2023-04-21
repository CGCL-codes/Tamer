package net.sf.djdoc.util;

import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * The <code>Logging</code> class is a utility class wrapping the access to the
 * Apache Log4J logging library.
 */
public class Logging {

    private Logging() {
    }

    /**
   * This method initializes the Log4J logging engine with specified properties.
   *
   * @param props The set of properties to initialize Log4J with
   */
    public static void setup(Properties props) {
        PropertyConfigurator.configure(props);
    }

    /**
   * This method returns a Log4J <code>Logger</code> with a name equal to the
   * class from which this method is invoked.
   *
   * @return A Log4J <code>Logger</code> with the name of the invoking class
   */
    public static Logger getLogger() {
        String className = Thread.currentThread().getStackTrace()[1].getClassName();
        return Logger.getLogger(className);
    }
}
