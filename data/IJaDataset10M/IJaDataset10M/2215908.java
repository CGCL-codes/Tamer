package net.ardvaark.jackbot;

/**
 * Indicates the network connection to the server has been lost.
 * 
 * @author Brian Vargas
 * @version $Revision: 55 $ $Date: 2008-04-13 14:36:35 -0400 (Sun, 13 Apr 2008) $
 */
public class IRCConnectionLostException extends IRCException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new IRCConnectionLostException.
     * 
     * @param msg The message.
     */
    public IRCConnectionLostException(String msg) {
        this(msg, null);
    }

    /**
     * Constructs a new IRCConnectionLostException.
     * 
     * @param msg The message.
     * @param orig The original cause exception of this exception.
     */
    public IRCConnectionLostException(String msg, Exception orig) {
        super(msg, orig);
    }
}
