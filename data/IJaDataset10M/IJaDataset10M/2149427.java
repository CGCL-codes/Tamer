package org.aiotrade.charting.chart.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;

/** Line segment
 */
public class PathSegment extends AbstractSegment {

    private GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 2);

    public PathSegment() {
    }

    public PathSegment(Color color) {
        this.color = color;
    }

    /**
     * Do not define setPath() method to force use the member path that has been created
     * public void setPath(GeneralPath path) {
     * this.path = path;
     * }
     */
    public GeneralPath getPath() {
        return path;
    }

    public void render(Graphics g) {
        g.setColor(color);
        ((Graphics2D) g).draw(getPath());
    }
}
