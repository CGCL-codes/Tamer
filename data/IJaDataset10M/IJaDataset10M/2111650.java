package com.simplerss.handler.itunes;

import com.simplerss.dataobject.itunes.ITunesOwner;
import com.simplerss.handler.ChainedHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ITunesOwnerHandler extends ChainedHandler {

    ITunesOwner owner = null;

    String currentTag = null;

    public ITunesOwnerHandler(ChainedHandler parent) {
        super(parent);
    }

    public void startHandlingEvents(String tag, Attributes attrs) throws SAXException {
        super.startHandlingEvents(tag, attrs);
        owner = new ITunesOwner();
        currentTag = tag;
    }

    public void endElement(String uri, String name, String qName) throws SAXException {
        String tag = qName.toLowerCase();
        if ("itunes:name".equals(tag)) {
            owner.setName(mText);
        } else if ("itunes:email".equals(tag)) {
            owner.setEmail(mText);
        } else if (currentTag.equals(tag)) {
            mParent.setAttribute(currentTag, owner);
            stopHandlingEvents();
        }
    }
}
