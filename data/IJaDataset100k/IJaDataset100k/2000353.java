package net.jini.lookup.entry;

import net.jini.entry.AbstractEntry;

/**
 * The base class from which other status-related entry classes may be derived.
 * 
 * @author Sun Microsystems, Inc.
 * 
 * @see StatusBean
 * @see StatusType
 */
public abstract class Status extends AbstractEntry implements ServiceControlled {

    private static final long serialVersionUID = -5193075846115040838L;

    /**
	 * Construct an empty Status object.
	 */
    protected Status() {
    }

    /**
	 * Construct a Status object with the given severity.
	 * 
	 * @param severity
	 *            the severity level of this status object
	 */
    protected Status(StatusType severity) {
        this.severity = severity;
    }

    /**
	 * The severity level of this status object.
	 * 
	 * @serial
	 */
    public StatusType severity;
}
