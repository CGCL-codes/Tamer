package org.fao.geonet.kernel.csw.domain;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * A customized element set.
 *
 */
public class CustomElementSet {

    /**
     * Each included element is described by a full xpath relative to the document root.
     */
    private List<String> xpaths = new ArrayList<String>();

    public List<String> getXpaths() {
        return xpaths;
    }

    public void setXpaths(List<String> xpaths) {
        this.xpaths = xpaths;
    }

    public boolean add(String s) {
        return xpaths.add(s);
    }

    public boolean remove(Object o) {
        return xpaths.remove(o);
    }
}
