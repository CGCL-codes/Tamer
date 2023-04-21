package com.mapmidlet.tile.provider;

/**
 * Abstract class for tile factories.
 * 
 * @author Damian Waradzyn
 */
public abstract class AbstractTileFactory {

    public abstract int getTileSize();

    public abstract Tile createTile(Tile referenceTile, int horizontalShift, int verticalShift);

    public abstract Tile createTile(int zoom, double latitude, double longtitude);

    public abstract String getDirectoryName();

    public int getMaxZoom() {
        return 17;
    }
}
