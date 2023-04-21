package com.vividsolutions.jts.index.strtree;

/**
 * Boundable wrapper for a non-Boundable spatial object. Used internally by
 * AbstractSTRtree.
 *
 * @version 1.7
 */
public class ItemBoundable implements Boundable {

    private Object bounds;

    private Object item;

    public ItemBoundable(Object bounds, Object item) {
        this.bounds = bounds;
        this.item = item;
    }

    public Object getBounds() {
        return bounds;
    }

    public Object getItem() {
        return item;
    }
}
