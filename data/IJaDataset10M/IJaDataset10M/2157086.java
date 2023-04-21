package org.openstreetmap.gui.griddirectory;

import org.openstreetmap.gui.jmapviewer.Tile;

/**
 * Implementing class serves the result of a SquareCovering.
 * <br/>
 * <b>Note:</b> All results are available if state is {@link Thread.State#TERMINATED}.
 * @author Stefan Zeller
 *
 */
public interface SquareCoveringResult {

    /**
	 * The tiles covering the SelectionSquare
	 * @return
	 */
    public Tile[][] getTiles();

    /**
	 * Detailed info about the top left corner of the selection square
	 * with respect to the covering.
	 * @return
	 */
    public TileAtCoordinate getInfoTopLeft();

    /**
	 * Detailed info about the bottom right corner of the selection square
	 * with respect to the covering.
	 * @return
	 */
    public TileAtCoordinate getInfoBottomRight();

    /**
	 * The zoom used for generating the covering
	 * @return
	 */
    public int getZoom();

    /**
	 * The current state of the worker.
	 * <br/>
	 * <b>Note:</b> All upper informations are just available if state is {@link Thread.State#TERMINATED}
	 * @return
	 */
    public Thread.State getState();
}
