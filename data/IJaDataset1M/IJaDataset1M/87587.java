package org.lobobrowser.util.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

/**
 * @author J. H. S.
 */
public class CenterLayout implements LayoutManager {

    public void addLayoutComponent(String arg0, Component arg1) {
    }

    public void removeLayoutComponent(Component arg0) {
    }

    public Dimension preferredLayoutSize(Container arg0) {
        java.awt.Insets insets = arg0.getInsets();
        int count = arg0.getComponentCount();
        if (count > 0) {
            Dimension d = arg0.getComponent(0).getPreferredSize();
            return new Dimension(d.width + insets.left + insets.right, d.height + insets.top + insets.bottom);
        } else {
            return new Dimension(insets.left + insets.right, insets.top + insets.bottom);
        }
    }

    public Dimension minimumLayoutSize(Container arg0) {
        java.awt.Insets insets = arg0.getInsets();
        int count = arg0.getComponentCount();
        if (count > 0) {
            Dimension d = arg0.getComponent(0).getMinimumSize();
            return new Dimension(d.width + insets.left + insets.right, d.height + insets.top + insets.bottom);
        } else {
            return new Dimension(insets.left + insets.right, insets.top + insets.bottom);
        }
    }

    public void layoutContainer(Container container) {
        int count = container.getComponentCount();
        if (count > 0) {
            Component child = container.getComponent(0);
            java.awt.Insets insets = container.getInsets();
            int availWidth = container.getWidth() - insets.left - insets.right;
            int availHeight = container.getHeight() - insets.top - insets.bottom;
            Dimension preferredSize = child.getPreferredSize();
            double preferredWidth = preferredSize.getWidth();
            double preferredHeight = preferredSize.getHeight();
            int width;
            int height;
            int x;
            int y;
            if (preferredWidth < availWidth) {
                x = (int) Math.round(insets.left + (availWidth - preferredWidth) / 2);
                width = (int) Math.round(preferredWidth);
            } else {
                x = insets.left;
                width = availWidth;
            }
            if (preferredHeight < availHeight) {
                y = (int) Math.round(insets.top + (availHeight - preferredHeight) / 2);
                height = (int) Math.round(preferredHeight);
            } else {
                y = insets.top;
                height = availHeight;
            }
            child.setBounds(x, y, width, height);
        }
    }

    private static CenterLayout instance = new CenterLayout();

    public static CenterLayout getInstance() {
        return instance;
    }
}
