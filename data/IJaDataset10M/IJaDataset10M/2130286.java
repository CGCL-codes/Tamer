package de.carne.util.resource;

import java.util.Locale;
import de.carne.util.Exceptions;

/**
 * Indicates that a specific resource is not available.
 */
public class ResourceUnavailableException extends Exception {

    private static final long serialVersionUID = 1l;

    private String[] key;

    private Locale locale;

    /**
	 * Construct ResourceUnavailableException.
	 * 
	 * @param key The requested key.
	 * @param locale The requested locale.
	 * @param message The exception message.
	 */
    public ResourceUnavailableException(String[] key, Locale locale, String message) {
        this(key, locale, message, null);
    }

    /**
	 * Construct ResourceUnavailableException.
	 * 
	 * @param key The requested key.
	 * @param locale The requested locale.
	 * @param cause The causing exception.
	 */
    public ResourceUnavailableException(String[] key, Locale locale, Throwable cause) {
        this(key, locale, (cause != null ? Exceptions.getMessage(cause) : null), cause);
    }

    /**
	 * Construct ResourceUnavailableException.
	 * 
	 * @param key The requested key.
	 * @param locale The requested locale.
	 * @param message The exception message.
	 * @param cause The causing exception.
	 */
    public ResourceUnavailableException(String[] key, Locale locale, String message, Throwable cause) {
        super(message, cause);
        assert key != null;
        assert locale != null;
        this.key = key;
        this.locale = locale;
    }

    /**
	 * Get the key causing this exception.
	 * 
	 * @return The requested key.
	 */
    public String[] key() {
        return this.key;
    }

    /**
	 * Get the locale causing this exception.
	 * 
	 * @return The requested locale.
	 */
    public Locale locale() {
        return this.locale;
    }
}
