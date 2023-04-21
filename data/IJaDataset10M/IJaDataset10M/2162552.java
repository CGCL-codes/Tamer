package com.infinity.map;

import java.io.*;

public class Tile {

    public int loadedZoom;

    public Nodes node;

    public Routes route;

    public PolyFig line;

    public PolyFig poly;

    public Foreign foreign;

    public Tile() {
    }

    public int Read(DataInputStream din) {
        return 0;
    }

    public void clear() {
        node = null;
        route = null;
        line = null;
        poly = null;
        foreign = null;
        loadedZoom = 0;
    }
}
