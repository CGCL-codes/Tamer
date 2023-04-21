package com.rapidminer.gui.look.borders;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.UIResource;
import com.rapidminer.gui.look.RapidLookListCellRenderer;
import com.rapidminer.gui.look.RapidLookTools;

/**
 * The UIResource for combo box list cell renderer focus borders.
 *
 * @author Ingo Mierswa
 */
public class ComboBoxListCellRendererFocusBorder extends AbstractBorder implements UIResource {

    private static final long serialVersionUID = -7891513529402201022L;

    private Color selectionBackground = RapidLookTools.getColors().getTextHighlightBackColor();

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
        if (c instanceof RapidLookListCellRenderer) {
            this.selectionBackground = ((RapidLookListCellRenderer) c).getParentList().getSelectionBackground();
        }
        g.translate(x, y);
        g.drawLine(0, 0, 0, 0);
        g.drawLine(w - 1, 0, w - 1, 0);
        g.drawLine(0, h - 1, 0, h - 1);
        g.drawLine(w - 1, h - 1, w - 1, h - 1);
        g.setColor(this.selectionBackground.brighter());
        g.drawLine(1, 0, 2, 0);
        g.drawLine(w - 2, 0, w - 3, 0);
        g.drawLine(1, h - 1, 2, h - 1);
        g.drawLine(w - 2, h - 1, w - 3, h - 1);
        g.drawLine(0, 1, 0, 2);
        g.drawLine(0, h - 2, 0, h - 3);
        g.drawLine(w - 1, 1, w - 1, 2);
        g.drawLine(w - 1, h - 2, w - 1, h - 3);
        g.translate(-x, -y);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(1, 1, 1, 1);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = 1;
        insets.top = 1;
        insets.right = 1;
        insets.bottom = 1;
        return insets;
    }
}
