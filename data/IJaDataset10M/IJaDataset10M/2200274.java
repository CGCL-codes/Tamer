package net.sourceforge.g2destiny.ui.g2d.grid;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import net.sourceforge.g2destiny.model.grid.GridModel;
import net.sourceforge.g2destiny.ui.g2d.DMapping;
import org.apache.log4j.Logger;

/**
 * Draws a 2-dimensional grid.
 * 
 * @author Jeff D. Conrad
 * @since 02/14/2010
 */
public class DRectilinearGrid extends DGrid {

    public static final Logger log = Logger.getLogger(DRectilinearGrid.class);

    public static final String GRID_TYPE_RECTILINEAR = "rectilinear";

    public DRectilinearGrid(GridModel gridModel) {
        super(gridModel);
    }

    public void paintGrid(Graphics2D g2d, DMapping mapping) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        Rectangle2D.Double mapPort = mapping.getMapPort();
        Double doubleMajorsToX0 = mapPort.x / getGridModel().getMajorRadius();
        doubleMajorsToX0 = Math.floor(doubleMajorsToX0) - 1d;
        double firstMajorX = doubleMajorsToX0 * getGridModel().getMajorRadius();
        double lastMajorX = firstMajorX + mapPort.width + 2d * getGridModel().getMajorRadius();
        Double doubleMajorsToY0 = mapPort.y / getGridModel().getMajorRadius();
        doubleMajorsToY0 = Math.floor(doubleMajorsToY0) - 1d;
        double firstMajorY = doubleMajorsToY0 * getGridModel().getMajorRadius();
        double lastMajorY = firstMajorY + mapPort.height + 2d * getGridModel().getMajorRadius();
        double dx = firstMajorX;
        Point2D.Double dp1 = new Point2D.Double(dx, firstMajorY);
        Point2D.Double dp2 = new Point2D.Double(dx, lastMajorY);
        while (dx < lastMajorX) {
            dp1.x = dx;
            dp2.x = dx;
            g2d.setColor(getGridModel().getMinorColor());
            mapping.drawLine(g2d, dp1, dp2);
            dx += getGridModel().getMinorRadius();
        }
        dx = firstMajorX;
        dp1 = new Point2D.Double(dx, firstMajorY);
        dp2 = new Point2D.Double(dx, lastMajorY);
        while (dx < lastMajorX) {
            dp1.x = dx;
            dp2.x = dx;
            g2d.setColor(getGridModel().getMajorColor());
            mapping.drawLine(g2d, dp1, dp2);
            dx += getGridModel().getMajorRadius();
        }
        double dy = firstMajorY;
        dp1 = new Point2D.Double(firstMajorX, dy);
        dp2 = new Point2D.Double(lastMajorX, dy);
        while (dy < lastMajorY) {
            dp1.y = dy;
            dp2.y = dy;
            g2d.setColor(getGridModel().getMinorColor());
            mapping.drawLine(g2d, dp1, dp2);
            dy += getGridModel().getMinorRadius();
        }
        dy = firstMajorY;
        dp1 = new Point2D.Double(firstMajorX, dy);
        dp2 = new Point2D.Double(lastMajorX, dy);
        while (dy < lastMajorY) {
            dp1.y = dy;
            dp2.y = dy;
            g2d.setColor(getGridModel().getMajorColor());
            mapping.drawLine(g2d, dp1, dp2);
            dy += getGridModel().getMajorRadius();
        }
        g2d.setColor(getGridModel().getAxisColor());
        dp1 = new Point2D.Double(firstMajorX, 0);
        dp2 = new Point2D.Double(lastMajorX, 0);
        mapping.drawLine(g2d, dp1, dp2);
        dp1 = new Point2D.Double(0, firstMajorY);
        dp2 = new Point2D.Double(0, lastMajorY);
        mapping.drawLine(g2d, dp1, dp2);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    @Override
    public String getType() {
        return GRID_TYPE_RECTILINEAR;
    }
}
