package org.xbeans;

import java.util.EventObject;
import org.w3c.dom.Document;

public class DOMEvent extends EventObject {

    private transient Document document;

    public DOMEvent(Object source, Document document) {
        super(source);
        this.document = document;
    }

    public Document getDocument() {
        return document;
    }
}
