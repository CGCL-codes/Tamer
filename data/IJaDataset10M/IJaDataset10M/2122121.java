package com.hardwire.softphysics;

import javax.microedition.lcdui.Graphics;
import com.hardwire.utils.MathUtils;

/**
 * Implicit grid takes care of the broadphase. It divides space into uniform sized cells, but doesn't store them explicitely in an array. Instead it stores what is in a specific row/column and reproduces actuall cell data on the fly.
 */
public class ImplicitGrid {

    private static final int TILE_SIZE_SHIFT = com.hardwire.blob.Game.TILE_SIZE_SHIFT + MathUtils.SHIFT_COUNT;

    private int width, height;

    private int numChunksPerCell;

    private int maxObjects;

    private static final int CHUNK_SIZE_SHIFT = 6;

    private long[] cols, rows;

    private int[] neighbours, visibles;

    private int[][] areasBounds;

    /**
   * Allocates memory according to world and cell sizes.
   * 
   * @param pWorldWidth Width of the world.
   * @param pWorldHeight Height of the world.
   * @param pMaxObjects Maximum number of objects present in this grid at one time.
   */
    public ImplicitGrid(int pWorldWidth, int pWorldHeight, int pMaxObjects) {
        width = pWorldWidth >> TILE_SIZE_SHIFT;
        height = pWorldHeight >> TILE_SIZE_SHIFT;
        if (pWorldWidth % (1 << TILE_SIZE_SHIFT) != 0) width++;
        if (pWorldHeight % (1 << TILE_SIZE_SHIFT) != 0) height++;
        numChunksPerCell = (pMaxObjects >> CHUNK_SIZE_SHIFT) + 1;
        maxObjects = numChunksPerCell << CHUNK_SIZE_SHIFT;
        cols = new long[width * numChunksPerCell];
        rows = new long[height * numChunksPerCell];
        neighbours = new int[maxObjects + 1];
        visibles = new int[maxObjects + 1];
    }

    private void resize(int pID) {
        int newChunksPreCell = (pID >> CHUNK_SIZE_SHIFT) + 1;
        long[] newCols = new long[newChunksPreCell * width];
        long[] newRows = new long[newChunksPreCell * height];
        for (int i = 0, ox = 0, nx = 0; i < width; i++, ox += numChunksPerCell, nx += newChunksPreCell) System.arraycopy(cols, ox, newCols, nx, numChunksPerCell);
        for (int i = 0, ox = 0, nx = 0; i < height; i++, ox += numChunksPerCell, nx += newChunksPreCell) System.arraycopy(rows, ox, newRows, nx, numChunksPerCell);
        cols = null;
        rows = null;
        neighbours = null;
        visibles = null;
        cols = newCols;
        rows = newRows;
        numChunksPerCell = newChunksPreCell;
        maxObjects = numChunksPerCell << CHUNK_SIZE_SHIFT;
        neighbours = new int[maxObjects + 1];
        visibles = new int[maxObjects + 1];
    }

