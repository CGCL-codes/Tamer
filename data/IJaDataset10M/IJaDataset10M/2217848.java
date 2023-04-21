package org.jalajala.simple;

/**
 * Errors found while configuring a test parameter are converted to some
 * subclass of this exception.
 * {@link ConfigurationInvocationException} is used to convert errors
 * raised before calling configuration methods:
 * signature mismatches, parsing errors, etc.
 * {@link ConfigurationExecutionException} is used to convert errors
 * encountered during the execution of configuration methods code.
 */
public class ConfigurationException extends Exception {

    public ConfigurationException() {
        super();
    }

    public ConfigurationException(final String message) {
        super(message);
    }

    public ConfigurationException(final Throwable cause) {
        super(cause);
    }

    public ConfigurationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
