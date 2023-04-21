package org.behrang.macbeans.tab.camino;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.Border;

/**
 *
 * @author behrangsa
 */
public class CaminoLowerTabControlBorder implements Border, CaminoColors {

    public Insets getBorderInsets(Component c) {
        return new Insets(0, 1, 1, 1);
    }

    public boolean isBorderOpaque() {
        return true;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Color old = g.getColor();
        g.setColor(UL);
        g.drawLine(x, y, x, height - 1);
        g.drawLine(width - 1, y, width - 1, height - 1);
        g.drawLine(x, height - 1, width - 1, height - 1);
        g.setColor(old);
    }
}