    /**
   * Query an area for the presence of objects saved in implicit grid.
   * 
   * @param pMyID ID of object which should be excluded from the query. Pass -1 if you don't want any excluded.
   * @param pNumObjects Maximum ID of searched objects. This is needed to speed things up. 
   * @param pArea Area (in absolute units) which should be searched. Defined by a minimum and maximum point the same way like an AABB.
   * @return An array of IDs of objects found. In the zero index in the array there is a number of objects found.
   */
    public int[] queryArea(int pMyID, int pNumObjects, int[] pArea) {
        if (pNumObjects == 0 || numChunksPerCell == 0) {
            neighbours[0] = 0;
            return neighbours;
        }
        int minX = pArea[0] >> TILE_SIZE_SHIFT;
        int minY = pArea[1] >> TILE_SIZE_SHIFT;
        int maxX = pArea[2] >> TILE_SIZE_SHIFT;
        int maxY = pArea[3] >> TILE_SIZE_SHIFT;
        minX = minX < 0 ? 0 : minX;
        minY = minY < 0 ? 0 : minY;
        maxX = maxX >= width ? width - 1 : maxX;
        maxY = maxY >= height ? height - 1 : maxY;
        minX *= numChunksPerCell;
        minY *= numChunksPerCell;
        maxX *= numChunksPerCell;
        maxY *= numChunksPerCell;
        int neighboursLen = 0;
        long chunk, chunkCols;
        int myIDChunk = pMyID >> CHUNK_SIZE_SHIFT;
        int myIDRelative = pMyID == -1 ? -1 : pMyID - (myIDChunk << CHUNK_SIZE_SHIFT);
        long myIDRelativeMask = pMyID == -1 ? 0L : myIDRelative == 63 ? ~0L : ((1L << myIDRelative + 1) - 1L);
        for (int j = pMyID == -1 ? 0 : myIDChunk; j != numChunksPerCell; j++) {
            chunkCols = 0;
            chunk = 0;
            for (int x = minX; x <= maxX; x += numChunksPerCell) chunkCols |= cols[x + j];
            for (int y = minY; y <= maxY; y += numChunksPerCell) chunk |= rows[y + j];
            chunk &= chunkCols;
            while (chunk != 0) {
                long bitmask = chunk & -chunk;
                if ((bitmask & myIDRelativeMask) == 0 || j != myIDChunk) {
                    int shift = 0;
                    if ((bitmask & (4294967295L << shift)) == 0) shift += 32;
                    if ((bitmask & (65535L << shift)) == 0) shift += 16;
                    if ((bitmask & (255L << shift)) == 0) shift += 8;
                    if ((bitmask & (15L << shift)) == 0) shift += 4;
                    if ((bitmask & (3L << shift)) == 0) shift += 2;
                    if ((bitmask & (1L << shift)) == 0) shift += 1;
                    neighbours[++neighboursLen] = shift + (j << CHUNK_SIZE_SHIFT);
                }
                chunk ^= bitmask;
            }
        }
        neighbours[0] = neighboursLen;
        return neighbours;
    }

    /**
   * Query multiple (possible overlapping) areas for the presence of objects saved in implicit grid.
   * 
   * @param pMyID ID of object which should be excluded from the query. Pass -1 if you don't want any excluded.
   * @param pNumObjects Maximum ID of searched objects. This is needed to speed things up. 
   * @param pArea Areas (in absolute units) which should be searched.
   * @return An array of IDs of objects found. In the zero index in the array there is a number of objects found.
   */
    public int[] queryAreas(int pMyID, int pNumObjects, int[][] pArea) {
        if (pNumObjects == 0 || numChunksPerCell == 0) {
            visibles[0] = 0;
            return visibles;
        }
        int numAreas = pArea.length;
        if (areasBounds == null || numAreas != areasBounds.length) {
            areasBounds = null;
            areasBounds = new int[numAreas][4];
        }
        for (int i = 0; i < numAreas; i++) {
            int[] bounds = areasBounds[i];
            bounds[0] = pArea[i][0] >> TILE_SIZE_SHIFT;
            bounds[1] = pArea[i][1] >> TILE_SIZE_SHIFT;
            bounds[2] = pArea[i][2] >> TILE_SIZE_SHIFT;
            bounds[3] = pArea[i][3] >> TILE_SIZE_SHIFT;
            bounds[0] = bounds[0] < 0 ? 0 : bounds[0];
            bounds[1] = bounds[1] < 0 ? 0 : bounds[1];
            bounds[2] = bounds[2] >= width ? width - 1 : bounds[2];
            bounds[3] = bounds[3] >= height ? height - 1 : bounds[3];
            bounds[0] *= numChunksPerCell;
            bounds[1] *= numChunksPerCell;
            bounds[2] *= numChunksPerCell;
            bounds[3] *= numChunksPerCell;
        }
        int visiblesLen = 0;
        long areaChunk, chunk, chunkCols;
        int myIDChunk = pMyID >> CHUNK_SIZE_SHIFT;
        int myIDRelative = pMyID == -1 ? -1 : pMyID - (myIDChunk << CHUNK_SIZE_SHIFT);
        long myIDRelativeMask = pMyID == -1 ? 0L : myIDRelative == 63 ? ~0L : ((1L << myIDRelative + 1) - 1L);
        for (int j = pMyID == -1 ? 0 : myIDChunk; j != numChunksPerCell; j++) {
            chunk = 0;
            for (int l = 0; l < numAreas; l++) {
                chunkCols = 0;
                areaChunk = 0;
                int max = areasBounds[l][2];
                for (int x = areasBounds[l][0]; x <= max; x += numChunksPerCell) chunkCols |= cols[x + j];
                max = areasBounds[l][3];
                for (int y = areasBounds[l][1]; y <= max; y += numChunksPerCell) areaChunk |= rows[y + j];
                chunk |= areaChunk & chunkCols;
            }
            while (chunk != 0) {
                long bitmask = chunk & -chunk;
                if ((bitmask & myIDRelativeMask) == 0 || j != myIDChunk) {
                    int shift = 0;
                    if ((bitmask & (4294967295L << shift)) == 0) shift += 32;
                    if ((bitmask & (65535L << shift)) == 0) shift += 16;
                    if ((bitmask & (255L << shift)) == 0) shift += 8;
                    if ((bitmask & (15L << shift)) == 0) shift += 4;
                    if ((bitmask & (3L << shift)) == 0) shift += 2;
                    if ((bitmask & (1L << shift)) == 0) shift += 1;
                    visibles[++visiblesLen] = shift + (j << CHUNK_SIZE_SHIFT);
                }
                chunk ^= bitmask;
            }
        }
        visibles[0] = visiblesLen;
        return visibles;
    }

