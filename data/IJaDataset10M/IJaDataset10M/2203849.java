package org.skycastle.util.componentgraph.nodeview.nodes;

import org.skycastle.util.componentgraph.nodeview.AbstractViewNode;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * A node that renders a background panel.
 *
 * @author Hans Haggstrom
 */
public class PanelNode extends AbstractViewNode {

    private Rectangle2D myArea;

    private Color myColor;

    public PanelNode(final Rectangle2D area, final Color color) {
        myArea = area;
        myColor = color;
    }

    public void render(final Graphics2D g2) {
        g2.setColor(myColor);
        g2.fill(myArea);
    }

    public boolean containsPoint(final float x, final float y) {
        return myArea.contains(x, y);
    }
}
