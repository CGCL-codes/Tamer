package org.breport.breport;

import org.breport.breport.util.*;
import java.util.*;
import java.io.*;
import javax.mail.internet.*;
import org.w3c.dom.*;

/**
 * Configuration information for sending email
 * @author Scott Peshak (scott@sourceallies.com)
 */
public class EmailConfig {

    /**
	 * Mail host to send through
	 */
    protected String host = "";

    /**
	 * Address to use as the from address
	 */
    protected InternetAddress from;

    /**
	 * Subject for sent email
	 */
    protected String subject = "Bacula Report";

    /**
	 * Message body
	 */
    protected String message = "";

    /**
	 * User to authenticate to mail host as
	 */
    protected String authUser = "";

    /**
	 * Password for mail user
	 */
    protected String authPassword = "";

    /**
	 * Load the config values from an XML fragment
	 * @param emailElem DOM Element of email tag to load.
	 */
    public void loadXml(Element emailElem) throws AddressException {
        Element serverElem = (Element) (emailElem.getElementsByTagName("server").item(0));
        NodeList serverTextList = serverElem.getChildNodes();
        this.setHost((serverTextList.getLength() > 0) ? ((Node) serverTextList.item(0)).getNodeValue().trim() : "");
        Element fromElem = (Element) (emailElem.getElementsByTagName("from").item(0));
        NodeList fromTextList = fromElem.getChildNodes();
        this.setFrom((fromTextList.getLength() > 0) ? ((Node) fromTextList.item(0)).getNodeValue().trim() : "");
        Element subjectElem = (Element) (emailElem.getElementsByTagName("subject").item(0));
        NodeList subjectTextList = subjectElem.getChildNodes();
        this.setSubject((subjectTextList.getLength() > 0) ? ((Node) subjectTextList.item(0)).getNodeValue().trim() : "");
        Element messageElem = (Element) (emailElem.getElementsByTagName("message").item(0));
        NodeList messageTextList = messageElem.getChildNodes();
        this.setMessage((messageTextList.getLength() > 0) ? ((Node) messageTextList.item(0)).getNodeValue().trim() : "");
        Element authUserElem = (Element) (emailElem.getElementsByTagName("authUser").item(0));
        NodeList authUserTextList = authUserElem.getChildNodes();
        this.setAuthUser((authUserTextList.getLength() > 0) ? ((Node) authUserTextList.item(0)).getNodeValue().trim() : "");
        Element authPasswordElem = (Element) (emailElem.getElementsByTagName("authPassword").item(0));
        NodeList authPasswordTextList = authPasswordElem.getChildNodes();
        this.setAuthPassword((authPasswordTextList.getLength() > 0) ? ((Node) authPasswordTextList.item(0)).getNodeValue().trim() : "");
    }

    /**
	 * Set email server host
	 * @param host Hostname of email server
	 */
    public void setHost(String host) {
        this.host = host;
    }

    /**
	 * Set the From address
	 * @param from From address
	 */
    public void setFrom(String from) throws AddressException {
        this.from = new InternetAddress(from);
    }

    /**
	 * Set email subject
	 * @param sub Email subject
	 */
    public void setSubject(String sub) {
        this.subject = sub;
    }

    /**
	 * Set email message
	 * @param mess Message
	 */
    public void setMessage(String mess) {
        this.message = mess;
    }

    /**
	 * Set SMTP authentication user
	 * @param user Username
	 */
    public void setAuthUser(String user) {
        this.authUser = user;
    }

    /**
	 * Set SMTP authentication password
	 * @param pass Password
	 */
    public void setAuthPassword(String pass) {
        this.authPassword = pass;
    }

    /**
	 * Get email server host
	 */
    public String getHost() {
        return this.host;
    }

    /**
	 * Get email from address
	 */
    public InternetAddress getFrom() {
        return this.from;
    }

    /**
	 * Get email subject
	 */
    public String getSubject() {
        return this.subject;
    }

    /**
	 * Get email message
	 */
    public String getMessage() {
        return this.message;
    }

    /**
	 * Get SMTP authentication user
	 */
    public String getAuthUser() {
        return this.authUser;
    }

    /**
	 * Get SMTP authentication password
	 */
    public String getAuthPassword() {
        return this.authPassword;
    }

    /**
	 * Check to see if authentication is required
	 */
    public boolean authRequired() {
        return (!this.authPassword.equals("") && !this.authUser.equals(""));
    }
}
