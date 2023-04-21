package org.flexgen.example;

import java.util.Collection;
import java.util.LinkedList;
import org.flexgen.map.MapTileLocation;
import org.flexgen.map.MapTileLocationFilter;

/**
 * Class implementing logic for returning a collection of map tile locations that slowly builds out
 * from the center of the map.
 */
public class GrowingMapTileLocationFilter implements MapTileLocationFilter {

    /**
     * Map tile location filter to use as a base for this filter.
     */
    private final MapTileLocationFilter mapTileLocationFilter;

    /**
     * Maximum distance from the center of the map for tiles.
     */
    private int maxDistance;

    /**
     * Construct a growing map tile location filter.
     *
     * @param mapTileLocationFilter
     *            Map tile location filter to use as a base for this filter.
     */
    public GrowingMapTileLocationFilter(MapTileLocationFilter mapTileLocationFilter) {
        this.mapTileLocationFilter = mapTileLocationFilter;
    }

    /**
     * Get the smallest possible X coordinate for allowed map tile locations.
     *
     * @return The smallest possible X coordinate for allowed map tile locations.
     */
    public int getMinX() {
        return mapTileLocationFilter.getMinX();
    }

    /**
     * Get the smallest possible Y coordinate for allowed map tile locations.
     *
     * @return The smallest possible Y coordinate for allowed map tile locations.
     */
    public int getMinY() {
        return mapTileLocationFilter.getMinY();
    }

    /**
     * Get the largest possible X coordinate for allowed map tile locations.
     *
     * @return The largest possible X coordinate for allowed map tile locations.
     */
    public int getMaxX() {
        return mapTileLocationFilter.getMaxX();
    }

    /**
     * Get the largest possible Y coordinate for allowed map tile locations.
     *
     * @return The largest possible Y coordinate for allowed map tile locations.
     */
    public int getMaxY() {
        return mapTileLocationFilter.getMaxY();
    }

    /**
     * Get a filtered collection of map tile locations.
     *
     * @param mapTileLocations
     *            Collection of map tile locations to filter.
     *
     * @return A filtered collection of map tile locations.
     */
    public Collection<MapTileLocation> getFilteredMapTileLocations(Collection<MapTileLocation> mapTileLocations) {
        Collection<MapTileLocation> rawMapTileLocations = mapTileLocationFilter.getFilteredMapTileLocations(mapTileLocations);
        Collection<MapTileLocation> localMapTileLocations = new LinkedList<MapTileLocation>();
        if (!rawMapTileLocations.isEmpty()) {
            while (localMapTileLocations.isEmpty()) {
                for (MapTileLocation mapTileLocation : rawMapTileLocations) {
                    int distance = Math.max(Math.abs(mapTileLocation.getX()), Math.abs(mapTileLocation.getY()));
                    if (distance <= maxDistance) {
                        localMapTileLocations.add(mapTileLocation);
                    }
                }
                if (localMapTileLocations.isEmpty()) {
                    maxDistance++;
                }
            }
        }
        return localMapTileLocations;
    }
}
