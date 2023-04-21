package net.rptools.maptool.client;

import net.rptools.maptool.client.ui.ZoneRenderer;
import net.rptools.maptool.model.Zone;

public class CellPoint extends AbstractPoint {

    public CellPoint(int x, int y) {
        super(x, y);
    }

    public String toString() {
        return "CellPoint" + super.toString();
    }

    /**
     * Find the screen cooridnates of the upper left hand corner of a cell taking
     * into acount scaling and translation. 
     * 
     * @param cell Get the coordinates of this cell.
     * @param screen The point used to contains the screen coordinates. It may
     * be <code>null</code>.
     * @return The screen coordinates of the upper left hand corner in the passed
     * point or in a new point.
     */
    public ScreenPoint convertToScreen(ZoneRenderer renderer) {
        double scale = renderer.getScale();
        Zone zone = renderer.getZone();
        int sx = renderer.getOffsetX() + (int) (zone.getGridOffsetX() * scale + x * zone.getGridSize() * scale);
        int sy = renderer.getOffsetY() + (int) (zone.getGridOffsetY() * scale + y * zone.getGridSize() * scale);
        return new ScreenPoint(sx, sy);
    }

    public ZonePoint convertToZone(ZoneRenderer renderer) {
        return new ZonePoint(x * renderer.getZone().getGridSize(), y * renderer.getZone().getGridSize());
    }
}
