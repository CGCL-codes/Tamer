package simis.messaging;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

/**
 * Class representing an XML element as used e. g. in SOAP messages.
 * 
 * @author UBT
 * 
 */
public class Element {

    private String name;

    private HashMap<String, TypedContent> attributes;

    private TypedContent content;

    private Vector<Element> children = new Vector<Element>();

    /**
	 * Constructor
	 * 
	 * @param nameToSet
	 *            element's name to be set
	 */
    public Element(String nameToSet) {
        this.name = new String(nameToSet);
    }

    /**
	 * Constructor
	 * 
	 * @param nameToSet
	 *            element's name to be set
	 * @param contentToSet
	 *            element's content to be set
	 */
    public Element(String nameToSet, TypedContent contentToSet) {
        this.name = new String(nameToSet);
        this.content = contentToSet;
    }

    /**
	 * Returns the element's name.
	 * 
	 * @return element's name
	 */
    public String getName() {
        return name;
    }

    /**
	 * Sets the element's name
	 * 
	 * @param nametoSet
	 *            name to be set
	 */
    public void setName(String nameToSet) {
        this.name = nameToSet;
    }

    /**
	 * Returns the element's attributes.
	 * 
	 * @return element's attributes
	 */
    public HashMap<String, TypedContent> getAttributes() {
        return attributes;
    }

    /**
	 * Sets the element's attributes.
	 * 
	 * @param attributesToSet
	 *            attributes to be set
	 */
    public void setAttributes(HashMap<String, TypedContent> attributesToSet) {
        this.attributes = attributesToSet;
    }

    /**
	 * Returns the element's content.
	 * 
	 * @return element's content
	 */
    public TypedContent getContent() {
        return content;
    }

    /**
	 * Sets the element's content.
	 * 
	 * @param contentToSet
	 *            content to be set
	 */
    public void setContent(TypedContent contentToSet) {
        this.content = contentToSet;
    }

    /**
	 * Returns the element's child element(s)
	 * 
	 * @return element's child elements
	 */
    public Vector<Element> getChildren() {
        return children;
    }

    /**
	 * Sets the element's child element(s)
	 * 
	 * @param children
	 */
    public void setChildren(Vector<Element> children) {
        this.children = children;
    }

    /**
	 * Sets a single child element.
	 * 
	 * @param child
	 *            child element to be set
	 */
    public void setChild(Element child) {
        this.children.add(child);
    }

    /**
	 * Returns a cloned copy of this element.
	 * 
	 * @return cloned element
	 */
    public Element clone() {
        Element toReturn;
        if (this.content == null) {
            toReturn = new Element(this.name);
        } else {
            toReturn = new Element(this.name, this.content.clone());
        }
        HashMap<String, TypedContent> tempAttributes = new HashMap<String, TypedContent>();
        if (this.attributes != null) {
            Iterator<String> tempIterator = attributes.keySet().iterator();
            while (tempIterator.hasNext()) {
                tempAttributes.put(tempIterator.next(), this.attributes.get(tempIterator.next()).clone());
            }
            toReturn.setAttributes(tempAttributes);
        }
        Vector<Element> tempElements = new Vector<Element>();
        if (this.children != null) {
            for (Element e : this.children) {
                tempElements.add(e.clone());
            }
            toReturn.setChildren(tempElements);
        }
        return toReturn;
    }
}
