package uk.ac.lkl.common.ui.jft;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * Hand-drawn circle highlight style.
 */
public class CircleHighlight extends Highlight {

    public CircleHighlight() {
        setStroke(Color.BLUE, 3.0f);
    }

    public void paintAround(Rectangle bounds, Graphics2D g) {
        g.setPaint(strokePaint);
        g.setStroke(stroke);
        int deltaRX, deltaRY;
        deltaRX = (int) (bounds.width * 0.2);
        deltaRY = (int) (bounds.height * 0.2);
        g.drawOval(bounds.x - deltaRX, bounds.y - deltaRY, bounds.width + 2 * deltaRX, bounds.height + 2 * deltaRY);
    }
}
