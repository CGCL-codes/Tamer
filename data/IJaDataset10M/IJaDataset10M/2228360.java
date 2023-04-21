package org.jopenchart.marker;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import org.jopenchart.Chart;

public class CircleMarker extends ShapeMarker {

    @Override
    public void draw(Chart c, Graphics2D g) {
        int x = getX();
        int y = getY();
        g.setColor(getColor());
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.fillOval((int) (x - this.getSize() / 2), (int) (y - this.getSize() / 2), (int) this.getSize(), (int) this.getSize());
    }
}
