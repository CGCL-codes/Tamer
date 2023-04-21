package org.modss.facilitator.ui.result;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * PolarGraphLayout is a simple layout to centre the PolarGraph inside
 * a panel and keep it square.
 *
 * @author John Farrell
 */
public final class PolarGraphLayout implements LayoutManager {

    private int margin = 0;

    public PolarGraphLayout(int margin) {
        this.margin = margin;
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

    public void layoutContainer(Container parent) {
        Component[] cs = parent.getComponents();
        Component c = cs[0];
        Insets insets = null;
        Border border = null;
        if (parent instanceof JComponent) border = ((JComponent) parent).getBorder();
        if (border == null) {
            insets = new Insets(0, 0, 0, 0);
        } else {
            insets = border.getBorderInsets(parent);
        }
        Rectangle bounds = parent.getBounds();
        int width = bounds.width - insets.left - insets.right - margin - margin;
        int height = bounds.height - insets.top - insets.bottom - margin - margin;
        int side = (width < height) ? width : height;
        int x = (bounds.width - side) / 2;
        int y = (bounds.height - side) / 2;
        c.setBounds(x, y, side, side);
    }
}
