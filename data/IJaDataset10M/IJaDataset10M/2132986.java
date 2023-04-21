package org.jpxx.mail.core.pop3.message;

/**
 * The base class for all exceptions thrown by the Message classes
 *
 * @author Jun Li
 * @version $Revision: 0.0.1 $, $Date: 2008/04/29 10:03:00 $
 */
public class MessageException extends Exception {

    /**
     * The next exception in the chain.
     *
     * @serial
     */
    private Exception next;

    private static final long serialVersionUID = -7569192289819959253L;

    /**
     * Constructs a MessageException with no detail message.
     */
    public MessageException() {
        super();
        initCause(null);
    }

    /**
     * Constructs a MessageException with the specified detail message.
     *
     * @param s		the detail message
     */
    public MessageException(String s) {
        super(s);
        initCause(null);
    }

    /**
     * Constructs a MessageException with the specified 
     * Exception and detail message. The specified exception is chained
     * to this exception.
     *
     * @param s		the detail message
     * @param e		the embedded exception
     * @see	#getNextException
     * @see	#setNextException
     * @see	#getCause
     */
    public MessageException(String s, Exception e) {
        super(s);
        next = e;
        initCause(null);
    }

    /**
     * Get the next exception chained to this one. If the
     * next exception is a MessageException, the chain
     * may extend further.
     *
     * @return	next Exception, null if none.
     */
    public synchronized Exception getNextException() {
        return next;
    }

    /**
     * Overrides the <code>getCause</code> method of <code>Throwable</code>
     * to return the next exception in the chain of nested exceptions.
     *
     * @return	next Exception, null if none.
     */
    @Override
    public synchronized Throwable getCause() {
        return next;
    }

    /**
     * Add an exception to the end of the chain. If the end
     * is <strong>not</strong> a MessageException, this 
     * exception cannot be added to the end.
     *
     * @param	ex	the new end of the Exception chain
     * @return		<code>true</code> if this Exception
     *			was added, <code>false</code> otherwise.
     */
    public synchronized boolean setNextException(Exception ex) {
        Exception theEnd = this;
        while (theEnd instanceof MessageException && ((MessageException) theEnd).next != null) {
            theEnd = ((MessageException) theEnd).next;
        }
        if (theEnd instanceof MessageException) {
            ((MessageException) theEnd).next = ex;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Override toString method to provide information on
     * nested exceptions.
     */
    @Override
    public synchronized String toString() {
        String s = super.toString();
        Exception n = next;
        if (n == null) {
            return s;
        }
        StringBuffer sb = new StringBuffer(s == null ? "" : s);
        while (n != null) {
            sb.append(";\n  nested exception is:\n\t");
            if (n instanceof MessageException) {
                MessageException mex = (MessageException) n;
                sb.append(mex.superToString());
                n = mex.next;
            } else {
                sb.append(n.toString());
                n = null;
            }
        }
        return sb.toString();
    }

    /**
     * Return the "toString" information for this exception,
     * without any information on nested exceptions.
     */
    private final String superToString() {
        return super.toString();
    }
}
