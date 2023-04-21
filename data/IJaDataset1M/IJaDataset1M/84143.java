package net.sourceforge.nconfigurations;

/**
 * An umbrella exception thrown by {@code Configuration} implementations when
 * retrieving values or children fails.
 *
 * @author Petr Novotník
 * @since 1.0
 */
public abstract class ConfigurationAccessException extends RuntimeException {

    protected ConfigurationAccessException() {
        super();
    }

    protected ConfigurationAccessException(final String message) {
        super(message);
    }

    protected ConfigurationAccessException(final String message, final Throwable cause) {
        super(message, cause);
    }

    protected ConfigurationAccessException(final Throwable cause) {
        super(cause);
    }

    private static final long serialVersionUID = -1L;
}
