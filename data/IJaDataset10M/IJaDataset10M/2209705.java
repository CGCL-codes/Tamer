package org.jtools.elements;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import org.jpattern.xml.QName;

public class ElementImpl extends ItemImpl implements Element {

    private final List<Attribute> attributes = new ArrayList<Attribute>();

    private final List<Child> childElements = new ArrayList<Child>();

    private <T extends Item> boolean equals(List<T> list1, List<T> list2, boolean contentOnly) {
        if (list1 == null) return list2 == null;
        if (list2 == null) return false;
        if (list1.size() != list2.size()) return false;
        ListIterator<T> e1 = list1.listIterator();
        ListIterator<T> e2 = list2.listIterator();
        while (e1.hasNext() && e2.hasNext()) {
            T o1 = e1.next();
            T o2 = e2.next();
            if (!(o1 == null ? o2 == null : (contentOnly ? o1.equalsContent(o2) : o1.equals(o2)))) return false;
        }
        return !(e1.hasNext() || e2.hasNext());
    }

    public ElementImpl() {
    }

    public ElementImpl(Element parent, QName name) {
        super(parent, name);
    }

    protected ElementImpl(Element parent, Element src) {
        super(parent, src);
        for (Attribute attr : src.getAttributes()) attributes.add((Attribute) attr.copy(this));
        for (Child child : src.getChildElements()) childElements.add((Child) child.copy(this));
    }

    @Override
    public Item copy(Element parent) {
        return new ElementImpl(parent, this);
    }

    public Collection<Child> getChildElements() {
        return childElements;
    }

    public Child getFirstChild() {
        for (Child element : getChildElements()) return element;
        return null;
    }

    public Child getNextChild(Child previous) {
        boolean found = false;
        for (Child element : getChildElements()) {
            if (found) return element; else if (element == previous) found = true;
        }
        if (found) return null;
        throw new NoSuchElementException();
    }

    public Collection<Attribute> getAttributes() {
        return attributes;
    }

    protected final <T extends Child> T internalCreate(T child) {
        childElements.add(child);
        return child;
    }

    protected Element newElement(QName name) {
        return new ElementImpl(this, name);
    }

    public void removeChildElement(Child child) {
        childElements.remove(child);
    }

    public Element create(QName name) {
        return internalCreate(newElement(name));
    }

    protected final <T extends Attribute> T internalSet(T attribute) {
        attributes.add(attribute);
        return attribute;
    }

    protected Attribute newAttribute(QName name, Object value) {
        return new AttributeImpl(this, name, value);
    }

    public void set(QName name, Object value) {
        internalSet(newAttribute(name, value));
    }

    @Override
    public <R, D> R accept(ElementsVisitor<R, D> visitor, D data) {
        return visitor.visit(this, data);
    }

    public void addText(String txt) {
        internalCreate(new XMLTextImpl(this, txt));
    }

    public String getText() {
        StringBuilder sb = null;
        for (Child child : getChildElements()) {
            if (child instanceof XMLText) {
                if (sb == null) sb = new StringBuilder(250);
                sb.append(((XMLText) child).getValue());
            }
        }
        return sb == null ? null : sb.toString();
    }

    public void trim() {
        Collection<Child> childElements = getChildElements();
        Child[] childs = childElements.toArray(new Child[childElements.size()]);
        XMLText lastText = null;
        for (int i = 0; i < childs.length; i++) {
            if (childs[i] instanceof XMLText) {
                if (lastText == null) lastText = (XMLText) childs[i]; else {
                    lastText.add((XMLTextImpl) childs[i]);
                    removeChildElement(childs[i]);
                    childs[i] = null;
                }
            } else lastText = null;
        }
        for (Child child : childs) {
            if (child != null) {
                if (child instanceof XMLText) {
                    if (((XMLText) child).isEmpty()) removeChildElement(child);
                } else if (child instanceof Element) ((Element) child).trim();
            }
        }
    }

    public String toRenderedString() {
        StringWriter buffer = new StringWriter();
        PrintWriter out = new PrintWriter(buffer);
        accept(new XMLRenderer(), out);
        out.flush();
        out.close();
        return buffer.toString();
    }

    public String toString() {
        StringWriter buffer = new StringWriter();
        PrintWriter out = new PrintWriter(buffer);
        accept(new XMLRenderer(true), out);
        out.flush();
        out.close();
        return buffer.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((attributes == null) ? 0 : attributes.hashCode());
        result = prime * result + ((childElements == null) ? 0 : childElements.hashCode());
        return result;
    }

    @Override
    protected boolean equals(Item obj, boolean contentOnly) {
        if (this == obj) return true;
        if (!super.equals(obj, contentOnly)) return false;
        if (!(obj instanceof ElementImpl)) return false;
        ElementImpl other = (ElementImpl) obj;
        if (!equals(attributes, other.attributes, contentOnly)) return false;
        if (!equals(childElements, other.childElements, contentOnly)) return false;
        return true;
    }
}
