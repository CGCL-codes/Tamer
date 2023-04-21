package ca.sqlpower.architect.swingui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class FormLayout implements LayoutManager {

    int hgap;

    int vgap;

    public FormLayout() {
        this(2, 2);
    }

    public FormLayout(int hgap, int vgap) {
        this.hgap = hgap;
        this.vgap = vgap;
    }

    public void addLayoutComponent(String name, Component comp) {
    }

    public void removeLayoutComponent(Component comp) {
    }

    private LeftRightHeight calcSizes(Container parent) {
        int maxLHWidth = 0;
        int maxRHWidth = 0;
        int lHeight = 0;
        int height = 0;
        Dimension d;
        for (int i = 0; i < parent.getComponentCount(); i++) {
            d = parent.getComponent(i).getPreferredSize();
            if (i % 2 == 0) {
                maxLHWidth = Math.max(maxLHWidth, d.width);
                lHeight = d.height;
            } else {
                maxRHWidth = Math.max(maxRHWidth, d.width);
                height += Math.max(lHeight, d.height);
            }
        }
        LeftRightHeight lrh = new LeftRightHeight();
        lrh.left = maxLHWidth;
        lrh.right = maxRHWidth;
        lrh.height = height;
        return lrh;
    }

    public Dimension preferredLayoutSize(Container parent) {
        Dimension minimum = minimumLayoutSize(parent);
        return minimum;
    }

    public Dimension minimumLayoutSize(Container parent) {
        LeftRightHeight lrh = calcSizes(parent);
        Insets i = parent.getInsets();
        int rows = parent.getComponentCount() / 2;
        return new Dimension(lrh.left + lrh.right + i.left + i.right + hgap, lrh.height + i.top + i.bottom + (vgap * (rows)));
    }

    /**
	 * Lays out the container as a 2-column form.  Labels go in the
	 * left column and input fields go in the right column.
	 *
	 * <p>The left column will always be as wide as its widest
	 * component (so that the labels always fit), and the right column
	 * will be as wide as leftover space permits.  Leftover space is
	 * defined as the parent container's width minus its left and
	 * right insets, this layout's hgap, and the left column width.
	 */
    public void layoutContainer(Container parent) {
        LeftRightHeight lrh = calcSizes(parent);
        Dimension size = parent.getSize();
        Insets ins = parent.getInsets();
        int lColWidth = Math.min(size.width, lrh.left);
        int rColWidth = size.width - ins.left - lColWidth - hgap - ins.right;
        Dimension d;
        int lHeight = 0;
        int y = ins.top;
        for (int i = 0; i < parent.getComponentCount(); i++) {
            Component c = parent.getComponent(i);
            d = c.getPreferredSize();
            if (i % 2 == 0) {
                c.setBounds(ins.left, y, lColWidth, d.height);
                lHeight = d.height;
            } else {
                int width = c.getPreferredSize().width;
                if (c instanceof JTextField || c instanceof JPanel || c instanceof JScrollPane) {
                    width = rColWidth;
                }
                c.setBounds(lColWidth + ins.left + hgap, y, width, d.height);
                y += Math.max(d.height, lHeight) + vgap;
            }
        }
    }

    /**
	 * A class to hold three numbers: the width of the left column,
	 * width of the right column, and the overall height of all the
	 * rows together.  The calcSizes() method returns an instance of
	 * this class.
	 */
    private class LeftRightHeight {

        int left;

        int right;

        int height;
    }
}
