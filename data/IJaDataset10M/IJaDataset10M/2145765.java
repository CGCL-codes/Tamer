package com.luzan.app.map.bean;

public class GNorMapTile extends ExtMapTile {

    public GNorMapTile(int x, int y, int z) {
        super("http://mt" + getHostId(x, y) + ".google.com/mt?v=w2.63&x=" + x + "&y=" + y + "&zoom=" + z, x, y, z);
    }
}
