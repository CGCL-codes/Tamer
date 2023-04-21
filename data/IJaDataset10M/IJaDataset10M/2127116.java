package org.formaria.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.SwingUtilities;
import java.util.Hashtable;
import org.formaria.aria.LayoutSupport;
import org.formaria.swing.Page;

/**
 * A guide layout manager. Uses the guides setup in the Aria to control the
 * layout. Guides can be positioned absolutely in terms of pixels, relative to
 * one another or relative to the container so that they scale as the page is
 * resized.
 *
 * <p>Copyright (c) Formaria Ltd., 2008</p>
 * $Revision: 1.12 $
 */
public class GuideLayout extends ScaleLayout implements LayoutManager2, LayoutSupport {

    protected Hashtable constraints;

    protected Component page;

    /** Creates a new instance of GuideLayout */
    public GuideLayout() {
        constraints = new Hashtable();
    }

    /**
   * Lays out the specified container.
   * @param parent the container to be laid out
   */
    public void layoutContainer(Container parent) {
        int numComps = parent.getComponentCount();
        for (int i = 0; i < numComps; i++) {
            Component comp = parent.getComponent(i);
            Object obj = constraints.get(comp);
            if (!((obj == null) || !((obj instanceof String) || (obj instanceof Guide[])))) {
                Rectangle bounds = comp.getBounds();
                int l = bounds.x;
                int t = bounds.y;
                int w = bounds.width;
                int h = bounds.height;
                Guide[] guides = findGuides(obj);
                if (page == null) page = parent;
                Point p;
                if (guides[0] != null) {
                    l = guides[0].getAbsolutePosition();
                    p = SwingUtilities.convertPoint(page, l, t, parent);
                    l = p.x;
                }
                if (guides[1] != null) {
                    t = guides[1].getAbsolutePosition();
                    p = SwingUtilities.convertPoint(page, l, t, parent);
                    t = p.y;
                }
                int r = l + w;
                int b = t + h;
                if (guides[2] != null) {
                    r = guides[2].getAbsolutePosition();
                    p = SwingUtilities.convertPoint(page, r, t, parent);
                    r = p.x;
                    if (guides[0] == null) l = r - w;
                }
                if (guides[3] != null) {
                    b = guides[3].getAbsolutePosition();
                    p = SwingUtilities.convertPoint(page, r, b, parent);
                    b = p.y;
                    if (guides[1] == null) t = b - h;
                }
                comp.setBounds(l, t, r - l, b - t);
            }
            if (comp instanceof Container) {
                LayoutManager lm = ((Container) comp).getLayout();
                if (lm != null) {
                    if (lm instanceof GuideLayout) lm.layoutContainer((Container) comp);
                } else Page.updateChildLayouts((Container) comp);
            }
        }
    }

    /**
   * Get the component constraints. In this case the reference is assumed to be
   * an array of the guide objects. Derived classes may specify the
   * constraints in other ways.
   * @param compConstraints the constraints specification or reference
   * @return an array of component constraints, one for each of the four edges
   */
    protected Guide[] findGuides(Object compConstraints) {
        return (Guide[]) compConstraints;
    }

    /**
   * Adds the specified component to the layout, using the specified
   * constraint object.
   * @param comp the component to be added
   * @param objConstraints  where/how the component is added to the layout.
   */
    public void addLayoutComponent(Component comp, Object objConstraints) {
        invalidateLayout(comp.getParent());
        constraints.remove(comp);
        constraints.put(comp, ((objConstraints != null) ? objConstraints : "-1,-1,-1,-1"));
    }

    /**
   * Calculates the preferred size dimensions for the specified
   * container, given the components it contains.
   * @see #minimumLayoutSize
   * @param parent the container to be laid out
   * @return the preferred layout size
   */
    public Dimension preferredLayoutSize(Container parent) {
        return parent.getSize();
    }

    /**
   * Calculates the minimum size dimensions for the specified
   * container, given the components it contains.
   * @see #preferredLayoutSize
   * @param parent the component to be laid out
   * @return the max layout size
   */
    public Dimension minimumLayoutSize(Container parent) {
        return new Dimension(10, 10);
    }

    /**
   * Calculates the maximum size dimensions for the specified container,
   * given the components it contains.
   * @see java.awt.Component#getMaximumSize
   * @see LayoutManager
   * @param target
   * @return the max layout size
   */
    public Dimension maximumLayoutSize(Container target) {
        return target.getSize();
    }

    /**
   * Returns the alignment along the x axis.  This specifies how
   * the component would like to be aligned relative to other
   * components.  The value should be a number between 0 and 1
   * where 0 represents alignment along the origin, 1 is aligned
   * the furthest away from the origin, 0.5 is centered, etc.
   * @param target the container to lay out
   */
    public float getLayoutAlignmentX(Container target) {
        return 0.0f;
    }

    /**
   * Returns the alignment along the y axis.  This specifies how
   * the component would like to be aligned relative to other
   * components.  The value should be a number between 0 and 1
   * where 0 represents alignment along the origin, 1 is aligned
   * the furthest away from the origin, 0.5 is centered, etc.
   * @param target the container to lay out
   */
    public float getLayoutAlignmentY(Container target) {
        return 0.0f;
    }

    /**
   * Invalidates the layout, indicating that if the layout manager
   * has cached information it should be discarded.
   * @param target the container to lay out
   */
    public void invalidateLayout(Container target) {
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
   * Removes the specified component from the layout.
   * @param comp the component to be removed
   */
    public void removeLayoutComponent(Component comp) {
    }

    /**
   * Get the layout constraints
   * @param comp the component for which the constraints are being retrieved
   * @return the constraints or null if none have been specified.
   */
    public Object getConstraints(Object comp) {
        return constraints.get(comp);
    }

    /**
   * Set the root or page for the layout. The guides are specified in terms of
   * this object.
   * @param comp the root component
   */
    public void setPage(Component comp) {
        page = comp;
    }
}
