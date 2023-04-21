package com.terrain;

import java.util.List;

public interface Chunk {

    static final int MIN_X = 0, MAX_X = 32, MIN_Z = 0, MAX_Z = 32;

    Block getBlock(int x, int y, int z);

    List<Block> getBlocks();

    List<Entity> getEntities();

    List<TileEntity> getTileEntities();

    long getLastUpdate();

    int getLocalX();

    int getLocalZ();

    int getXpos();

    int getZpos();

    boolean isTerrainPopulated();

    String getName();
}
