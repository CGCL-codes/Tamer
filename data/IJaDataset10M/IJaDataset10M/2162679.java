package com.usoog.hextd.wave;

import com.usoog.commons.gamecore.gamegrid.GridPoint;
import com.usoog.commons.gamecore.gamegrid.ScaledCanvas;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import com.usoog.hextd.core.GameGridImplementation;
import com.usoog.hextd.core.GameManagerImplementation;
import com.usoog.hextd.core.GameStateImplementation;
import com.usoog.hextd.core.HexTDPlayer;
import com.usoog.hextd.creep.HexTDCreep;
import com.usoog.hextd.creep.HexTDCreepData;
import com.usoog.hextd.hex.HexTile;
import com.usoog.hextd.tower.HexTDTower;
import com.usoog.tdcore.pathfinder.TDPathfinder;

/**
 *
 * @author hylke
 */
public interface PathfinderWaypoints extends TDPathfinder<HexTDCreep, HexTDCreepData, HexTDTower, HexTile, HexTDPlayer, GameGridImplementation, GameManagerImplementation, GameStateImplementation> {

    public class Waypoint {

        public GridPoint gridPoint;

        public Point2D.Double gamePoint = new Point2D.Double();

        public Point2D.Double paintPoint = new Point2D.Double();

        /**
		 * Create a new waypoint at the specified grid location.
		 *
		 * @param col the column of the waypoint.
		 * @param row the row of the waypoint.
		 */
        public Waypoint(int col, int row) {
            gridPoint = new GridPoint(col, row);
        }
    }

    @Override
    public PathLocationWaypoint getStartingPosition();

    public int length();

    public Waypoint getWaypoint(int step);

    public GridPoint getWaypointTileCoords(int step);

    public Point2D.Double getWaypointGameCoords(int step);

    @Override
    public void paint(Graphics2D g2, int gameTime);

    @Override
    public void resetScale(ScaledCanvas sc);

    public void finalise();

    public boolean isSelected();

    public void setSelected(boolean selected);

    @Override
    public double getBaseDelay();
}
