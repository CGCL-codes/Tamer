package com.simplerss.handler;

import com.simplerss.dataobject.Cloud;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class CloudHandler extends ChainedHandler {

    Cloud cloud = null;

    String currentTag = null;

    public CloudHandler(ChainedHandler parent) {
        super(parent);
    }

    public void startHandlingEvents(String tag, Attributes attrs) throws SAXException {
        super.startHandlingEvents(tag, attrs);
        cloud = new Cloud();
        currentTag = tag;
    }

    public void endElement(String uri, String name, String qName) throws SAXException {
        String tag = qName.toLowerCase();
        if ("domain".equals(tag)) {
            cloud.setDomain(mText);
        } else if ("path".equals(tag)) {
            cloud.setPath(mText);
        } else if ("port".equals(tag)) {
            cloud.setPort(mText);
        } else if ("protocol".equals(tag)) {
            cloud.setProtocol(mText);
        } else if ("registerprocedure".equals(tag)) {
            cloud.setRegisterProcedure(mText);
        } else if (currentTag.equals(tag)) {
            mParent.setAttribute(currentTag, cloud);
            stopHandlingEvents();
        }
    }
}
