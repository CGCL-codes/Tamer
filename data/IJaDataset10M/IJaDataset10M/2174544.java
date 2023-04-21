package org.waffel.jscf.gpg;

import org.waffel.jscf.JSCFException;

/**
 * @author waffel
 * 
 */
public class ProgramNotFoundException extends JSCFException {

    /**
	 * Creates a new exception with the given reason.
	 * 
	 * @param reason
	 *            The reason for the exception.
	 */
    public ProgramNotFoundException(final String reason) {
        super(reason);
    }
}
