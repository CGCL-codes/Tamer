package nl.huub.van.amelsvoort.bsp;

import java.awt.Color;
import java.awt.Graphics2D;

public class SolidPolygonRenderer extends PolygonRenderer {

    public SolidPolygonRenderer(Transform3D camera, ViewWindow viewWindow) {
        this(camera, viewWindow, true);
    }

    public SolidPolygonRenderer(Transform3D camera, ViewWindow viewWindow, boolean clearViewEveryFrame) {
        super(camera, viewWindow, clearViewEveryFrame);
    }

    /**
   * Draws the current polygon. At this point, the current polygon is
   * transformed, clipped, projected, scan-converted, and visible.
   */
    public void drawCurrentPolygon(Graphics2D g) {
        if (sourcePolygon instanceof SolidPolygon3D) {
            g.setColor(((SolidPolygon3D) sourcePolygon).getColor());
        } else {
            g.setColor(Color.GREEN);
        }
        int y = scanConverter.getTopBoundary();
        while (y <= scanConverter.getBottomBoundary()) {
            ScanConverter.Scan scan = scanConverter.getScan(y);
            if (scan.isValid()) {
                g.drawLine(scan.left, y, scan.right, y);
            }
            y++;
        }
    }
}
