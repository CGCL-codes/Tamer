package org.flexgen.map.test.support;

import java.util.LinkedList;
import java.util.List;
import org.junit.Assert;
import org.flexgen.map.BeforeMapTileAddedListener;
import org.flexgen.map.MapGenerator;
import org.flexgen.map.MapTileLocation;

/**
 * Test class implementing the BeforeMapTileAddedListener interface.
 */
public class TestBeforeMapTileAddedListener implements BeforeMapTileAddedListener {

    /**
     * The list of map tile locations that have been added.
     */
    private List<MapTileLocation> mapTileLocations;

    /**
     * The list of map generators that have added map tiles.
     */
    private List<MapGenerator> mapGenerators;

    /**
     * Construct a test "before map tile added" listener.
     */
    public TestBeforeMapTileAddedListener() {
        mapTileLocations = new LinkedList<MapTileLocation>();
        mapGenerators = new LinkedList<MapGenerator>();
    }

    /**
     * Informs the listener that a map tile will be added at the specified location.
     *
     * @param mapGenerator
     *            Map generator that will add the map tile.
     * @param mapTileLocation
     *            Location at which the map tile will be added.
     */
    public void beforeMapTileAdded(MapGenerator mapGenerator, MapTileLocation mapTileLocation) {
        Assert.assertNull("Expected map tile to be null.", mapGenerator.getMapTile(mapTileLocation));
        mapTileLocations.add(mapTileLocation);
        mapGenerators.add(mapGenerator);
    }

    /**
     * Get the list of map tile locations that have been added.
     *
     * @return The list of map tile locations that have been added.
     */
    public List<MapTileLocation> getMapTileLocations() {
        return mapTileLocations;
    }

    /**
     * Get the list of map generators that have added map tiles.
     *
     * @return The list of map generators that have added map tiles.
     */
    public List<MapGenerator> getMapGenerators() {
        return mapGenerators;
    }
}
