package hu.openig.xold.core;

import hu.openig.core.Location;

/**
 * Interface for asking a Tile object from a building (or tile).
 * The tile returned my depend on the current state of the building, and the asked location.
 * @author karnokd
 */
public interface TileProvider {

    /**
	 * Returns a tile associated with the object based on the supplied location.
	 * @param location the location object
	 * @return the tile, null should indicate an unused/invisible tiles
	 */
    Tile getTile(Location location);

    /**
	 * Returns the status of the tile for the minimap rendering.
	 * @return the tile status, never null
	 */
    TileStatus getStatus();
}
