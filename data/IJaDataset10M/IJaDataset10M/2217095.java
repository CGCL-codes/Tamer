package org.spantus.core.marker;

import java.util.Comparator;

public class MarkerTimeComparator implements Comparator<Marker> {

    public int compare(Marker o1, Marker o2) {
        if (o1 == null) {
            return -1;
        }
        if (o2 == null) {
            return 1;
        }
        return o1.getStart() - o2.getStart() > 0 ? 1 : -1;
    }
}
