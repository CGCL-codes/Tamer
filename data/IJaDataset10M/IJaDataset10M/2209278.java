package org.kablink.teaming.gwt.client.util;

import java.util.ArrayList;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * This class is used to hold subscription data
 * @author jwootton
 *
 */
public class SubscriptionData implements IsSerializable {

    public static final int SEND_TO_NONE = 0;

    public static final int SEND_TO_PRIMARY_EMAIL_ADDRESS = 1;

    public static final int SEND_TO_MOBILE_EMAIL_ADDRESS = 2;

    public static final int SEND_TO_TEXT_ADDRESS = 4;

    private int m_sendEmailTo;

    private int m_sendEmailToWithoutAttachment;

    private int m_sendTextTo;

    private String m_primaryEmailAddress;

    private String m_mobileEmailAddress;

    private String m_textAddress;

    /**
	 * 
	 */
    public SubscriptionData() {
        m_sendEmailTo = SEND_TO_NONE;
        m_sendEmailToWithoutAttachment = SEND_TO_NONE;
        m_sendTextTo = SEND_TO_NONE;
        m_primaryEmailAddress = null;
        m_mobileEmailAddress = null;
        m_textAddress = null;
    }

    /**
	 * Return the mobile email address
	 */
    public String getMobileEmailAddress() {
        return m_mobileEmailAddress;
    }

    /**
	 * Return the primary email address
	 */
    public String getPrimaryEmailAddress() {
        return m_primaryEmailAddress;
    }

    /**
	 * For the given value return who the recipients are as a string.
	 */
    private String[] getRecipients(int sendTo) {
        ArrayList<String> setting;
        String[] returnValue;
        if (sendTo == SEND_TO_NONE) return null;
        setting = new ArrayList<String>();
        if ((sendTo & SubscriptionData.SEND_TO_PRIMARY_EMAIL_ADDRESS) > 0) setting.add("_primary");
        if ((sendTo & SubscriptionData.SEND_TO_MOBILE_EMAIL_ADDRESS) > 0) setting.add("_mobile");
        if ((sendTo & SubscriptionData.SEND_TO_TEXT_ADDRESS) > 0) setting.add("_text");
        if (setting.size() > 0) returnValue = (String[]) setting.toArray(new String[setting.size()]); else returnValue = null;
        return returnValue;
    }

    /**
	 * Return the value of which addresses should be sent an email.
	 */
    public int getSendEmailTo() {
        return m_sendEmailTo;
    }

    /**
	 * Return the value of which addresses should be sent an email.
	 */
    public String[] getSendEmailToAsString() {
        return getRecipients(m_sendEmailTo);
    }

    /**
	 * Return the value of which addresses should be sent an email without attachments.
	 */
    public int getSendEmailToWithoutAttachment() {
        return m_sendEmailToWithoutAttachment;
    }

    /**
	 * Return the value of which addresses should be sent an email without attachments.
	 */
    public String[] getSendEmailToWithoutAttachmentAsString() {
        return getRecipients(m_sendEmailToWithoutAttachment);
    }

    /**
	 * Return the value of which addresses should be sent a text.
	 */
    public int getSendTextTo() {
        return m_sendTextTo;
    }

    /**
	 * Return the value of which addresses should be sent a text.
	 */
    public String[] getSendTextToAsString() {
        return getRecipients(m_sendTextTo);
    }

    /**
	 * Return the text messaging address
	 */
    public String getTextMessagingAddress() {
        return m_textAddress;
    }

    /**
	 * Parse the "send to" values found in the String[] that came from the db and convert them into an integer.
	 */
    private int parseSendToValues(String[] values) {
        int i;
        int sendTo = SEND_TO_NONE;
        if (values == null) return SEND_TO_NONE;
        for (i = 0; i < values.length; ++i) {
            String value;
            value = values[i];
            if (value != null) {
                if (value.equalsIgnoreCase("_primary")) sendTo |= SEND_TO_PRIMARY_EMAIL_ADDRESS; else if (value.equalsIgnoreCase("_mobile")) sendTo |= SEND_TO_MOBILE_EMAIL_ADDRESS; else if (value.equalsIgnoreCase("_text")) sendTo |= SEND_TO_TEXT_ADDRESS;
            }
        }
        return sendTo;
    }

    /**
	 * Set the mobile email address
	 */
    public void setMobileEmailAddress(String address) {
        m_mobileEmailAddress = address;
    }

    /**
	 * Set the primary email address
	 */
    public void setPrimaryEmailAddress(String address) {
        m_primaryEmailAddress = address;
    }

    /**
	 * Set the value of which addresses should be sent an email.
	 */
    public void setSendEmailTo(String[] values) {
        m_sendEmailTo = parseSendToValues(values);
    }

    /**
	 * Set the value of which addresses should be sent an email.
	 */
    public void setSendEmailTo(int value) {
        m_sendEmailTo = value;
    }

    /**
	 * Set the value of which addresses should be sent an email without attachments.
	 */
    public void setSendEmailToWithoutAttachment(String[] values) {
        m_sendEmailToWithoutAttachment = parseSendToValues(values);
    }

    /**
	 * Set the value of which addresses should be sent an email without attachments.
	 */
    public void setSendEmailToWithoutAttachment(int value) {
        m_sendEmailToWithoutAttachment = value;
    }

    /**
	 * Set the value of which addresses should be sent a text.
	 */
    public void setSendTextTo(String[] values) {
        m_sendTextTo = parseSendToValues(values);
    }

    /**
	 * Set the value of which addresses should be sent a text.
	 */
    public void setSendTextTo(int value) {
        m_sendTextTo = value;
    }

    /**
	 * Set the text messaging address
	 */
    public void setTextMessagingAddress(String address) {
        m_textAddress = address;
    }
}
