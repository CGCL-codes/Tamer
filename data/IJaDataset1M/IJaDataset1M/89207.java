package com.teknokala.xtempore.xml;

import com.teknokala.xtempore.xml.util.XMLUtil;

/**
 * An attribute. Valid only after start tag or another attribute event.
 *
 * @author Timo Santasalo <timo.santasalo@teknokala.com>
 * @see StartTagEvent
 */
public final class AttributeEvent extends XMLNameEvent {

    public static final long serialVersionUID = 0;

    private String value = null;

    public AttributeEvent() {
    }

    public AttributeEvent(String nsUri, String name, String value) {
        setNamespaceURI(nsUri);
        setLocalName(name);
        setValue(value);
    }

    public AttributeEvent(String nsUri, String prefix, String name, String value) {
        setNamespaceURI(nsUri);
        setPrefix(prefix);
        setLocalName(name);
        setValue(value);
    }

    public void setValue(String value) {
        this.value = value;
        resetXml();
    }

    public String getValue() {
        return value;
    }

    @Override
    protected String internalToXml() {
        return " " + getQName() + "=\"" + XMLUtil.escapeXml(value) + "\"";
    }

    @Override
    public String toString() {
        return "Attr\t" + getQName() + "=\"" + value + "\"";
    }
}
