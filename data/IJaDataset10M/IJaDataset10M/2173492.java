package org.xith3d.terrain.legacy;

/**
 * TODO: Insert comments here
 * 
 * @author David Yazel
 */
public class TerrainCornerData {

    TerrainCornerData parent;

    TerrainSquareHandle square;

    int childIndex;

    int level;

    int xorg;

    int zorg;

    float y[];

    public TerrainCornerData() {
        y = new float[4];
        init();
    }

    public void init() {
        parent = null;
        square = null;
    }
}
