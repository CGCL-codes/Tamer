package com.volantis.mcs.ibm.websphere.mcsi;

/**
 * Class which encapsulates the information which needs to be maintained
 * from the startElement to the endElement methods.
 */
public class MCSIElementStackEntry {

    /**
     * The Volantis copyright statement
     */
    private static final String mark = "(c) Volantis Systems Ltd 2004. ";

    /**
     * The MCSIElement.
     */
    protected MCSIElement element;

    /**
     * The MCSIAttributes.
     */
    protected MCSIAttributes attributes;

    /**
     * Create a new instance of MCSIElementStackEntry with the specified
     * MCSIElement and MCSIAttributes values.
     * @param element The MCSIElement
     * @param attrs The MCSIAttributes
     */
    public MCSIElementStackEntry(MCSIElement element, MCSIAttributes attrs) {
        this.element = element;
        this.attributes = attrs;
    }

    /**
     * Get the MCSIAttributes
     * @return the MCSIAttributes
     */
    public MCSIAttributes getAttributes() {
        return attributes;
    }

    /**
     * Set the MCSIAttributes
     * @param attributes The MCSIAttributes
     */
    public void setAttributes(MCSIAttributes attributes) {
        this.attributes = attributes;
    }

    /**
     * Get the MCSIElement
     * @return the MCSIElement
     */
    public MCSIElement getElement() {
        return element;
    }

    /**
     * Set the MCSIElement
      * @param element the MCSIElement
     */
    public void setElement(MCSIElement element) {
        this.element = element;
    }
}
