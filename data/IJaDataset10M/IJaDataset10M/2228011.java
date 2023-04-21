package org.apache.poi.hpsf;

/**
 * <p>This exception is thrown if one of the {@link PropertySet}'s
 * convenience methods that require a single {@link Section} is called
 * and the {@link PropertySet} does not contain exactly one {@link
 * Section}.</p>
 *
 * <p>The constructors of this class are analogous to those of its
 * superclass and documented there.</p>
 *
 * @author Rainer Klute <a
 * href="mailto:klute@rainer-klute.de">&lt;klute@rainer-klute.de&gt;</a>
 */
public class NoSingleSectionException extends HPSFRuntimeException {

    /**
     * <p>Constructor</p>
     */
    public NoSingleSectionException() {
        super();
    }

    /**
     * <p>Constructor</p>
     * 
     * @param msg The exception's message string
     */
    public NoSingleSectionException(final String msg) {
        super(msg);
    }

    /**
     * <p>Constructor</p>
     * 
     * @param reason This exception's underlying reason
     */
    public NoSingleSectionException(final Throwable reason) {
        super(reason);
    }

    /**
     * <p>Constructor</p>
     * 
     * @param msg The exception's message string
     * @param reason This exception's underlying reason
     */
    public NoSingleSectionException(final String msg, final Throwable reason) {
        super(msg, reason);
    }
}
