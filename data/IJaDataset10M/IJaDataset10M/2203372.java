package teach.matsim08.sheet5;

import java.util.Comparator;

class CAVehicleDepartureTimeComparator implements Comparator<CAVehicle> {

    public int compare(CAVehicle v1, CAVehicle v2) {
        if (v1.getDepartureTime() > v2.getDepartureTime()) return 1; else if (v1.getDepartureTime() < v2.getDepartureTime()) return -1; else return 0;
    }
}
