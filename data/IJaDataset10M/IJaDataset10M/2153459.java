package net.sf.archimede.model.metadata;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import net.sf.archimede.NodesTypes;

public class ValueElementImpl implements Element {

    private String name = NodesTypes.PREFIX + MetadataDao.VALUE_ELEMENT_NAME;

    private Element parent;

    private Metadata metadata;

    private boolean multipleAttribute = false;

    private List attributesList = new ArrayList();

    private List elementsList = new ArrayList();

    private List childElementTypes = new ArrayList();

    private ElementType defaultElementType;

    public ValueElementImpl(Metadata metadata1, Element parent1) {
        this.metadata = metadata1;
        this.parent = parent1;
    }

    public List getAttributes() {
        if (this.attributesList != null && this.attributesList.size() > 0) {
            List attributes = new ArrayList(1);
            attributes.add(this.attributesList.get(0));
            return attributes;
        }
        return null;
    }

    public void setAttributes(List attributes) {
        if (attributes == null) {
            throw new IllegalArgumentException("Attributes cannot be null");
        }
        if (attributes.size() != 1) {
            throw new IllegalArgumentException("Only one attribute is allowed");
        }
        Attribute attribute = (Attribute) attributes.get(0);
        if (!attribute.getName().equals(NodesTypes.PREFIX + Attribute.VALUE_ATTRIBUTE_NAME)) {
            throw new IllegalArgumentException("Attribute is not a value attribute");
        }
        this.attributesList = attributes;
    }

    public Attribute getAttributeForName(String name1) {
        if (name1.equals(NodesTypes.PREFIX + Attribute.VALUE_ATTRIBUTE_NAME) && this.attributesList.size() > 0) {
            return (Attribute) this.attributesList.get(0);
        }
        return null;
    }

    public List getElements() {
        return this.elementsList;
    }

    public void setElements(List elements) {
        if (elements == null) {
            throw new IllegalArgumentException("Elements cannot be null");
        }
        if (elements.size() > 0) {
            throw new IllegalArgumentException("A value element cannot contain elements");
        }
    }

    public List getElementsForName(String name) {
        List list = new ArrayList();
        for (Iterator it = this.elementsList.iterator(); it.hasNext(); ) {
            Element element = (Element) it.next();
            if (element.getName().equals(name)) {
                list.add(element);
            }
        }
        return list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        throw new IllegalArgumentException("A value element cannot have a custom name");
    }

    public Metadata getMetadataParent() {
        return metadata;
    }

    public void setMetadataParent(Metadata metadata) {
        this.metadata = metadata;
    }

    public Element getParent() {
        return parent;
    }

    public void setParent(Element parent) {
        this.parent = parent;
    }

    public boolean isText() {
        return true;
    }

    public List getNamedChildElementTypes() {
        return this.childElementTypes;
    }

    public void setNamedChildElementTypes(List childElementTypes) {
        if (childElementTypes == null) {
            throw new IllegalArgumentException("ChildElementTypes cannot be null");
        }
        if (childElementTypes.size() > 0) {
            throw new IllegalArgumentException("A value element cannot contain elements");
        }
    }

    public ElementType getDefaultElementType() {
        return null;
    }

    public void setDefaultElementType(ElementType defaultElementType) {
        throw new IllegalArgumentException("A value element cannot contain elements");
    }

    public Element relativeClone() {
        ValueElementImpl clone = new ValueElementImpl(this.metadata, this.parent);
        clone.name = this.name;
        List cloneAttributes = new ArrayList();
        for (Iterator it = this.attributesList.iterator(); it.hasNext(); ) {
            AttributeImpl attributeImpl = (AttributeImpl) it.next();
            cloneAttributes.add(attributeImpl.relativeClone());
        }
        clone.attributesList = cloneAttributes;
        return clone;
    }

    public boolean isValue() {
        return true;
    }

    public Attribute getValueAttribute() {
        return (Attribute) attributesList.get(0);
    }
}