    /**
   * Remove all objects from the implicit grid.
   */
    public void clear() {
        for (int i = 0; i < cols.length; i++) cols[i] = 0;
        for (int i = 0; i < rows.length; i++) rows[i] = 0;
    }

    /**
   * Update the influence of an object.
   * 
   * @param pID ID of the object.
   * @param minX1 X coord of the old AABB of the object.
   * @param minY1 Y coord of the old AABB of the object.
   * @param maxX1 X coord of the old AABB of the object.
   * @param maxY1 Y coord of the old AABB of the object.
   * @param minX2 X coord of the new AABB of the object.
   * @param minY2 Y coord of the new AABB of the object.
   * @param maxX2 X coord of the new AABB of the object.
   * @param maxY2 Y coord of the new AABB of the object.
   */
    public void updateObject(int pID, int minX1, int minY1, int maxX1, int maxY1, int minX2, int minY2, int maxX2, int maxY2) {
        minX1 >>= TILE_SIZE_SHIFT;
        minY1 >>= TILE_SIZE_SHIFT;
        maxX1 >>= TILE_SIZE_SHIFT;
        maxY1 >>= TILE_SIZE_SHIFT;
        minX2 >>= TILE_SIZE_SHIFT;
        minY2 >>= TILE_SIZE_SHIFT;
        maxX2 >>= TILE_SIZE_SHIFT;
        maxY2 >>= TILE_SIZE_SHIFT;
        if (minX1 != minX2 || minY1 != minY2 || maxX1 != maxX2 || maxY1 != maxY2) {
            unsetObject(pID, minX1, minY1, maxX1, maxY1);
            setObject(pID, minX2, minY2, maxX2, maxY2);
        } else if (pID >= maxObjects) resize(pID);
    }

    /**
   * Update the influence of an object.
   * 
   * @param pID ID of the object.
   * @param pOldAABB Old AABB of the object.
   * @param pAABB New AABB of the object.
   */
    public void updateObject(int pID, int[] pOldAABB, int[] pAABB) {
        int minX1 = pOldAABB[0] >> TILE_SIZE_SHIFT;
        int minY1 = pOldAABB[1] >> TILE_SIZE_SHIFT;
        int maxX1 = pOldAABB[2] >> TILE_SIZE_SHIFT;
        int maxY1 = pOldAABB[3] >> TILE_SIZE_SHIFT;
        int minX2 = pAABB[0] >> TILE_SIZE_SHIFT;
        int minY2 = pAABB[1] >> TILE_SIZE_SHIFT;
        int maxX2 = pAABB[2] >> TILE_SIZE_SHIFT;
        int maxY2 = pAABB[3] >> TILE_SIZE_SHIFT;
        if (minX1 != minX2 || minY1 != minY2 || maxX1 != maxX2 || maxY1 != maxY2) {
            unsetObject(pID, minX1, minY1, maxX1, maxY1);
            setObject(pID, minX2, minY2, maxX2, maxY2);
        } else if (pID >= maxObjects) resize(pID);
    }

    /**
   * Mark the presence of an object in the grid.
   * 
   * @param pID ID of the object.
   * @param pAABB AABB of the object.
   */
    public void setObject(int pID, int[] pAABB) {
        setObject(pID, pAABB[0] >> TILE_SIZE_SHIFT, pAABB[1] >> TILE_SIZE_SHIFT, pAABB[2] >> TILE_SIZE_SHIFT, pAABB[3] >> TILE_SIZE_SHIFT);
    }

