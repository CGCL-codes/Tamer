package org.jmlspecs.ajmlrac.runtime;

/**
 * A JML error class to notify invariant violations.
 *
 * @author Henrique Rebelo
 * @version $Revision: 1.0 $
 */
public class JMLPrivateInvariantError extends JMLInvariantError {

    /**
	 * Creates a new <code>JMLInvariantError</code> instance from the given assertion message error. 
	 */
    public JMLPrivateInvariantError(String message) {
        super(message);
    }
}
