package com.nexirius.framework.gadgets;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * lays out its components in horizontal or vertical array (the elements are resized to their
 * preferred size (except FULL_SIZE))
 */
public class ArrayLayout implements LayoutManager {

    public static final int CENTER = 0;

    public static final int BEGINNING = 1;

    public static final int END = 2;

    public static final int FULL_SIZE = 3;

    int m_gap = 10;

    int m_margin = 2;

    boolean m_horizontal;

    int m_alignment = 0;

    boolean m_pack = true;

    public ArrayLayout(boolean horizontal) {
        m_horizontal = horizontal;
    }

    public ArrayLayout(boolean horizontal, int alignment) {
        m_horizontal = horizontal;
        m_alignment = alignment;
    }

    public void initComponents(Container parent) {
        Component comp[] = parent.getComponents();
        int i;
        for (i = 0; i < comp.length; ++i) {
            if (comp[i].getSize().width == 0) {
                comp[i].setSize(comp[i].getPreferredSize());
            }
        }
    }

    public void addLayoutComponent(String s, Component c) {
    }

    public void removeLayoutComponent(Component c) {
    }

    /**
     * @return the initial size
     */
    public Dimension preferredLayoutSize(Container parent) {
        Component comp[] = parent.getComponents();
        if (comp == null || comp.length == 0) {
            return new Dimension(m_gap, m_gap);
        }
        initComponents(parent);
        int all = (comp.length + 1) * m_gap;
        Dimension d = minimumLayoutSize(parent);
        return m_horizontal ? new Dimension(d.width + all, d.height + 2 * m_margin) : new Dimension(d.width + 2 * m_margin, d.height + all);
    }

    /**
     * @return the initial size
     */
    public Dimension minimumLayoutSize(Container parent) {
        Component comp[] = parent.getComponents();
        int i;
        if (comp == null || comp.length == 0) {
            return new Dimension(0, 0);
        }
        initComponents(parent);
        int sum = 0;
        int max = 0;
        for (i = 0; i < comp.length; ++i) {
            sum += getSize(comp[i]);
            max = Math.max(max, getOtherSize(comp[i]));
        }
        sum += getAdd(parent, false);
        max += getAdd(parent, true);
        return m_horizontal ? new Dimension(sum, max) : new Dimension(max, sum);
    }

    /**
     * Lays out the container in the specified panel.
     *
     * @param parent the specified component being laid out
     * @see Container
     */
    public void layoutContainer(Container parent) {
        int total = getUsableSize(parent);
        Component comp[] = parent.getComponents();
        int i;
        if (comp == null || comp.length == 0) {
            return;
        }
        initComponents(parent);
        int sum = 0;
        for (i = 0; i < comp.length; ++i) {
            sum += getSize(comp[i]);
        }
        int gap = 0;
        if (sum < total) {
            if (m_pack) {
                gap = m_gap;
            } else {
                gap = (total - sum) / (comp.length + 1);
            }
        }
        int other = getUsableOtherSize(parent);
        int pos = gap + getAdd(parent, false, true);
        for (i = 0; i < comp.length; ++i) {
            int u = 0;
            int v = 0;
            switch(m_alignment) {
                case CENTER:
                    u = pos;
                    v = (other - getOtherSize(comp[i])) / 2;
                    break;
                case BEGINNING:
                    u = pos;
                    v = m_margin;
                    break;
                case END:
                    u = pos;
                    v = other - getOtherSize(comp[i]) - m_margin + getAdd(parent, true, true);
                    break;
                case FULL_SIZE:
                    u = pos;
                    v = m_margin + getAdd(parent, true, true);
                    if (m_horizontal) {
                        comp[i].setSize(getSize(comp[i]), other - 2 * m_margin);
                    } else {
                        comp[i].setSize(other - 2 * m_margin, getSize(comp[i]));
                    }
                    break;
            }
            if (m_horizontal) {
                comp[i].setLocation(u, v);
            } else {
                comp[i].setLocation(v, u);
            }
            pos += getSize(comp[i]) + gap;
        }
    }

    private int getAdd(Component c, boolean other, boolean is_begin) {
        int add = 0;
        if (c instanceof JComponent) {
            JComponent jc = (JComponent) c;
            Border border = jc.getBorder();
            if (border != null) {
                Insets insets = border.getBorderInsets(c);
                boolean is_x = (m_horizontal && !other) || (!m_horizontal && other);
                add = is_x ? (is_begin ? insets.left : insets.right) : (is_begin ? insets.top : insets.bottom);
            }
        }
        return add;
    }

    private int getAdd(Component c, boolean other) {
        int add = 0;
        if (c instanceof JComponent) {
            JComponent jc = (JComponent) c;
            Border border = jc.getBorder();
            if (border != null) {
                Insets insets = border.getBorderInsets(c);
                boolean is_width = (m_horizontal && !other) || (!m_horizontal && other);
                add = is_width ? insets.left + insets.right : insets.bottom + insets.top;
            }
        }
        return add;
    }

    private int getUsableSize(Component c) {
        return (m_horizontal ? c.getSize().width : c.getSize().height) - getAdd(c, false);
    }

    private int getUsableOtherSize(Component c) {
        return (m_horizontal ? c.getSize().height : c.getSize().width) - getAdd(c, true);
    }

    private int getSize(Component c) {
        return m_horizontal ? c.getSize().width : c.getSize().height;
    }

    private int getOtherSize(Component c) {
        return m_horizontal ? c.getSize().height : c.getSize().width;
    }

    /**
     * Sets the margin between the components.
     */
    public void setMargin(int m) {
        m_margin = m;
    }

    /**
     * Returns the String representation of this SnapGridLayout's values.
     */
    public String toString() {
        return getClass().getName() + "[m_margin=" + m_margin + ", m_gap=" + m_gap + "]";
    }

    /**
     * If true (which is the default) then the remaining (unused) space is displayed as an empty area at the
     * end of the list of components. If false, then all the gaps between the components are equally
     * sized to use up the remaining space.
     * @param pack
     */
    public void setPack(boolean pack) {
        m_pack = pack;
    }
}