    /**
   * Unmark the presence of an object in the grid.
   * 
   * @param pID ID of the object.
   * @param pAABB AABB of the object.
   */
    public void unsetObject(int pID, int[] pAABB) {
        unsetObject(pID, pAABB[0] >> TILE_SIZE_SHIFT, pAABB[1] >> TILE_SIZE_SHIFT, pAABB[2] >> TILE_SIZE_SHIFT, pAABB[3] >> TILE_SIZE_SHIFT);
    }

    private void setObject(int pID, int pMinTileX, int pMinTileY, int pMaxTileX, int pMaxTileY) {
        if (pID >= maxObjects) resize(pID);
        pMinTileX = pMinTileX < 0 ? 0 : pMinTileX;
        pMinTileY = pMinTileY < 0 ? 0 : pMinTileY;
        pMaxTileX = pMaxTileX >= width ? width - 1 : pMaxTileX;
        pMaxTileY = pMaxTileY >= height ? height - 1 : pMaxTileY;
        int px = pMinTileX * numChunksPerCell;
        int py = pMinTileY * numChunksPerCell;
        int chunk = pID >> CHUNK_SIZE_SHIFT;
        pID -= chunk << CHUNK_SIZE_SHIFT;
        long mask = 1L << pID;
        for (; pMinTileX <= pMaxTileX; pMinTileX++, px += numChunksPerCell) cols[px + chunk] |= mask;
        for (; pMinTileY <= pMaxTileY; pMinTileY++, py += numChunksPerCell) rows[py + chunk] |= mask;
    }

    private void unsetObject(int pID, int pMinTileX, int pMinTileY, int pMaxTileX, int pMaxTileY) {
        if (pID >= maxObjects) resize(pID);
        pMinTileX = pMinTileX < 0 ? 0 : pMinTileX;
        pMinTileY = pMinTileY < 0 ? 0 : pMinTileY;
        pMaxTileX = pMaxTileX >= width ? width - 1 : pMaxTileX;
        pMaxTileY = pMaxTileY >= height ? height - 1 : pMaxTileY;
        int px = pMinTileX * numChunksPerCell;
        int py = pMinTileY * numChunksPerCell;
        int chunk = pID >> CHUNK_SIZE_SHIFT;
        pID -= chunk << CHUNK_SIZE_SHIFT;
        long mask = ~(1L << pID);
        for (; pMinTileX <= pMaxTileX; pMinTileX++, px += numChunksPerCell) cols[px + chunk] &= mask;
        for (; pMinTileY <= pMaxTileY; pMinTileY++, py += numChunksPerCell) rows[py + chunk] &= mask;
    }

    /**
   * Draw the grid (used for debug).
   * 
   * @param pGraph Graphics instance to draw with.
   * @param camAABB AABB of the camera to speed things up.
   */
    public void d_draw(Graphics pGraph, int[] camAABB) {
        int minX = camAABB[0] >> TILE_SIZE_SHIFT;
        int minY = camAABB[1] >> TILE_SIZE_SHIFT;
        int maxX = camAABB[2] >> TILE_SIZE_SHIFT;
        int maxY = camAABB[3] >> TILE_SIZE_SHIFT;
        minX = minX < 0 ? 0 : minX;
        minY = minY < 0 ? 0 : minY;
        maxX = maxX >= width ? width - 1 : maxX;
        maxY = maxY >= height ? height - 1 : maxY;
        int oMinX = minX;
        int oMinY = minY;
        minX *= numChunksPerCell;
        minY *= numChunksPerCell;
        maxX *= numChunksPerCell;
        maxY *= numChunksPerCell;
        for (int x = minX, ox = oMinX; x <= maxX; x += numChunksPerCell, ox++) for (int y = minY, oy = oMinY; y <= maxY; y += numChunksPerCell, oy++) {
            boolean found = false;
            for (int j = 0; j < numChunksPerCell; j++) if ((cols[x + j] & rows[y + j]) != 0) {
                found = true;
                break;
            }
            if (found) pGraph.fillRect(ox << TILE_SIZE_SHIFT, oy << TILE_SIZE_SHIFT, 1 << TILE_SIZE_SHIFT, 1 << TILE_SIZE_SHIFT);
        }
    }
}
