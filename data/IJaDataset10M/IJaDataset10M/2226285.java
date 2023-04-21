package org.flexgen.map;

/**
 * Interface defining a mechanism for being notified of a map tile being added to a map.
 */
public interface MapTileAddedListener {

    /**
     * Informs the listener that a map tile has been added at the specified location.
     *
     * @param mapGenerator
     *            Map generator that added the map tile.
     * @param mapTileLocation
     *            Location at which the map tile was added.
     */
    void mapTileAdded(MapGenerator mapGenerator, MapTileLocation mapTileLocation);
}
