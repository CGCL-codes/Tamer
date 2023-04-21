package com.volantis.mcs.marlin.sax;

import com.volantis.mcs.integration.iapi.IAPIAttributes;
import com.volantis.mcs.integration.iapi.IAPIElement;

/**
 * Class which encapsulates the information which needs to be maintained
 * from the startElement to the endElement methods.
 */
public class IAPIElementStackEntry {

    /**
     * The Volantis copyright statement
     */
    private static final String mark = "(c) Volantis Systems Ltd 2003. ";

    /**
     * The IAPIElement.
     */
    protected IAPIElement element;

    /**
     * The IAPIAttributes.
     */
    protected IAPIAttributes attributes;

    /**
     * Create a new instance of IAPIElementStackEntry with the specified
     * IAPIElement and IAPIAttributes values.
     * @param element The IAPIElement
     * @param attrs The IAPIAttributes
     */
    public IAPIElementStackEntry(IAPIElement element, IAPIAttributes attrs) {
        this.element = element;
        this.attributes = attrs;
    }

    /**
     * Get the IAPIAttributes
     * @return the IAPIAttributes
     */
    public IAPIAttributes getAttributes() {
        return attributes;
    }

    /**
     * Set the IAPIAttributes
     * @param attributes The IAPIAttributes
     */
    public void setAttributes(IAPIAttributes attributes) {
        this.attributes = attributes;
    }

    /**
     * Get the IAPIElement
     * @return the IAPIElement
     */
    public IAPIElement getElement() {
        return element;
    }

    /**
     * Set the IAPIElement
      * @param element the IAPIElement
     */
    public void setElement(IAPIElement element) {
        this.element = element;
    }
}
