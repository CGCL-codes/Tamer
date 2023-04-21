package net.ardvaark.jackbot.scripting;

/**
 * An exception that is thrown by JackBot scripting engines. Check the inner
 * exception for more details.
 * 
 * @author Brian Vargas
 * @since JackBot v1.1
 * @version $Revision: 66 $ $Date: 2008-07-04 14:29:04 -0400 (Fri, 04 Jul 2008) $
 */
public class ScriptException extends net.ardvaark.jackbot.IRCException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new <CODE>ScriptException</CODE>.
     * 
     * @param msg The message.
     */
    public ScriptException(String msg) {
        super(msg);
    }

    /**
     * Constructs a new <CODE>ScriptException</CODE>.
     * 
     * @param innerException The exception which caused this exception.
     */
    public ScriptException(Exception inner) {
        super(inner);
    }

    /**
     * Constructs a new <CODE>ScriptException</CODE>.
     * 
     * @param msg The message.
     * @param innerException The exception which caused this exception.
     */
    public ScriptException(String msg, Exception innerException) {
        super(msg, innerException);
    }
}
