package com.swgman.util.xml;

public abstract class XmlAttribute {

    private final boolean mandatory;

    private final String name;

    private final XmlElement element;

    protected XmlAttribute(XmlElement element, String name, boolean mandatory) {
        this.element = element;
        this.name = name;
        this.mandatory = mandatory;
        element.addKnownAttribute(this);
    }

    public final String getName() {
        return name;
    }

    public final boolean isMandatory() {
        return mandatory;
    }

    public final XmlElement getElement() {
        return element;
    }

    protected abstract void set(String value) throws XmlMalformedAttributeException;

    public abstract boolean isSet();

    public abstract void reset();

    protected final void checkIsSet() {
        if (!isSet()) throw new IllegalStateException();
    }

    public abstract String getValueAsString();

    protected abstract boolean equalsAttribute(XmlAttribute other);

    public final boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof XmlAttribute)) return false;
        XmlAttribute other = (XmlAttribute) obj;
        if (!isSet()) return !other.isSet();
        if (!other.isSet()) return false;
        return equalsAttribute(other);
    }
}
