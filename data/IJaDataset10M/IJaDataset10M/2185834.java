package javax.swing.addon;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;

public class CenterColumnLayout implements LayoutManager2 {

    public static final String EAST = "East", BOTTOM = EAST;

    public static final String WEST = "West", TOP = WEST;

    public static final String CENTER = "Center";

    private boolean vertical;

    private int gap;

    private Component west, east, center;

    public CenterColumnLayout() {
        this(0, false);
    }

    public CenterColumnLayout(int gap) {
        this(gap, false);
    }

    public CenterColumnLayout(int gap, boolean vertical) {
        this.gap = gap;
        this.vertical = vertical;
    }

    public int getHgap() {
        return gap;
    }

    public void setGap(int gap) {
        this.gap = gap;
    }

    public void addLayoutComponent(Component comp, Object constraints) {
        synchronized (comp.getTreeLock()) {
            if ((constraints == null) || (constraints instanceof String)) addLayoutComponent((String) constraints, comp); else throw new IllegalArgumentException("cannot add to layout: constraint must be a string (or null)");
        }
    }

    /**
	 * @deprecated  replaced by <code>addLayoutComponent(Component, Object)</code>.
	 */
    @Deprecated
    public void addLayoutComponent(String name, Component comp) {
        synchronized (comp.getTreeLock()) {
            if (name == null) name = CENTER;
            if (CENTER.equals(name)) center = comp; else if (EAST.equals(name)) east = comp; else if (WEST.equals(name)) west = comp; else throw new IllegalArgumentException("cannot add to layout: unknown constraint: " + name);
        }
    }

    public void removeLayoutComponent(Component comp) {
        synchronized (comp.getTreeLock()) {
            if (comp == center) center = null; else if (comp == east) east = null; else if (comp == west) west = null;
        }
    }

    public Component getLayoutComponent(Object constraints) {
        if (CENTER.equals(constraints)) return center; else if (WEST.equals(constraints)) return west; else if (EAST.equals(constraints)) return east; else throw new IllegalArgumentException("cannot get component: unknown constraint: " + constraints);
    }

    public Dimension minimumLayoutSize(Container target) {
        synchronized (target.getTreeLock()) {
            Dimension dim = new Dimension(0, 0);
            Component c = null;
            if (vertical) {
                if ((c = east) != null) {
                    Dimension d = c.getMinimumSize();
                    dim.height += d.height + gap;
                    dim.width = Math.max(d.width, dim.width);
                }
                if ((c = west) != null) {
                    Dimension d = c.getMinimumSize();
                    dim.height += d.height + gap;
                    dim.width = Math.max(d.width, dim.width);
                }
                if ((c = center) != null) {
                    Dimension d = c.getMinimumSize();
                    dim.height += d.height;
                    dim.width = Math.max(d.width, dim.width);
                }
            } else {
                if ((c = east) != null) {
                    Dimension d = c.getMinimumSize();
                    dim.width += d.width + gap;
                    dim.height = Math.max(d.height, dim.height);
                }
                if ((c = west) != null) {
                    Dimension d = c.getMinimumSize();
                    dim.width += d.width + gap;
                    dim.height = Math.max(d.height, dim.height);
                }
                if ((c = center) != null) {
                    Dimension d = c.getMinimumSize();
                    dim.width += d.width;
                    dim.height = Math.max(d.height, dim.height);
                }
            }
            Insets insets = target.getInsets();
            dim.width += insets.left + insets.right;
            dim.height += insets.top + insets.bottom;
            return dim;
        }
    }

    public Dimension preferredLayoutSize(Container target) {
        synchronized (target.getTreeLock()) {
            Dimension dim = new Dimension(0, 0);
            Component c = null;
            if (vertical) {
                if ((c = east) != null) {
                    Dimension d = c.getPreferredSize();
                    dim.height += d.height + gap;
                    dim.width = Math.max(d.width, dim.width);
                }
                if ((c = west) != null) {
                    Dimension d = c.getPreferredSize();
                    dim.height += d.height + gap;
                    dim.width = Math.max(d.width, dim.width);
                }
                if ((c = center) != null) {
                    Dimension d = c.getPreferredSize();
                    dim.height += d.height;
                    dim.width = Math.max(d.width, dim.width);
                }
            } else {
                if ((c = east) != null) {
                    Dimension d = c.getPreferredSize();
                    dim.width += d.width + gap;
                    dim.height = Math.max(d.height, dim.height);
                }
                if ((c = west) != null) {
                    Dimension d = c.getPreferredSize();
                    dim.width += d.width + gap;
                    dim.height = Math.max(d.height, dim.height);
                }
                if ((c = center) != null) {
                    Dimension d = c.getPreferredSize();
                    dim.width += d.width;
                    dim.height = Math.max(d.height, dim.height);
                }
            }
            Insets insets = target.getInsets();
            dim.width += insets.left + insets.right;
            dim.height += insets.top + insets.bottom;
            return dim;
        }
    }

    public Dimension maximumLayoutSize(Container target) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    public float getLayoutAlignmentX(Container parent) {
        return 0.5f;
    }

    public float getLayoutAlignmentY(Container parent) {
        return 0.5f;
    }

    public void invalidateLayout(Container target) {
    }

    public void layoutContainer(Container target) {
        synchronized (target.getTreeLock()) {
            Insets insets = target.getInsets();
            if (vertical) {
                int leading = insets.left;
                int width = target.getWidth() - insets.right - insets.left;
                int top = insets.top;
                int bottom = target.getHeight() - insets.bottom;
                int sideHeight = (bottom - top - gap) / 2;
                if (center != null) {
                    sideHeight = (bottom - top - center.getPreferredSize().height - 2 * gap) / 2;
                    center.setBounds(leading, top + sideHeight + gap, width, center.getPreferredSize().height);
                }
                if (east != null) east.setBounds(leading, top, width, sideHeight);
                if (west != null) west.setBounds(leading, top + sideHeight + gap + (center != null ? center.getPreferredSize().height + gap : 0), width, sideHeight);
            } else {
                int top = insets.top;
                int height = target.getHeight() - insets.bottom - insets.top;
                int left = insets.left;
                int right = target.getWidth() - insets.right;
                int sideWidth = (right - left - gap) / 2;
                if (center != null) {
                    sideWidth = (right - left - center.getPreferredSize().width - 2 * gap) / 2;
                    center.setBounds(left + sideWidth + gap, top, center.getPreferredSize().width, height);
                }
                if (east != null) east.setBounds(left, top, sideWidth, height);
                if (west != null) west.setBounds(left + sideWidth + gap + (center != null ? center.getPreferredSize().width + gap : 0), top, sideWidth, height);
            }
        }
    }

    public String toString() {
        return getClass().getName() + "[hgap=" + gap + "]";
    }
}
