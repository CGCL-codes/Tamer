package net.slashie.serf.ui.oryxUI;

import net.slashie.serf.ui.Appearance;
import net.slashie.utils.Position;

public class TiledLayer {

    private Appearance[][] tiles;

    private Appearance[][] tileBuffer;

    private Position position;

    private int layerIndex;

    private int cellWidth;

    private int cellHeight;

    private int superWidth;

    private int superHeight;

    private SwingSystemInterface si;

    public TiledLayer(int width, int height, int cellWidth, int cellHeight, int superWidth, int superHeight, Position position, int layerIndex, SwingSystemInterface si) {
        tiles = new Appearance[width][height];
        tileBuffer = new Appearance[width][height];
        this.position = position;
        this.layerIndex = layerIndex;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.superWidth = superWidth;
        this.superHeight = superHeight;
        this.si = si;
    }

    public void resetBuffer() {
        for (int y = 0; y < tiles[0].length; y++) {
            for (int x = 0; x < tiles.length; x++) {
                tileBuffer[x][y] = null;
            }
        }
    }

    public void setBuffer(int x, int y, Appearance appearance) {
        tileBuffer[x][y] = appearance;
    }

    public void updateBuffer() {
        System.arraycopy(tileBuffer, 0, tiles, 0, tileBuffer.length);
    }

    public void commit() {
        si.commitLayer(layerIndex);
    }

    public Position getPosition() {
        return position;
    }

    public int getSuperWidth() {
        return superWidth;
    }

    public int getSuperHeight() {
        return superHeight;
    }

    public Appearance[][] getTiles() {
        return tiles;
    }

    public int getLayerIndex() {
        return layerIndex;
    }

    public int getCellWidth() {
        return cellWidth;
    }

    public int getCellHeight() {
        return cellHeight;
    }

    public int getHeight() {
        return tiles[0].length;
    }

    public int getWidth() {
        return tiles.length;
    }
}
