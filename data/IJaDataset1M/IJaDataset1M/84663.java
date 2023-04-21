package org.naftulin.configmgr.parsers;

import java.net.URL;
import org.apache.log4j.PropertyConfigurator;
import org.naftulin.configmgr.ConfigurationType;

/**
 * Initilizes log4j engine by using propertes log4j initializer.
 * 
 * @author Henry Naftulin
 * @since 1.0
 */
public class Log4JPropertiesParserImpl extends AbstractLog4JParser {

    private static final long serialVersionUID = 1L;

    /**
	 * Initilizes log4j engine by using properties log4j initializer.
	 */
    void doCallLog4JConfigurator(final URL fileUrl) {
        PropertyConfigurator.configure(fileUrl);
    }

    /**
	 * Returns a string representation of this parser.
	 * @return a string representation of this parser.
	 */
    public String toString() {
        return "Log4J properties parser";
    }

    protected ConfigurationType getConfigurationType() {
        return ConfigurationType.LOG4J_PROPS;
    }
}
