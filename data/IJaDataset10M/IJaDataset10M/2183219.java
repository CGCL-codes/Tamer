package com.rapidminer.gui.look.borders;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.UIResource;
import com.rapidminer.gui.look.RapidLookTools;

/**
 * The UIResource for empty button borders.
 *
 * @author Ingo Mierswa
 */
public class EmptyButtonBorder extends AbstractBorder implements UIResource {

    private static final long serialVersionUID = -8402839025192013938L;

    private static final Insets INSETS = new Insets(1, 5, 2, 5);

    private static final Insets TOOLBAR_INSETS = new Insets(5, 5, 5, 5);

    @Override
    public Insets getBorderInsets(Component c) {
        if (RapidLookTools.isToolbarButton((JComponent) c)) {
            return TOOLBAR_INSETS;
        } else {
            return INSETS;
        }
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        if (RapidLookTools.isToolbarButton((JComponent) c)) {
            insets.top = insets.left = insets.right = insets.bottom = 0;
            return TOOLBAR_INSETS;
        } else {
            insets.top = INSETS.top;
            insets.left = INSETS.left;
            insets.right = INSETS.right;
            insets.bottom = INSETS.bottom;
            return INSETS;
        }
    }
}
