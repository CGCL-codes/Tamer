package net.sourceforge.htmlunit.htmlparser;

import org.xml.sax.Attributes;

public class Element {

    private String uri_;

    private String localName_;

    private String qName_;

    private Attributes attributes_;

    public Element(String uri, String localName, String qName, Attributes attributes) {
        uri_ = uri;
        localName_ = localName;
        uri_ = uri;
        qName_ = qName;
        attributes_ = attributes;
    }

    public String getUri() {
        return uri_;
    }

    public String getLocalName() {
        return localName_;
    }

    public String getQName() {
        return qName_;
    }

    public Attributes getAttributes() {
        return attributes_;
    }
}
