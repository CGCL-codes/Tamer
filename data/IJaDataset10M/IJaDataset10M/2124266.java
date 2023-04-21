package net.sf.gridarta.gui.dialog.plugin;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

/**
 * A layoutManager which stacks components one on top of the other, regardless
 * of their size.
 * @author unknown
 */
public class StackLayout implements LayoutManager {

    /**
     * The vertical gap between components in pixels.
     */
    private final int vGap;

    /**
     * Create a StackLayout.
     * @param vGap vertical gap between components in pixels
     */
    public StackLayout(final int vGap) {
        this.vGap = vGap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addLayoutComponent(final String name, final Component comp) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension preferredLayoutSize(final Container parent) {
        final Insets insets = parent.getInsets();
        final int componentCount = parent.getComponentCount();
        int w = 0;
        int h = 0;
        for (int i = 0; i < componentCount; i++) {
            final Component comp = parent.getComponent(i);
            final Dimension d = comp.getPreferredSize();
            if (w < d.width) {
                w = d.width;
            }
            h += d.height;
            if (i != 0) {
                h += vGap;
            }
        }
        return new Dimension(insets.left + insets.right + w, insets.top + insets.bottom + h);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void layoutContainer(final Container parent) {
        final Insets insets = parent.getInsets();
        final int x = insets.left;
        int y = insets.top;
        final int w = preferredLayoutSize(parent).width;
        final int componentCount = parent.getComponentCount();
        for (int i = 0; i < componentCount; ++i) {
            final Component comp = parent.getComponent(i);
            final Dimension d = comp.getPreferredSize();
            comp.setBounds(x, y, w, d.height);
            y += d.height + vGap;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension minimumLayoutSize(final Container parent) {
        return preferredLayoutSize(parent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeLayoutComponent(final Component comp) {
    }
}
