package org.merlin.xmlv;

import java.util.*;
import org.jdom.*;

/**
 * @author  Brian Pontarelli
 */
public class ElementInfo {

    private boolean textError = false;

    private Element element;

    private List validatedElements = new ArrayList();

    private List validatedAttributes = new ArrayList();

    /** Constructs a new element info for the given element */
    public ElementInfo(Element element) {
        this.element = element;
    }

    /** Returns the element of this info */
    public Element getElement() {
        return element;
    }

    /** Returns the element name of this info */
    public String getElementName() {
        return element.getName();
    }

    public boolean hasText() {
        return (element.getTextTrim() != null && element.getTextTrim().length() != 0);
    }

    public boolean hasTextError() {
        return textError;
    }

    public void setTextError(boolean textError) {
        this.textError = textError;
    }

    /** Returns all the children of this element */
    public List getChildren() {
        return element.getChildren();
    }

    /**
     * Finds the children in the element with the given name. This is only
     * as fast as the Element can lookup children by name
     * @param   name The name of the children to find.
     * @return  The list of children if found or null if not
     */
    public List findChildren(String name) {
        return element.getChildren(name);
    }

    /** Returns true if the element has any children, false otherwise */
    public boolean hasChildren() {
        return element.hasChildren();
    }

    /** Adds an element that has been validated to the list for this info */
    public void addValidatedElement(Element element) {
        validatedElements.add(element);
    }

    /** Returns the list of validated elements of this info */
    public List getValidatedElements() {
        return validatedElements;
    }

    /** Returns true if all the elements of the info were validated */
    public boolean allElementsValidated() {
        return validatedElements.containsAll(element.getChildren());
    }

    /** Returns all the attributes of this info */
    public List getAttributes() {
        return element.getAttributes();
    }

    /**
     * Finds the attribute in the info with the given name. This is only as fast
     * as the Element can lookup an attribute by name
     * @param   name The name of the attribute to find.
     * @return  The attribute if found or null if not
     */
    public Attribute findAttribute(String name) {
        return element.getAttribute(name);
    }

    /** Returns true if the info has any attributes, false if the info is empty */
    public boolean hasAttributes() {
        return element.getAttributes() != null;
    }

    /** Adds an attribute that has been validated to the list for this info */
    public void addValidatedAttribute(Attribute attribute) {
        validatedAttributes.add(attribute);
    }

    /** Returns the list of validated attributes of this info */
    public List getValidatedAttributes() {
        return validatedAttributes;
    }

    /** Returns true if all the attributes of the info were validated */
    protected boolean allAttributesValidated() {
        return validatedAttributes.containsAll(element.getAttributes());
    }
}
