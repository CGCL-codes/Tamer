package org.formaria.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

/**
 * A scaling layout manager. Scales the components in proportion to their original
 * layout size relative to the original container size
 *
 * <p>Copyright (c) Formaria Ltd., 2008</p>
 * $Revision: 1.2 $
 */
public class LayerLayout implements LayoutManager {

    public LayerLayout() {
    }

    /**
   * If the layout manager uses a per-component string,
   * adds the component <code>comp</code> to the layout,
   * associating it 
   * with the string specified by <code>name</code>.
   * 
   * @param name the string to be associated with the component
   * @param comp the component to be added
   */
    public void addLayoutComponent(String name, Component comp) {
    }

    /** 
   * Lays out the specified container.
   * @param parent the container to be laid out 
   */
    public void layoutContainer(Container parent) {
        Dimension d = parent.getSize();
        int width = d.width;
        int height = d.height;
        int numComponents = parent.getComponentCount();
        for (int i = 0; i < numComponents; i++) {
            Component c = parent.getComponent(i);
            c.setBounds(0, 0, width, height);
        }
    }

    /** 
   * Calculates the minimum size dimensions for the specified 
   * container, given the components it contains.
   * @param parent the component to be laid out
   * @see #preferredLayoutSize
   */
    public Dimension minimumLayoutSize(Container parent) {
        int width = 0;
        int height = 0;
        int numComponents = parent.getComponentCount();
        for (int i = 0; i < numComponents; i++) {
            Component c = parent.getComponent(i);
            Dimension d2 = c.getMinimumSize();
            width = Math.max(width, d2.width);
            height = Math.max(height, d2.height);
        }
        return new Dimension(width, height);
    }

    /**
   * Calculates the preferred size dimensions for the specified 
   * container, given the components it contains.
   * @param parent the container to be laid out
   *  
   * @see #minimumLayoutSize
   */
    public Dimension preferredLayoutSize(Container parent) {
        int width = 2;
        int height = 2;
        int numComponents = parent.getComponentCount();
        for (int i = 0; i < numComponents; i++) {
            Component c = parent.getComponent(i);
            Dimension d2 = c.getPreferredSize();
            width = Math.max(width, d2.width);
            height = Math.max(height, d2.height);
        }
        return new Dimension(width, height);
    }

    /**
   * Removes the specified component from the layout.
   * @param comp the component to be removed
   */
    public void removeLayoutComponent(Component comp) {
    }
}
