package org.dbe.composer.wfengine.bpeladmin.war.graph.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;

/**
 * Implements a arbitrary coordinate based layout mechanism..
 */
public class SdlXyLayoutManager extends SdlLayoutManagerAdopter {

    /**
     * Default constructor.
     */
    public SdlXyLayoutManager() {
        super();
    }

    /**
     * Overrides method to calculate the minumum size.
     * @see java.awt.LayoutManager#minimumLayoutSize(java.awt.Container)
     */
    public Dimension minimumLayoutSize(Container aParent) {
        int n = aParent.getComponentCount();
        Rectangle rect = new Rectangle();
        Rectangle rv = new Rectangle();
        for (int i = 0; i < n; i++) {
            Component c = aParent.getComponent(i);
            c.getBounds(rv);
            rect = rect.union(rv);
        }
        return aParent.getSize();
    }

    /**
     * Overrides method to layout the components based on their absolute locations.
     * @see java.awt.LayoutManager#layoutContainer(java.awt.Container)
     */
    public void layoutContainer(Container aParent) {
        int n = aParent.getComponentCount();
        int x = 5;
        for (int i = 0; i < n; i++) {
            Component c = aParent.getComponent(i);
            Dimension dim = c.getPreferredSize();
            c.setSize(dim);
            x = x + 5 + dim.width;
        }
    }
}
