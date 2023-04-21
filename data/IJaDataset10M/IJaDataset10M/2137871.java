package org.vikamine.swing.subgroup.visualization.box;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import javax.swing.JComponent;

/**
 * @author Tobias Vogele
 */
public class LabelPanelLayout implements LayoutManager {

    public static final String TARGET_SHARE_KEY = "targetShare";

    public void removeLayoutComponent(Component comp) {
    }

    public void layoutContainer(Container parent) {
        Insets insets = parent.getInsets();
        int width = parent.getWidth() - insets.left - insets.right;
        for (int i = 0; i < parent.getComponentCount(); i++) {
            Component c = parent.getComponent(i);
            if (c instanceof JComponent) {
                JComponent jc = (JComponent) c;
                Object o = jc.getClientProperty(TARGET_SHARE_KEY);
                if (o instanceof Double) {
                    double tpr = ((Double) o).doubleValue();
                    int center = (int) (tpr * width);
                    Dimension pref = c.getPreferredSize();
                    int x = insets.left + center - pref.width / 2;
                    c.setLocation(x, insets.top);
                    c.setSize(pref);
                }
            }
        }
    }

    public void addLayoutComponent(String name, Component comp) {
    }

    public Dimension minimumLayoutSize(Container parent) {
        int height = 0;
        for (int i = 0; i < parent.getComponentCount(); i++) {
            height = Math.max(height, parent.getComponent(i).getMinimumSize().height);
        }
        Insets insets = parent.getInsets();
        return new Dimension(1 + insets.left + insets.right, height + insets.top + insets.bottom);
    }

    public Dimension preferredLayoutSize(Container parent) {
        int height = 0;
        for (int i = 0; i < parent.getComponentCount(); i++) {
            height = Math.max(height, parent.getComponent(i).getPreferredSize().height);
        }
        Insets insets = parent.getInsets();
        return new Dimension(1 + insets.left + insets.right, height + insets.top + insets.bottom);
    }
}
