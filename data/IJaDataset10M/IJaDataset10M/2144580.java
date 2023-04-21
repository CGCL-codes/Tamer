package net.sourceforge.hobbes.common.protocol;

/**
 *  This <code>Exception</code> should be thrown if a document makes a
 *  reply of a <code>DocumentSender</code> which should not have been
 *  asked. For example, a document replying a <code>HobbesClient</code> to
 *  <code>list-clients</code> or telling the <code>ClientConnection</code>
 *  to <code>read-rules</code>.
 *
 * @author     Daniel M. Hackney
 * @created    May 10, 2005
 */
public class IllegalReplyException extends HobbesProtocolException {

    /**
     * 
     */
    private static final long serialVersionUID = -2801158909027400403L;

    /**
     *  Creates a new <code>IllegalReplyException</code> and with the
     *  specified detail message.
     *
     * @param  message  Description of the <code>Exception</code>, should
     *      include the name of the object.
     */
    public IllegalReplyException(String message) {
        super(message);
    }
}
