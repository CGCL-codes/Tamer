package org.omg.tacsit.common.ui.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;

/**
 * A layout which stacks components veritcally, and resizes them horizontally to take up the maximum possible width.
 * The vertical size a component takes up is based on its preferred size.  This layout is particularly useful for
 * creating a vertical "Stack" of related buttons next to a JTable, JList, or JTree.
 * <p>
 * This layout differs from BoxLayout in two ways:
 * <li>It allows components to have a consistent vertical spacing.</li>
 * <li>The components always take up the maximum available horizontal space.
 * <p>
 * This layout differs from GridLayout in two ways:<br>
 * <li>It does not resize the height of the components.</li>
 * <li>The layout may not take up all of the available vertical space allocated to it.
 * 
 * @see StackedLayout
 * @author Matthew Child
 */
public class FatStackedLayout extends StackedLayout {

    /**
   * Creates a new instance.
   */
    public FatStackedLayout() {
        this(0);
    }

    /**
   * Creates a new instance.
   * @param vgap The vertical gap between components in the layout.
   */
    public FatStackedLayout(int vgap) {
        super(vgap);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        Dimension minSize = new Dimension(0, 0);
        int vgap = getVgap();
        boolean first = true;
        for (Component comp : parent.getComponents()) {
            Dimension prefSize = comp.getPreferredSize();
            if (prefSize.width > minSize.width) {
                minSize.width = prefSize.width;
            }
            minSize.height += prefSize.height;
            if (first) {
                first = false;
            } else {
                minSize.height += vgap;
            }
        }
        Insets insets = parent.getInsets();
        minSize.height += insets.top + insets.bottom;
        minSize.width += insets.left + insets.right;
        return minSize;
    }

    @Override
    public void layoutContainer(Container parent) {
        Dimension size = minimumLayoutSize(parent);
        Insets insets = parent.getInsets();
        int currHeight = insets.top;
        int insetWidth = insets.left + insets.right;
        int parentWidth = parent.getSize().width;
        int vgap = getVgap();
        boolean first = true;
        for (Component comp : parent.getComponents()) {
            Dimension compSize = comp.getPreferredSize();
            if (first) {
                first = false;
            } else {
                currHeight += vgap;
            }
            comp.setBounds(insets.left, currHeight, parentWidth - insetWidth, compSize.height);
            currHeight += compSize.height;
        }
    }
}
