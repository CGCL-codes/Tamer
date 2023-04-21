package org.jmlspecs.ajmlrac.runtime;

/**
 * A JML error class to notify internal normal postcondition violations.
 *
 * @author Henrique Rebelo
 * @version $Revision: 1.0$
 */
public class JMLInternalExceptionalPostconditionError extends JMLPostconditionError {

    /**
	 * Creates a new instance from the given assertion message error. 
	 */
    public JMLInternalExceptionalPostconditionError(String message) {
        super(message);
    }
}
