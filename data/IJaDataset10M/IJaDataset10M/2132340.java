package ch.olsen.products.util.configuration;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import ch.olsen.products.util.configuration.ListOfNamedProperties.PropertyFactory;

/**
 * A NamedProperty is normally contained into a ListOfNamedProperties and
 * adds to a property a name so it can be distinguished from within the list
 *
 * @author Olsen Ltd.
 */
public class NamedProperty<E extends Property> extends PropertyAbstr<NamedItem<E>> implements NamedItem<E> {

    protected String refID;

    protected E property;

    protected final PropertyFactory<E> factory;

    private static final long serialVersionUID = 1L;

    public NamedProperty(String refID, PropertyFactory<E> factory) {
        super(false);
        this.refID = refID;
        this.factory = factory;
    }

    public final String getName() {
        return property.getName();
    }

    public final String getDescription() {
        return property.getDescription();
    }

    public final void parse(Node child, boolean ignoreRootNodeName) throws XMLParseException {
        if (!child.getNodeName().equals("element")) {
            throw new XMLParseException("Elements in the list in " + "wrong format");
        }
        NamedNodeMap map = child.getAttributes();
        if (map == null) {
            throw new XMLParseException("Element in the list does not" + " contain a refID");
        }
        Node attr = map.getNamedItem("refID");
        if (attr == null) {
            throw new XMLParseException("Error in parsing refID");
        }
        refID = attr.getNodeValue();
        try {
            property = factory.newInstance();
        } catch (InternalConfigurationException e1) {
            throw new XMLParseException(e1.getMessage());
        }
        for (Node child2 = child.getFirstChild(); child2 != null; child2 = child2.getNextSibling()) {
            if (child2.getNodeType() != Node.ELEMENT_NODE) continue;
            if (child2.getNodeName().equals(property.getName())) property.parse(child2, false);
        }
    }

    @Override
    public final void fill(Node node) throws XMLFillException {
        Document document = node.getOwnerDocument();
        if (document == null) {
            if (node instanceof Document) document = (Document) node; else throw new XMLFillException("Unable to find Document");
        }
        Element child = document.createElement("element");
        node.appendChild(child);
        Attr attr = document.createAttribute("refID");
        attr.setValue(refID);
        child.setAttributeNode(attr);
        property.setAttribute(false);
        property.fill(child);
    }

    public final void clear() {
        property.clear();
    }

    public final void setDefault() {
        property.setDefault();
    }

    public boolean isDefault() {
        return property.isDefault();
    }

    @Override
    public final void lock() {
        property.lock();
    }

    @Override
    public final void unlock() {
        property.unlock();
    }

    @Override
    public final boolean isLocked() {
        return property.isLocked();
    }

    @Override
    public final void setVisible(boolean b) {
        property.setVisible(b);
    }

    @Override
    public final boolean isVisible() {
        return property.isVisible();
    }

    public final String getRefID() {
        return refID;
    }

    public final void setRefID(String refID) {
        this.refID = refID;
    }

    @SuppressWarnings("unchecked")
    public final void set(E e) {
        if (property == null) property = factory.newInstance();
        property.copy(e);
    }

    public final E get() {
        return property;
    }

    @SuppressWarnings("unchecked")
    public final void copy(NamedItem<E> value) throws InternalConfigurationException {
        property.copy(value.get());
    }

    @Override
    public final void checkIntegrity() throws PropertyIntegrityException {
        property.checkIntegrity();
    }

    @Override
    public String toString() {
        return refID;
    }

    @Override
    public final boolean equals(Object other) {
        if (other.getClass() != NamedProperty.class) return false;
        NamedProperty bp = (NamedProperty) other;
        return bp.getRefID().equals(getRefID()) && bp.get().equals(get());
    }

    @Override
    public final boolean identify(Object other) {
        if (other.getClass() != NamedProperty.class) return false;
        NamedProperty bp = (NamedProperty) other;
        return bp.getRefID().equals(getRefID()) && bp.get().identify(get());
    }
}
