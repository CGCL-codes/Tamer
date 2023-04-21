package com.sun.jini.resource;

/**
 * Error thrown when something goes wrong while looking up service providers. In
 * particular, this error will be thrown in the following situations:
 * 
 * <ul>
 * <li>A concrete provider class cannot be found,
 * <li>A concrete provider class cannot be instantiated,
 * <li>The format of a provider-configuration file is illegal, or
 * <li>An IOException occurs while reading a provider-configuration file.
 * </ul>
 * 
 * @author Sun Microsystems, Inc.
 * 
 * @since 2.0
 */
public class ServiceConfigurationError extends Error {

    private static final long serialVersionUID = 5383327130135226968L;

    /**
	 * Constructs a new instance with the specified detail string.
	 */
    public ServiceConfigurationError(String msg) {
        super(msg);
    }

    /**
	 * Constructs a new instance that wraps the specified throwable.
	 */
    public ServiceConfigurationError(Throwable x) {
        super(x);
    }
}
