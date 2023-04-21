package com.simplerss.handler;

import com.simplerss.dataobject.Enclosure;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import java.net.MalformedURLException;
import java.net.URL;

public class EnclosureHandler extends ChainedHandler {

    Enclosure enclosure = null;

    String currentTag = null;

    public EnclosureHandler(ChainedHandler parent) {
        super(parent);
    }

    public void startHandlingEvents(String tag, Attributes attrs) throws SAXException {
        super.startHandlingEvents(tag, attrs);
        enclosure = new Enclosure();
        enclosure.setLength(attrs.getValue("length"));
        enclosure.setType(attrs.getValue("type"));
        try {
            enclosure.setUrl(new URL(attrs.getValue("url")));
        } catch (MalformedURLException e) {
        }
        currentTag = tag;
    }

    public void endElement(String uri, String name, String qName) throws SAXException {
        String tag = qName.toLowerCase();
        if (currentTag.equals(tag)) {
            mParent.setAttribute(currentTag, enclosure);
            stopHandlingEvents();
        }
    }
}
