package com.volantis.mcs.protocols.trans;

import com.volantis.mcs.dom.Element;

/**
 * This interface provides the method that should be invoked to perform a table
 * (tree or element) remapping when a container validator returns the REMAP
 * container action.
 */
public interface TransMapper {

    /**
     * This method should be invoked to perform the remapping of table elements
     * into alternative non-table ones when a table is found that cannot
     * otherwise be successfully transformed. The remapping is expected to be
     * limited to the hierarchy rooted at the given DOM element (and will
     * include this given element). If additional new elements are required,
     * the given DOM pool can be used to allocate them (if non-null).
     */
    void remap(Element element, ElementHelper helper);
}
