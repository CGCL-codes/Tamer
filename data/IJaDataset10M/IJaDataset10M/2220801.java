package com.vividsolutions.jump.geom;

import java.util.*;
import com.vividsolutions.jts.geom.Coordinate;

/**
 * A CoordinateList is a list of Coordinates.
 * It prevents duplicate consecutive coordinates from appearing in the list.
 */
public class CoordinateList extends ArrayList {

    private static final Coordinate[] coordArrayType = new Coordinate[0];

    public CoordinateList() {
    }

    public Coordinate getCoordinate(int i) {
        return (Coordinate) get(i);
    }

    public boolean add(Object obj) {
        add((Coordinate) obj);
        return true;
    }

    public void add(Coordinate coord) {
        if (size() >= 1) {
            Coordinate last = (Coordinate) get(size() - 1);
            if (last.equals(coord)) {
                return;
            }
        }
        super.add(coord);
    }

    public boolean addAll(Collection coll) {
        boolean isChanged = false;
        for (Iterator i = coll.iterator(); i.hasNext(); ) {
            add((Coordinate) i.next());
            isChanged = true;
        }
        return isChanged;
    }

    /**
     * Ensure this coordList is a ring, by adding the start point if necessary
     */
    public void closeRing() {
        if (size() > 0) {
            add(get(0));
        }
    }

    public Coordinate[] toCoordinateArray() {
        return (Coordinate[]) toArray(coordArrayType);
    }
}
