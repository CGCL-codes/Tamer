package org.sablecc.sablecc.lrautomaton;

import java.util.*;

public class Farther implements Ahead {

    private static Map<Integer, Farther> distanceToFartherMap = new LinkedHashMap<Integer, Farther>();

    private final int distance;

    private Farther(int distance) {
        this.distance = distance;
    }

    public static Farther get(int distance) {
        Farther farther = distanceToFartherMap.get(distance);
        if (farther == null) {
            farther = new Farther(distance);
            distanceToFartherMap.put(distance, farther);
        }
        return farther;
    }

    public int getDistance() {
        return this.distance;
    }

    @Override
    public String toString() {
        return "Farther(" + this.distance + ")";
    }
}
