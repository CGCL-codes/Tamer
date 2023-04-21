package no.uio.edd.model.geo.calc;

import java.util.Comparator;

public class MapPlaceComparator implements Comparator<MapPlace> {

    @Override
    public int compare(MapPlace o1, MapPlace o2) {
        int conn1 = o1.getConnectivity();
        int conn2 = o2.getConnectivity();
        if (conn1 < conn2) return 1; else if (conn1 == conn2) return 0; else return -1;
    }
}
