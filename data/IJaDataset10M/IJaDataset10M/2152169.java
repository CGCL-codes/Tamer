package pl.mn.communicator;

/**
 * Created on 2004-12-11
 * 
 * @author <a href="mailto:mati@sz.home.pl">Mateusz Szczap</a>
 * @version $Id: MessageStatus.java,v 1.1 2005/11/05 23:34:52 winnetou25 Exp $
 */
public class MessageStatus {

    private String m_messageStatus = null;

    private MessageStatus(String messageStatus) {
        m_messageStatus = messageStatus;
    }

    /** Message has not been blocked. */
    public static final MessageStatus BLOCKED = new MessageStatus("message_status_blocked");

    /** Message has been successfuly delivered. */
    public static final MessageStatus DELIVERED = new MessageStatus("message_status_delivered");

    /** Message has been queued for later delivery. */
    public static final MessageStatus QUEUED = new MessageStatus("message_status_queued");

    /** Message has not been delivered because remote queue is full (max. 20 messages). */
    public static final MessageStatus BLOCKED_MBOX_FULL = new MessageStatus("message_status_mbox_full");

    /** Message has not been delivered. This status is only in case of GG_CLASS_CTCP */
    public static final MessageStatus NOT_DELIVERED = new MessageStatus("message_status_not_delivered");

    public static final MessageStatus UNKNOWN = new MessageStatus("message_status_unknown");

    /**
	 * @see java.lang.Object#toString()
	 */
    public String toString() {
        return m_messageStatus;
    }

    /**
	 * @see java.lang.Object#hashCode()
	 */
    public int hashCode() {
        return m_messageStatus.hashCode();
    }
}
