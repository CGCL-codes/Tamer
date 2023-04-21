package org.file4j.configuration;

/**
 * May be thrown by a Configuration when it is unable to load the configuration.
 *
 * @author $Author: genlbm $
 * @version $Revision: 1.1 $
 */
public class ConfigurationLoadingException extends Exception {

    public ConfigurationLoadingException(String message, Throwable cause) {
        super(message, cause);
    }
}
