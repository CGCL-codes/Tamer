package jgnash.convert;

/** Thrown when <code>QifParse encounters</code> transactions without a 
 * destination account.
 * <p>
 * $Id: NoAccountException.java 675 2008-06-17 01:36:01Z ccavanaugh $
 * 
 * @author Craig Cavanaugh 
 */
public class NoAccountException extends Exception {

    /**
     * Constructs a <code>NoAccountException</code> with no detail  message. 
     */
    public NoAccountException() {
        super();
    }

    /**
     * Constructs a <code>NoAccountException</code> with the 
     * specified detail message. 
     *
     * @param   s   the detail message.
     */
    public NoAccountException(String s) {
        super(s);
    }
}
