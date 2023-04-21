package mrusanov.fantasyruler.game.background;

import java.awt.Point;

public class TerrainUnitCell {

    public final Point pixelLocation;

    public final CellState state;

    public final Point cellLocation;

    public TerrainUnitCell(Point location, Point cellLocation, CellState state) {
        pixelLocation = location;
        this.state = state;
        this.cellLocation = cellLocation;
    }
}
