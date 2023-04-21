package wtanaka.praya;

import java.io.Serializable;

/**
 * Base class for all entities which can receive messages, and are
 * "fully resolved."  The "fully resolved" concept is demonstrated in
 * these following examples:
 *
 * <ul>
 * <li>Outlook (a popular email program written by the Microsoft
 * Corporation) has a feature where you can type a string into the
 * "To" field of an outgoing email message, and Outlook will
 * asynchronously look in your address book for entries which might
 * match your string.  If a unique address book entry is found, your
 * string is converted into a complete name and email address.  In
 * this case, the string would be represented in Praya as a Recipient,
 * and the name and email address would be represented in Praya as a
 * ResolvedRecipient
 * <li>Gale has the concept of a symlink key.  In this case, the
 * original location would be represented as a Recipient and the
 * target location would be represented as a ResolvedRecipient
 * </ul>
 *
 * <p>
 * Return to <A href="http://sourceforge.net/projects/praya/">
 * <IMG src="http://sourceforge.net/sflogo.php?group_id=2302&type=1"
 *   alt="Sourceforge" width="88" height="31" border="0"></A>
 * or the <a href="http://praya.sourceforge.net/">Praya Homepage</a>
 *
 * @author $Author: wtanaka $
 * @version $Name:  $ $Date: 2003/12/17 01:28:44 $
 **/
public abstract class ResolvedRecipient extends Recipient implements Serializable {

    /**
    * Constructor.
    **/
    public ResolvedRecipient(Protocol parent) {
        super(parent);
        m_protocol = parent;
    }

    /**
    * This object is already resolved, so we just return this object.
    * @return this, which is a fully resolved Recipient.
    **/
    public ResolvedRecipient resolve() {
        return this;
    }

    /**
    * Sends reply to this recipient (as described with getDescription()).
    * @exception NotSentException if the exception is not sent for
    * some reason.
    **/
    public abstract void sendReply(Object reply) throws NotSentException;
}
