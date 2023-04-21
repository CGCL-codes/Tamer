package org.freelords.game.generator;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Stack;
import org.apache.log4j.Logger;
import org.freelords.main.configuration.FreelordsConfiguration;
import org.freelords.map.GameMap;
import org.freelords.map.MapTile;
import org.freelords.map.TileType;
import org.freelords.util.Pair;
import org.freelords.util.Rand;
import org.freelords.util.Triplet;
import org.freelords.util.geom.Direction;
import org.freelords.util.geom.Point;

class RandomMap implements GameMap {

    /** Width of the map */
    private int width;

    /** Height of the map */
    private int height;

    /** List of map tiles */
    private MapTile[] map;

    /** Creates a new random map from a given terrain set.
	  *
	  * @param width the width of the map to be set up.
	  * @param height the height of the map to be set up.
	  * @param terrain the terrain types for the single tiles
	  */
    public RandomMap(int width, int height, TileType[][] terrain) {
        this.width = width;
        this.height = height;
        map = new MapTile[height * width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                map[x + y * width] = new MapTile(terrain[x][y], false);
            }
        }
    }

    /** Returns the height of the map. */
    public int getHeight() {
        return height;
    }

    /** Returns the width of the map. */
    public int getWidth() {
        return width;
    }

    /** Returns the maptile on position (x,y).
     *
     * Tiles out of bounds are returned as simple water tiles.
     */
    public MapTile getTile(int x, int y) {
        if (isoffmap(x, y)) {
            return new MapTile(TileType.NONE, false);
        }
        return map[x + y * width];
    }

    /** Returns true if the position (x, y) is outside the map boundaries. */
    private boolean isoffmap(int x, int y) {
        return ((y < 0) || (y >= height) || (x < 0) || (x >= width));
    }

    @Override
    public String toString() {
        String s = "";
        for (int y = 0; y < height; y++) {
            if (y % 2 == 1) {
                s += " ";
            }
            for (int x = 0; x < width; x++) {
                s = s + getTile(x, y).getTileType().getCharCode() + " ";
            }
            s = s + "\n";
        }
        return s;
    }
}
