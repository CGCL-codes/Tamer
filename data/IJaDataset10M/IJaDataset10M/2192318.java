package org.jabber.jabberbeans.sax.Extension;

import org.jabber.jabberbeans.Extension.*;
import org.jabber.jabberbeans.util.*;
import org.jabber.jabberbeans.sax.SubHandler;
import org.xml.sax.SAXException;
import org.xml.sax.AttributeList;

/**
 * Handler class to build jabber:iq:browse objects
 *
/*
 * @author  Shawn Wilton <a href="mailto:shawn@black9.net">
 *                      <i>&lt;shawn@black9.net&gt;</i></a>
 * @author  $Author: shawnxb $
 * @version $Revision: 1.1 $
 */
public class IQBrowseHandler extends SubHandler {

    /** used to capture data between element tags */
    private StringBuffer elementChars;

    /** builder for IQPassExtension objects */
    private IQBrowseBuilder builder;

    public IQBrowseHandler() {
        super();
        builder = new IQBrowseBuilder();
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        elementChars.append(ch, start, length);
    }

    protected void startHandler(String name, AttributeList attributes) throws SAXException {
        elementChars = new StringBuffer();
        builder.reset();
        builder.setCategory(name);
        if (attributes.getValue("xmlns") != null) builder.setIQ(true);
        if (attributes.getValue("type") != null) builder.setType(attributes.getValue("type"));
        if (attributes.getValue("name") != null) builder.setName(attributes.getValue("name"));
        if (attributes.getValue("jid") != null) builder.setJID(JID.fromString(attributes.getValue("jid")));
    }

    protected void handleStartElement(String name, AttributeList attributes) throws SAXException {
        elementChars = new StringBuffer();
        if (!"ns".equals(name)) setChildSubHandler(new IQBrowseHandler(), name, attributes);
    }

    protected void handleEndElement(String name) throws SAXException {
        if ("ns".equals(name)) builder.addNameSpace(new String(elementChars));
    }

    protected Object stopHandler(String name) throws SAXException {
        try {
            return builder.build();
        } catch (InstantiationException e) {
            e.fillInStackTrace();
            throw new SAXException(e);
        }
    }

    protected void receiveChildData(SubHandler subHandler, Object o) {
        builder.addChild((BrowseItem) o);
    }
}
