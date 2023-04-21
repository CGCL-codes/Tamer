package org.jmlspecs.ajmlrac.runtime;

/**
 * A JML error class to notify internal normal postcondition violations.
 *
 * @author Henrique Rebelo
 * @version $Revision: 1.0$
 */
public class JMLInternalProtectedExceptionalPostconditionError extends JMLInternalExceptionalPostconditionError {

    /**
	 * Creates a new instance from the given assertion message error. 
	 */
    public JMLInternalProtectedExceptionalPostconditionError(String message) {
        super(message);
    }
}
