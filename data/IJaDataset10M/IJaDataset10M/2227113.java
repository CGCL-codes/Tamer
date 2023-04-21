package net.sourceforge.aidungeon.common.map.mapCoordinate;

import java.io.Serializable;

public class MapCoordinate implements Serializable {

    private static final long serialVersionUID = 8398401604002876104L;

    private final int x;

    private final int y;

    public MapCoordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public MapCoordinate getNorth() {
        return new MapCoordinate(x, y - 1);
    }

    public MapCoordinate getNorthEast() {
        return new MapCoordinate(x + 1, y - 1);
    }

    public MapCoordinate getEast() {
        return new MapCoordinate(x + 1, y);
    }

    public MapCoordinate getSouthEast() {
        return new MapCoordinate(x + 1, y + 1);
    }

    public MapCoordinate getSouth() {
        return new MapCoordinate(x, y + 1);
    }

    public MapCoordinate getSouthWest() {
        return new MapCoordinate(x - 1, y + 1);
    }

    public MapCoordinate getWest() {
        return new MapCoordinate(x - 1, y);
    }

    public MapCoordinate getNorthWest() {
        return new MapCoordinate(x - 1, y - 1);
    }

    public boolean isNextTo(MapCoordinate coordinate, boolean includeDiagonalDirections) {
        if (this.getNorth().equals(coordinate)) {
            return true;
        } else if (this.getEast().equals(coordinate)) {
            return true;
        } else if (this.getSouth().equals(coordinate)) {
            return true;
        } else if (this.getWest().equals(coordinate)) {
            return true;
        }
        if (includeDiagonalDirections) {
            if (this.getNorthEast().equals(coordinate)) {
                return true;
            }
            if (this.getSouthEast().equals(coordinate)) {
                return true;
            }
            if (this.getSouthWest().equals(coordinate)) {
                return true;
            }
            if (this.getNorthWest().equals(coordinate)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        MapCoordinate other = (MapCoordinate) obj;
        if (x != other.x) return false;
        if (y != other.y) return false;
        return true;
    }

    @Override
    public String toString() {
        return "x = " + x + ", y = " + y;
    }
}
