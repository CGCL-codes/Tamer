package br.com.felix.fwt.datatype;

import java.io.Serializable;

public class Coordinate implements Serializable {

    private static final long serialVersionUID = -4333804927990656134L;

    public int x;

    public int y;

    public Coordinate() {
    }

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Coordinate)) return false;
        Coordinate other = (Coordinate) obj;
        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        String hash = x + "_" + y;
        return hash.hashCode();
    }
}
