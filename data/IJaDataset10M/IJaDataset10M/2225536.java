package com.myapp.game.foxAndBunny.model;

import java.util.HashMap;
import java.util.Map;
import com.myapp.game.foxAndBunny.Logger;
import com.myapp.util.cache.compress.IntToLongCompressor;

/**
 * an immutable position with a row and column 
 * 
 * @author andre
 *
 */
public class Position {

    public static final Position NOWHERE = new Position(-1, -1);

    private static final int CACHE_SIZE_MAX = 16 * 1024;

    private static Map<Long, Position> CACHE = new HashMap<Long, Position>(CACHE_SIZE_MAX);

    private final int row, col;

    private transient int hash = -1;

    private Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public static Position create(int row, int col) {
        Long key = IntToLongCompressor.pack(row, col);
        Position pos = CACHE.get(key);
        if (pos != null) {
            Logger.debug("Position.create() cache hit: " + pos);
            return pos;
        }
        pos = new Position(row, col);
        if (CACHE.size() < CACHE_SIZE_MAX) {
            Logger.debug("Position.create() caching: " + pos);
            CACHE.put(key, pos);
        } else {
            Logger.debug("Position.create() cache full: " + CACHE.size());
        }
        assert pos.col == col && pos.row == row : "row=" + row + ", col=" + col + ", pos=" + pos;
        return pos;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public int hashCode() {
        if (hash == -1) {
            hash = hashCode(row, col);
        }
        return hash;
    }

    private static int hashCode(int row, int col) {
        final int prime = 31;
        int result = 1;
        result = prime * result + col;
        result = prime * result + row;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Position other = (Position) obj;
        if (col != other.col) return false;
        if (row != other.row) return false;
        return true;
    }

    @Override
    public String toString() {
        return "(r" + row + "c" + col + ")";
    }
}
