package com.jaeksoft.searchlib.crawler.web.database;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import com.jaeksoft.searchlib.util.DomUtils;
import com.jaeksoft.searchlib.util.StringUtils;
import com.jaeksoft.searchlib.util.XmlWriter;

public class CredentialItem {

    private String pattern;

    private String username;

    private String password;

    public CredentialItem() {
        pattern = null;
        username = null;
        password = null;
    }

    public CredentialItem(String pattern, String username, String password) {
        this.pattern = pattern;
        this.username = username;
        this.password = password;
    }

    public static CredentialItem fromXml(Node node) {
        CredentialItem credentialItem = new CredentialItem();
        credentialItem.setPattern(DomUtils.getText(node));
        credentialItem.setUsername(StringUtils.base64decode(DomUtils.getAttributeText(node, "username")));
        credentialItem.setPassword(StringUtils.base64decode(DomUtils.getAttributeText(node, "password")));
        return credentialItem;
    }

    public void writeXml(XmlWriter xmlWriter) throws UnsupportedEncodingException, SAXException {
        xmlWriter.startElement("credential", "username", new String(StringUtils.base64encode(username)), "password", new String(StringUtils.base64encode(password)));
        xmlWriter.textNode(pattern);
        xmlWriter.endElement();
    }

    public void copy(CredentialItem credential) {
        credential.pattern = this.pattern;
        credential.username = this.username;
        credential.password = this.password;
    }

    /**
	 * @return the pattern
	 */
    public String getPattern() {
        return pattern;
    }

    /**
	 * 
	 * @return an URL object
	 * @throws MalformedURLException
	 */
    public URL extractUrl() throws MalformedURLException {
        return new URL(pattern);
    }

    /**
	 * @param pattern
	 *            the pattern to set
	 */
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    /**
	 * @return the username
	 */
    public String getUsername() {
        return username;
    }

    /**
	 * @param username
	 *            the username to set
	 */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
	 * @return the password
	 */
    public String getPassword() {
        return password;
    }

    /**
	 * @param password
	 *            the password to set
	 */
    public void setPassword(String password) {
        this.password = password;
    }

    public boolean match(String sUrl) {
        return sUrl.startsWith(pattern);
    }

    public String getURLUserInfo() throws UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();
        sb.append(URLEncoder.encode(username, "UTF-8"));
        sb.append(':');
        sb.append(URLEncoder.encode(password, "UTF-8"));
        return sb.toString();
    }
}
