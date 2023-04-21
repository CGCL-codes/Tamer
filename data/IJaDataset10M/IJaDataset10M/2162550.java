package clp.core;

/**
 * InvalidStateException is a standard exception used
 * to report the condition where no object was found after
 * a query.  This exception automatically creates a 
 * corresponding business error with a message that can
 * optionally be shown to the user.
 *
 * @author Darren Broemmer
 */
public class NoVerbFoundException extends CLPException {

    public NoVerbFoundException() {
        super("I'm sorry, I'm not sure what it is that you want to do.\n " + "Can you be more descriptive?");
    }
}
