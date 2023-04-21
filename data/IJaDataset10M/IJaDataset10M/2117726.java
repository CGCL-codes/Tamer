package org.kablink.teaming.gwt.client.rpc.shared;

import java.util.ArrayList;
import java.util.List;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * This class holds the response data for a request for email
 * notification information.
 * 
 * @author drfoster@novell.com
 */
public class EmailNotificationInfoRpcResponseData implements IsSerializable, VibeRpcResponseData {

    private boolean m_overridePresets;

    private List<EmailAddressInfo> m_emailAddresses;

    private List<String> m_digestAddresses;

    private List<String> m_msgAddresses;

    private List<String> m_msgNoAttAddresses;

    private List<String> m_textAddresses;

    private String m_bannerHelpUrl;

    private String m_overrideHelpUrl;

    /**
	 * Inner class used to represent an email address.
	 */
    public static class EmailAddressInfo implements IsSerializable {

        private String m_ema;

        private String m_type;

        /**
		 * Constructor method.
		 * 
		 * For GWT serialization, must have a zero parameter
		 * constructor.
		 */
        public EmailAddressInfo() {
            super();
        }

        /**
		 * Constructor method.
		 * 
		 * @param type
		 * @param ema
		 */
        public EmailAddressInfo(String type, String ema) {
            this();
            setType(type);
            setAddress(ema);
        }

        /**
		 * Get'er methods.
		 * 
		 * @return
		 */
        public String getAddress() {
            return m_ema;
        }

        public String getType() {
            return m_type;
        }

        /**
		 * Set'er methods.
		 * 
		 * @param
		 */
        public void setAddress(String ema) {
            m_ema = ema;
        }

        public void setType(String type) {
            m_type = type;
        }
    }

    /**
	 * Constructor method.
	 * 
	 * For GWT serialization, must have a zero parameter constructor.
	 */
    public EmailNotificationInfoRpcResponseData() {
        super();
        m_emailAddresses = new ArrayList<EmailAddressInfo>();
        m_digestAddresses = new ArrayList<String>();
        m_msgAddresses = new ArrayList<String>();
        m_msgNoAttAddresses = new ArrayList<String>();
        m_textAddresses = new ArrayList<String>();
    }

    /**
	 * Get'er methods.
	 * 
	 * @return
	 */
    public boolean getOverridePresets() {
        return m_overridePresets;
    }

    public List<EmailAddressInfo> getEmailAddresses() {
        return m_emailAddresses;
    }

    public List<String> getDigestAddresses() {
        return m_digestAddresses;
    }

    public List<String> getMsgAddresses() {
        return m_msgAddresses;
    }

    public List<String> getMsgNoAttAddresses() {
        return m_msgNoAttAddresses;
    }

    public List<String> getTextAddresses() {
        return m_textAddresses;
    }

    public String getBannerHelpUrl() {
        return m_bannerHelpUrl;
    }

    public String getOverrideHelpUrl() {
        return m_overrideHelpUrl;
    }

    /**
	 * Set'er methods.
	 * 
	 * @param
	 */
    public void setOverridePresets(boolean overridePresets) {
        m_overridePresets = overridePresets;
    }

    public void setEmailAddresses(List<EmailAddressInfo> emailAddresses) {
        m_emailAddresses = emailAddresses;
    }

    public void setDigestAddresses(List<String> digestAddresses) {
        m_digestAddresses = digestAddresses;
    }

    public void setMsgAddresses(List<String> msgAddresses) {
        m_msgAddresses = msgAddresses;
    }

    public void setMsgNoAttAddresses(List<String> msgNoAttAddresses) {
        m_msgNoAttAddresses = msgNoAttAddresses;
    }

    public void setTextAddresses(List<String> textAddresses) {
        m_textAddresses = textAddresses;
    }

    public void setBannerHelpUrl(String bannerHelpUrl) {
        m_bannerHelpUrl = bannerHelpUrl;
    }

    public void setOverrideHelpUrl(String overrideHelpUrl) {
        m_overrideHelpUrl = overrideHelpUrl;
    }

    /**
	 * Adds an email address to the list of email addresses.
	 * 
	 * @param type
	 * @param ema
	 */
    public void addEmailAddress(String type, String ema) {
        addEmailAddress(new EmailAddressInfo(type, ema));
    }

    /**
	 * Adds an email address to a list of email addresses.
	 * 
	 * @param
	 */
    public void addEmailAddress(EmailAddressInfo ema) {
        m_emailAddresses.add(ema);
    }

    public void addDigestAddress(String ema) {
        m_digestAddresses.add(ema);
    }

    public void addMsgAddress(String ema) {
        m_msgAddresses.add(ema);
    }

    public void addMsgNoAttAddress(String ema) {
        m_msgNoAttAddresses.add(ema);
    }

    public void addTextAddress(String ema) {
        m_textAddresses.add(ema);
    }
}
