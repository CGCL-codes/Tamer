package net.buddat.wplanner.map.layer;

import net.buddat.wplanner.map.layer.tile.ObjectTile;

public class ObjectLayer extends DefaultLayer {

    private static final long serialVersionUID = -1146106570152320493L;

    private ObjectTile[][] layerTiles;

    public ObjectLayer(int x, int y) {
        tileX = x;
        tileY = y;
        layerTiles = new ObjectTile[tileX][tileY];
        for (int i = 0; i < tileX; i++) for (int j = 0; j < tileY; j++) layerTiles[i][j] = new ObjectTile();
    }

    @Override
    public ObjectTile getTile(int x, int y) {
        return layerTiles[x][y];
    }

    @Override
    public void resizeLayer(int newX, int newY, int xOff, int yOff) {
        ObjectTile[][] newTiles = new ObjectTile[newX][newY];
        if (xOff >= 0 && yOff >= 0) {
            for (int i = 0; i < tileX; i++) for (int j = 0; j < tileY; j++) if (xOff + i < newX && yOff + j < newY) newTiles[xOff + i][yOff + j] = layerTiles[i][j];
        } else {
            for (int i1 = (xOff < 0 ? 0 - xOff : 0); i1 < tileX; i1++) for (int j1 = (yOff < 0 ? 0 - yOff : 0); j1 < tileY; j1++) if (xOff + i1 < newX && yOff + j1 < newY) newTiles[i1 + xOff][j1 + yOff] = layerTiles[i1][j1];
        }
        for (int i = 0; i < newX; i++) for (int j = 0; j < newY; j++) if (newTiles[i][j] == null) newTiles[i][j] = new ObjectTile();
        tileX = newX;
        tileY = newY;
        layerTiles = newTiles;
    }
}
