package org.freelords.map;

/** The most rudimentary possible Map implementation; Build the whole
  * map out of water.
  *
  * This is not really used and might be removed eventually. Its sole
  * purpose is to provide a dummy for the map drawing instance before the
  * actual map is set.
  */
public class GameMapPlain implements GameMap {

    public static final GameMap INSTANCE = new GameMapPlain();

    private GameMapPlain() {
    }

    public int getWidth() {
        return 10;
    }

    public int getHeight() {
        return 10;
    }

    public MapTile getTile(int x, int y) {
        return MapTile.getSimpleMapTile(TileType.WATER);
    }

    public void replaceTileType(int x, int y, TileType t) {
    }

    ;
}
