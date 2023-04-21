package com.volantis.mcs.xdime.xhtml2;

import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xml.schema.model.ElementType;

/**
 * Generic list element from which all list elements inherit.
 */
public abstract class GenericListElement extends XHTML2Element {

    protected GenericListElement(ElementType type, XDIMEContextInternal context) {
        super(type, context);
    }
}
