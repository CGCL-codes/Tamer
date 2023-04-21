package net.sourceforge.jute.draw;

import java.awt.Graphics;

/**
 *
 * @author david.mcnerney
 */
public class LineSegment extends GraphicObject {

    public LineSegment(int w, int h) {
        bounds.width = w;
        bounds.height = h;
    }

    @Override
    protected void drawObject(Graphics g) {
        g.drawLine(0, 0, bounds.width, bounds.height);
    }
}
