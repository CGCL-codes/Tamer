package com.volantis.mcs.xdime.widgets;

import com.volantis.mcs.protocols.widgets.attributes.FieldExpanderAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;

/**
 * Class for field-expander XDIME2 element
 */
public class FieldExpanderElement extends WidgetElement {

    public FieldExpanderElement(XDIMEContextInternal context) {
        super(WidgetElements.FIELD_EXPANDER, context);
        protocolAttributes = new FieldExpanderAttributes();
    }
}
