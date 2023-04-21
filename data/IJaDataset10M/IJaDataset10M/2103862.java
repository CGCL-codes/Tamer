package com.volantis.mcs.protocols.href;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMHelper;

/**
 * Href rule to create anchor element inside the current element and populate
 * it with inherited attributes.
 */
public class HrefInsideRule extends HrefRule {

    /**
     * Create a child element with name "a" and copy the href attribute to the
     * new field.
     *
     * @param element current element
     */
    public void transform(Element element) {
        Element newElement = DOMHelper.insertChildElement(element, "a");
        moveTheHref(element, newElement);
        addInheritedStyles(element, newElement);
    }
}
