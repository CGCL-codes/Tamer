package net.hanjava.svg;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.Icon;
import org.apache.batik.gvt.GraphicsNode;

/**
 * Swing icon for Batik graphics node.
 * @author behumble@hanjava.net
 */
public class GvtNodeIcon implements Icon {

    private GraphicsNode node;

    private int w = 100;

    private int h = 100;

    public GvtNodeIcon(GraphicsNode node) {
        this.node = node;
        Rectangle2D bounds = node.getBounds();
        w = (int) bounds.getWidth();
        h = (int) bounds.getHeight();
    }

    public int getIconHeight() {
        return h;
    }

    public int getIconWidth() {
        return w;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.translate(x, y);
        node.paint((Graphics2D) g);
        g.translate(-x, -y);
    }
}
