package jackbot;

/**
 * Indicates the network connection to the server has been lost.
 * @author Brian Vargas
 * @version $Revision: 5 $ $Date: 2002-09-23 01:22:20 -0400 (Mon, 23 Sep 2002) $
 */
public class IRCConnectionLostException extends IRCException {

    /**
     * Constructs a new IRCConnectionLostException.
     * @param msg The message.
     */
    public IRCConnectionLostException(String msg) {
        this(msg, null);
    }

    /**
     * Constructs a new IRCConnectionLostException.
     * @param msg The message.
     * @param orig The original cause exception of this exception.
     */
    public IRCConnectionLostException(String msg, Exception orig) {
        super(msg, orig);
    }
}
