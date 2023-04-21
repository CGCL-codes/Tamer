package com.volantis.mcs.xdime.ticker;

import com.volantis.mcs.protocols.ticker.attributes.ItemTitleAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;

/**
 * ItemTitle element.
 */
public class ItemTitleElement extends ItemPropertyElement {

    /**
     * Creates and returns new instance of ItemTitleElement, 
     * initalised with empty attributes.
     * @param context
     */
    public ItemTitleElement(XDIMEContextInternal context) {
        super(TickerElements.ITEM_TITLE, context);
        protocolAttributes = new ItemTitleAttributes();
    }
}
