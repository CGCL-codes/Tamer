package net.sf.lfscontrol.core.configuration.exceptions;

/**
 * This exception is thrown when error during configuration store/load process
 * occurs.
 *
 * @author Jiří Sotona
 *
 */
public class ConfigurationException extends Exception {

    private static final long serialVersionUID = 1049393656233768081L;

    public ConfigurationException(final String message, final Throwable cuase) {
        super(message, cuase);
    }
}
