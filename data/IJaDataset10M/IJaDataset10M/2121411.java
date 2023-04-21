package org.jtools.elements;

import org.jpattern.xml.QName;

public class XMLTextImpl extends ItemImpl implements XMLText {

    private String value;

    public XMLTextImpl() {
        super();
    }

    public XMLTextImpl(Element parent, String value) {
        super(parent, (QName) null);
        this.value = value;
    }

    public XMLTextImpl(Element parent, XMLText src) {
        super(parent, src);
        this.value = src.getValue();
    }

    @Override
    public Item copy(Element parent) {
        return new XMLTextImpl(parent, this);
    }

    public String getValue() {
        return value;
    }

    public void add(XMLTextImpl txt) {
        if (txt != null && txt.value != null) this.value = this.value == null ? txt.value : this.value.concat(txt.value);
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isEmpty() {
        if (value == null) return true;
        if (value.trim().length() == 0) return true;
        return false;
    }

    @Override
    public QName getQName() {
        return null;
    }

    @Override
    public <R, D> R accept(ElementsVisitor<R, D> visitor, D data) {
        return visitor.visit(this, data);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    protected boolean equals(Item obj, boolean contentOnly) {
        if (this == obj) return true;
        if (!super.equals(obj, contentOnly)) return false;
        if (!(obj instanceof XMLTextImpl)) return false;
        XMLTextImpl other = (XMLTextImpl) obj;
        if (value == null) {
            if (other.value != null) return false;
        } else if (!value.equals(other.value)) return false;
        return true;
    }

    public String toString() {
        return value == null ? "" : value;
    }
}
