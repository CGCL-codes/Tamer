package org.nightlabs.jfire.organisation;

import org.nightlabs.ModuleException;

/**
 * @author Marco Schulze - marco at nightlabs dot de
 * @author Marc Klinger - marc[at]nightlabs[dot]de
 */
public class OrganisationAlreadyRegisteredException extends ModuleException {

    /**
	 * The serial version of this class.
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * Create a new OrganisationAlreadyRegisteredException.
	 */
    public OrganisationAlreadyRegisteredException() {
        super();
    }

    /**
	 * Create a new OrganisationAlreadyRegisteredException.
	 * @param  message the detail message (which is saved for later retrieval
	 *         by the {@link #getMessage()} method).
	 */
    public OrganisationAlreadyRegisteredException(String message) {
        super(message);
    }

    /**
	 * Create a new OrganisationAlreadyRegisteredException.
	 * @param  message the detail message (which is saved for later retrieval
	 *         by the {@link #getMessage()} method).
	 * @param  cause the cause (which is saved for later retrieval by the
	 *         {@link #getCause()} method).  (A <tt>null</tt> value is
	 *         permitted, and indicates that the cause is nonexistent or
	 *         unknown.)
	 */
    public OrganisationAlreadyRegisteredException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
	 * Create a new OrganisationAlreadyRegisteredException.
	 * @param  cause the cause (which is saved for later retrieval by the
	 *         {@link #getCause()} method).  (A <tt>null</tt> value is
	 *         permitted, and indicates that the cause is nonexistent or
	 *         unknown.)
	 */
    public OrganisationAlreadyRegisteredException(Throwable cause) {
        super(cause);
    }
}
