package org.opennms.sms.reflector.smsservice;

import org.smslib.OutboundMessage;
import org.springframework.core.style.ToStringCreator;

/**
 * @author brozow
 *
 */
public class SmsRequest extends MobileMsgRequest {

    private OutboundMessage m_msg;

    /**
     * @param msg
     * @param timeout
     * @param retries
     * @param cb
     * @param responseMatcher
     */
    public SmsRequest(OutboundMessage msg, long timeout, int retries, MobileMsgResponseCallback cb, MobileMsgResponseMatcher responseMatcher) {
        super(timeout, retries, cb, responseMatcher);
        m_msg = msg;
    }

    /**
     * @return the originator
     */
    public String getOriginator() {
        return m_msg.getFrom();
    }

    /**
     * @param originator the originator to set
     */
    public void setOriginator(String originator) {
        m_msg.setFrom(originator);
    }

    /**
     * @return the recipient
     */
    public String getRecipient() {
        return m_msg.getRecipient();
    }

    /**
     * @param recipient the recipient to set
     */
    public void setRecipient(String recipient) {
        m_msg.setRecipient(recipient);
    }

    /**
     * @return the text
     */
    public String getText() {
        return m_msg.getText();
    }

    @Override
    public String getId() {
        return m_msg.getRecipient();
    }

    @Override
    public MobileMsgRequest createNextRetry() {
        if (getRetries() > 0) {
            return new SmsRequest(m_msg, getTimeout(), getRetries() - 1, getCb(), getResponseMatcher());
        } else {
            return null;
        }
    }

    public OutboundMessage getMessage() {
        return m_msg;
    }

    public String toString() {
        return new ToStringCreator(this).append("recipient", getRecipient()).append("text", getText()).toString();
    }
}
