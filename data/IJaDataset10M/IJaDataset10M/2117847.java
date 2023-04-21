package org.jug.tdd.model;

public class Coordinates {

    int x, y;

    public int getX() {
        return this.x;
    }

    public void setX(int _x) {
        this.x = _x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int _y) {
        this.y = _y;
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
        Coordinates other = (Coordinates) obj;
        if (x != other.x) return false;
        if (y != other.y) return false;
        return true;
    }
}
