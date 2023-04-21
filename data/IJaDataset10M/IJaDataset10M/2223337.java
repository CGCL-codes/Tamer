package de.miethxml.toolkit.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.CubicCurve2D;
import javax.swing.border.Border;

/**
 * @author <a href="mailto:simon.mieth@gmx.de">Simon Mieth </a>
 *
 */
public class SplineBorder implements Border {

    private Insets i = new Insets(0, 40, 0, 0);

    /**
     *
     */
    public SplineBorder() {
        super();
    }

    public boolean isBorderOpaque() {
        return true;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        CubicCurve2D.Float spline = new CubicCurve2D.Float(x, y + height, x + (i.left / 2) + 2, y + height, x + (i.left / 2), y, x + i.left, y - 1);
        g2.setColor(Color.WHITE);
        g2.draw(spline);
    }

    public Insets getBorderInsets(Component c) {
        return i;
    }
}
