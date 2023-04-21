package au.gov.qld.dnr.dss.v1.ui.result;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

/** A layout manager which places polar graph nodes.
 **
 ** @author John Farrell
 **/
public final class PolarLayout implements LayoutManager {

    Viewport port;

    int diam = 0;

    LayoutQuadtree quadtree;

    void setViewport(Viewport port) {
        this.port = port;
    }

    void setDiameter(int diam) {
        this.diam = diam;
    }

    public void addLayoutComponent(String name, Component comp) {
    }

    public void removeLayoutComponent(Component comp) {
    }

    public Dimension preferredLayoutSize(Container parent) {
        return new Dimension(0, 0);
    }

    public Dimension minimumLayoutSize(Container parent) {
        return new Dimension(0, 0);
    }

    Rectangle getVirtualBounds(Rectangle bounds) {
        int width = (int) (1.0 / port.getSide() * bounds.width);
        int height = (int) (1.0 / port.getSide() * bounds.height);
        int ox = bounds.x + (int) (-port.x * width);
        int oy = bounds.y - (int) ((1.0 - port.y - port.height) * height);
        return new Rectangle(ox, oy, width, height);
    }

    int getVirtualX(Rectangle vb, double x) {
        return vb.x + (int) (x * vb.width);
    }

    int getVirtualY(Rectangle vb, double y) {
        return vb.y + (int) (vb.height * (1.0 - y));
    }

    double getRealX(Rectangle vb, int x) {
        return (double) (x - vb.x) / (double) vb.width;
    }

    double getRealY(Rectangle vb, int y) {
        return (double) (vb.y - y) / (double) vb.height + 1.0;
    }

    Rectangle getUseableBounds(Container parent) {
        Rectangle b = parent.getBounds();
        Insets is = null;
        Border border = null;
        if (parent instanceof JComponent) border = ((JComponent) parent).getBorder();
        if (border == null) {
            is = new Insets(0, 0, 0, 0);
        } else {
            is = border.getBorderInsets(parent);
        }
        int width = b.width - is.left - is.right - diam - diam;
        int height = b.height - is.top - is.bottom - diam - diam;
        return new Rectangle(is.left + diam, is.top + diam, width, height);
    }

    public void layoutContainer(Container parent) {
        Rectangle b = parent.getBounds();
        Rectangle ub = getUseableBounds(parent);
        Rectangle vb = getVirtualBounds(ub);
        Component[] cs = parent.getComponents();
        for (int i = 0; i < cs.length; i++) {
            if (cs[i] instanceof PolarNode) {
                PolarNode node = (PolarNode) cs[i];
                PolarAlternative alt = node.getAlternative();
                DoublePoint coords = alt.getCoords();
                if (!port.contains(coords)) {
                    node.setLocation(-100, -100);
                } else {
                    Dimension dim = node.getPreferredSize();
                    int x = getVirtualX(vb, coords.x) - dim.width / 2;
                    int y = getVirtualY(vb, coords.y) - dim.height / 2;
                    node.setBounds(x, y, dim.width, dim.height);
                }
            }
        }
    }
}
