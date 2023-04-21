package com.quikj.application.web.talk.feature.proactive.messaging;

import java.util.Date;
import java.util.Enumeration;
import net.n3.nanoxml.IXMLElement;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import com.quikj.application.web.talk.messaging.TalkMessageParser;

public class WebInfoElement {

    private String errorMessage = "";

    /** Holds value of property url. */
    private String url;

    /** Holds value of property accessTime. */
    private java.util.Date accessTime = null;

    public WebInfoElement() {
    }

    public String format() {
        StringBuffer buffer = new StringBuffer("<web-info-element");
        buffer.append(" url=\"" + url + "\"");
        buffer.append(" access-time=\"" + accessTime.getTime() + "\"");
        buffer.append("/>\n");
        return buffer.toString();
    }

    /**
	 * Getter for property accessTime.
	 * 
	 * @return Value of property accessTime.
	 * 
	 */
    public java.util.Date getAccessTime() {
        return this.accessTime;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    /**
	 * Getter for property url.
	 * 
	 * @return Value of property url.
	 * 
	 */
    public String getUrl() {
        return this.url;
    }

    public boolean parse(Object node) {
        if (TalkMessageParser.getParserType() == TalkMessageParser.DOM_PARSER) {
            return parseDOM((Node) node);
        } else {
            return parseNANO((IXMLElement) node);
        }
    }

    private boolean parseDOM(Node node) {
        NamedNodeMap attribs = node.getAttributes();
        int length = attribs.getLength();
        for (int i = 0; i < length; i++) {
            Node attr = attribs.item(i);
            if (setAttribute(attr.getNodeName(), attr.getNodeValue()) == false) {
                return false;
            }
        }
        return validate();
    }

    private boolean parseNANO(IXMLElement node) {
        Enumeration e = node.enumerateAttributeNames();
        while (e.hasMoreElements() == true) {
            String name = (String) e.nextElement();
            String value = node.getAttribute(name, null);
            if (setAttribute(name, value) == false) {
                return false;
            }
        }
        return validate();
    }

    /**
	 * Setter for property accessTime.
	 * 
	 * @param accessTime
	 *            New value of property accessTime.
	 * 
	 */
    public void setAccessTime(java.util.Date accessTime) {
        this.accessTime = accessTime;
    }

    private boolean setAttribute(String name, String value) {
        if (name.equals("access-time") == true) {
            try {
                long access_time = Long.parseLong(value);
                accessTime = new Date(access_time);
            } catch (NumberFormatException ex) {
                errorMessage = "Invalid value for the \"access-time\" attribute";
                return false;
            }
        } else if (name.equals("url") == true) {
            url = value;
        }
        return true;
    }

    /**
	 * Setter for property url.
	 * 
	 * @param url
	 *            New value of property url.
	 * 
	 */
    public void setUrl(String url) {
        this.url = url;
    }

    private boolean validate() {
        if (accessTime == null) {
            errorMessage = "The \"access-time\" attribute is not present";
            return false;
        }
        if (url == null) {
            errorMessage = "The \"url\" attribute is not present";
            return false;
        }
        return true;
    }
}
