package gpsmate.gui.utils;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;

/**
 * ExtendedFlowLayout
 * 
 * @author longdistancewalker
 * 
 * Taken from http://forums.sun.com/thread.jspa?threadID=780453
 ******************************************************
 * This LayoutManager alters java.awt.FlowLayout to solve a problem with
 * FlowLayout when FlowLayout needs to wrap the components. Specifically, the
 * preferred height of a FlowLayout is always the height of 1 row of components,
 * not the total height of all the rows, which sometimes results in only the
 * first row of components being visible.
 */
public class ExtendedFlowLayout extends FlowLayout {

    /**
   * Generated serialization UID.
   */
    private static final long serialVersionUID = -9220785736810327849L;

    public ExtendedFlowLayout() {
        super();
    }

    public ExtendedFlowLayout(int align) {
        super(align);
    }

    public ExtendedFlowLayout(int align, int hgap, int vgap) {
        super(align, hgap, vgap);
    }

    @Override
    public Dimension preferredLayoutSize(Container target) {
        return extendedPreferredSize(target);
    }

    @Override
    public Dimension minimumLayoutSize(Container target) {
        return extendedPreferredSize(target);
    }

    public Dimension extendedPreferredSize(Container target) {
        synchronized (target.getTreeLock()) {
            Insets insets = target.getInsets();
            int maxwidth = target.getWidth() - (insets.left + insets.right + getHgap() * 2);
            int nmembers = target.getComponentCount();
            int x = 0, y = insets.top + getVgap();
            int rowh = 0;
            for (int i = 0; i < nmembers; i++) {
                Component m = target.getComponent(i);
                if (m.isVisible()) {
                    Dimension d = m.getPreferredSize();
                    m.setSize(d.width, d.height);
                    if ((x == 0) || ((x + d.width) <= maxwidth)) {
                        if (x > 0) {
                            x += getHgap();
                        }
                        x += d.width;
                        rowh = Math.max(rowh, d.height);
                    } else {
                        x = d.width;
                        y += getVgap() + rowh;
                        rowh = d.height;
                    }
                }
            }
            return new Dimension(maxwidth, y + rowh + getVgap());
        }
    }
}
