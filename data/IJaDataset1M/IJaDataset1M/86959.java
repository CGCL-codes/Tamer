package org.behrang.macbeans.tab.coda;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.Border;

/**
 *
 * @author behrangsa
 */
public class CodaLowerEditorTabControlBorder implements Border {

    private static final Insets INSETS = new Insets(0, 1, 1, 1);

    private static final Color BORDER_COLOR = new Color(170, 170, 170);

    public Insets getBorderInsets(Component c) {
        return INSETS;
    }

    public boolean isBorderOpaque() {
        return true;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.setColor(BORDER_COLOR);
        g.drawLine(x, y, x, y + height);
        g.drawLine(x, y + height - 1, x + width - 1, y + height - 1);
        g.drawLine(x + width - 1, y, x + width - 1, y + height);
    }
}
