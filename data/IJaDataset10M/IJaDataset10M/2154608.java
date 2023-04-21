package org.apache.batik.svggen;

import org.w3c.dom.Element;

/**
 * Used to represent an SVG Composite. This can be achieved with
 * to values: an SVG opacity and a filter
 *
 * @author <a href="mailto:vincent.hardy@eng.sun.com">Vincent Hardy</a>
 * @version $Id: SVGFilterDescriptor.java,v 1.1 2005/11/21 09:51:19 dev Exp $
 */
public class SVGFilterDescriptor {

    private Element def;

    private String filterValue;

    public SVGFilterDescriptor(String filterValue) {
        this.filterValue = filterValue;
    }

    public SVGFilterDescriptor(String filterValue, Element def) {
        this(filterValue);
        this.def = def;
    }

    public String getFilterValue() {
        return filterValue;
    }

    public Element getDef() {
        return def;
    }
}
