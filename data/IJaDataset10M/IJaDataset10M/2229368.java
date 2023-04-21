package org.formaria.awt;

import java.util.Enumeration;
import java.util.Vector;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;

/**
 * <p>An extended hotspot support the drawing of a polygon around the 'hot' area</p>
 * <p>Copyright (c) Formaria Ltd., <br>
 * License:      see license.txt
 * $Revision: 2.3 $
 */
public class ImageMap extends HotspotImage {

    /**
   * The points/vertices marked on the image
   */
    protected Vector linePoints;

    /**
   * true to drawa highlight on the hotspots
   */
    protected boolean drawHotspots;

    /**
   * Create a new image map
   */
    public ImageMap() {
        linePoints = new Vector();
        drawHotspots = false;
    }

    /**
   * Add a new point 
   * @param pt the new point
   */
    public void addPoint(Point pt) {
        linePoints.addElement(pt);
    }

    /**
   * Add a new point 
   * @param x the new point's x coordinate
   * @param y the new point's y coordinate
   */
    public void addPoint(int x, int y) {
        linePoints.addElement(new Point(x, y));
    }

    /**
   * Render the component
   * @param g the graphics context
   */
    public void paint(Graphics g) {
        super.paint(g);
        Enumeration e = linePoints.elements();
        g.setColor(Color.red);
        Point startPt = null;
        if (e.hasMoreElements()) startPt = (Point) e.nextElement();
        while (e.hasMoreElements()) {
            Point endPt = (Point) e.nextElement();
            g.drawLine(startPt.x, startPt.y, endPt.x, endPt.y);
            g.drawLine(startPt.x - 1, startPt.y - 1, endPt.x - 1, endPt.y - 1);
            g.drawLine(startPt.x + 1, startPt.y + 1, endPt.x + 1, endPt.y + 1);
            g.drawLine(startPt.x + 1, startPt.y - 1, endPt.x + 1, endPt.y - 1);
            g.drawLine(startPt.x - 1, startPt.y + 1, endPt.x - 1, endPt.y + 1);
            startPt = endPt;
        }
        if (drawHotspots) {
            int numHotspots = hotspots.size();
            g.setColor(Color.darkGray);
            for (int i = 0; i < numHotspots; i++) {
                Polygon poly = (Polygon) hotspots.elementAt(i);
                g.drawPolygon(poly);
            }
        }
    }

    /**
   * Set the flag controlling rendering of the hotspots. When turned on a line
   * is drawn to show the edges of the hotspots
   * @param draw true to render the hotspots
   */
    public void setDrawHotspots(boolean draw) {
        drawHotspots = draw;
    }

    /**
   * Get the flag controlling rendering of the hotspots.
   * @return true if the hotspots are rendered
   */
    public boolean getDrawHotspots() {
        return drawHotspots;
    }
}
