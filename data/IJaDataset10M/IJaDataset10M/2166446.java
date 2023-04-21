package org.insight.model.config;

/**
 * Indicates an error with the configuration.
 *
 * <pre>
 * Version History:
 *
 * $Log: ConfigurationException.java,v $
 * Revision 1.2  2006/02/03 10:15:11  cjn
 * Fixed checkstyle violations.
 *
 * Revision 1.1  2003/08/04 08:45:24  cjn
 * Initial checkin
 *
 * </pre>
 *
 * @author Chris Nappin
 * @version $Revision: 1.2 $
 */
public class ConfigurationException extends Exception {

    /**
     * Constructor.
     * @param message The error message
     */
    public ConfigurationException(String message) {
        super(message);
    }
}
