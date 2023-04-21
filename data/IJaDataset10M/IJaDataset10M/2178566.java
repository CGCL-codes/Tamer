package eu.popeye.ui.laf;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.Border;

/**
 *
 * @author paolino
 */
public class RoundPopeyeBorder implements Border {

    private int arc = 24;

    private static final Color popeyeDarkGreen = new Color(0x7bbd42, false);

    private static final Color popeyeMediumGreen = new Color(0x8cce5a, false);

    private static final Color popeyeLightGreen = new Color(0xb8e691, false);

    /** RoundPopeyeBorder default constructor */
    public RoundPopeyeBorder() {
        super();
    }

    public boolean isBorderOpaque() {
        return false;
    }

    public Insets getBorderInsets(Component component) {
        return new Insets(arc, arc, arc, arc);
    }

    public void paintBorder(Component component, Graphics g, int x, int y, int w, int h) {
        g.setColor(popeyeDarkGreen);
        g.drawRoundRect(x, y, w - arc, h - arc, arc, arc);
    }
}
