package org.vikamine.gui.subgroup.visualization.stackedBars;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.util.Arrays;
import java.util.Iterator;
import javax.swing.JComponent;

/**
 * @author atzmueller
 */
public class StackedBarSetLayout implements LayoutManager {

    public double getScaleFactor(Container c) {
        int minSGSizePx = c.getFontMetrics(c.getFont()).stringWidth("X") * 5;
        double pixelsPerInstance = minSGSizePx / ((double) minSgSize);
        if (pixelsPerInstance == 0) {
            pixelsPerInstance = 1;
        }
        return pixelsPerInstance;
    }

    private int vgap = 50;

    private int hgap = 20;

    private int minSgSize;

    public StackedBarSetLayout(int minSgSize) {
        this.minSgSize = minSgSize;
    }

    public void removeLayoutComponent(Component comp) {
    }

    public void layoutContainer(Container parent) {
        Insets insets = parent.getInsets();
        int y = insets.top + vgap;
        int x = insets.left + hgap;
        int height = parent.getHeight() - insets.top - insets.bottom - vgap * 2;
        double scale = getScaleFactor(parent);
        for (Iterator iter = Arrays.asList(parent.getComponents()).iterator(); iter.hasNext(); ) {
            JComponent c = (JComponent) iter.next();
            ((StackedBarSGLayout) c.getLayout()).setScaleFactor(scale);
            c.setLocation(x, y);
            Dimension pref = getPreferredSize((StackedBarSGComponent) c, scale);
            pref.height = height;
            c.setSize(pref);
            x += pref.width + hgap;
        }
    }

    public void addLayoutComponent(String name, Component comp) {
    }

    public Dimension minimumLayoutSize(Container parent) {
        if (parent.getComponentCount() == 0) {
            return new Dimension(hgap, vgap);
        } else {
            int height = 0;
            int width = hgap;
            for (int i = 0; i < parent.getComponentCount(); i++) {
                Component c = parent.getComponent(i);
                Dimension d = c.getMinimumSize();
                height = Math.max(height, d.height);
                width += hgap + d.width;
            }
            height += vgap * 2;
            return new Dimension(width, height);
        }
    }

    public Dimension preferredLayoutSize(Container parent) {
        if (parent.getComponentCount() == 0) {
            return new Dimension(hgap, vgap);
        } else {
            int height = 0;
            int sgsWidth = hgap;
            int popWidth = -1;
            double scale = getScaleFactor(parent);
            for (int i = 0; i < parent.getComponentCount(); i++) {
                StackedBarSGComponent c = (StackedBarSGComponent) parent.getComponent(i);
                if (popWidth == -1) {
                    popWidth = (int) c.getSubgroup().getStatistics().getDefinedPopulationCount();
                }
                Dimension d = getPreferredSize(c, scale);
                height = Math.max(height, d.height);
                sgsWidth += hgap + d.width;
            }
            height += vgap * 2;
            int prefWidth = Math.max(popWidth, sgsWidth);
            Insets insets = parent.getInsets();
            prefWidth += insets.right + insets.left;
            height += insets.top + insets.bottom + 30;
            return new Dimension(prefWidth, height);
        }
    }

    private Dimension getPreferredSize(StackedBarSGComponent c, double scaleFactor) {
        Dimension pref = c.getPreferredSize();
        Dimension visPref = c.getVisualisationComponent().getPreferredSize();
        visPref.width *= scaleFactor;
        pref.width = Math.max(pref.width, visPref.width);
        return pref;
    }
}
