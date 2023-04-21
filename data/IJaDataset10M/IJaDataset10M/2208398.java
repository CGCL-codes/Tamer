package com.bluebrim.gemstone.shared;

/**
 * An unchecked exception that can be thrown by <code>CoCommand.doExecute()</code> 
 * when, for some reason, the action in this method is unable to correctly execute.
 * <br>
 * When this exception is thrown the transaction should abort w/o displaying any
 * messages to the user.
 * Creation date: (2000-09-01 12:44:01)
 * @author: Lasse Svad�ngs
 */
public class CoSilentTransactionCommandException extends RuntimeException {

    /**
 * com.bluebrim.gemstone.shared.CoTransactionException constructor comment.
 */
    public CoSilentTransactionCommandException() {
        super();
    }

    /**
 * com.bluebrim.gemstone.shared.CoTransactionException constructor comment.
 * @param s java.lang.String
 */
    public CoSilentTransactionCommandException(String s) {
        super(s);
    }
}
