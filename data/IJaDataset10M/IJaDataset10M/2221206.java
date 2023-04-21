package net.sf.amemailchecker.gui.component;

import javax.swing.*;
import java.awt.*;

public class ArrowIcon implements Icon {

    private boolean descending;

    private int size;

    private Color color;

    public ArrowIcon(boolean descending, int size) {
        this.descending = descending;
        this.size = size;
        color = UIManager.getColor("controlText");
    }

    public ArrowIcon(boolean descending, int size, Color color) {
        this.descending = descending;
        this.size = size;
        this.color = color;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        int dx = (size / 2);
        int dy = descending ? dx : -dx;
        y = y + 5 * size / 6 + (descending ? -dy : 0);
        Color previous = g.getColor();
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2.setComposite(AlphaComposite.SrcOver);
        g2.translate(x, y);
        g2.setColor(color);
        g2.fillPolygon(new int[] { dx / 2, dx, 0 }, new int[] { dy, 0, 0 }, 3);
        g2.translate(-x, -y);
        g.setColor(previous);
    }

    public int getIconWidth() {
        return size;
    }

    public int getIconHeight() {
        return size;
    }
}
