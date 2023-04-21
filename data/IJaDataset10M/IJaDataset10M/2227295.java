package playground.dsantani.gpsProcessing.tripAlgorithms;

import java.util.Iterator;
import playground.dsantani.gpsProcessing.GPSCoord;
import playground.dsantani.gpsProcessing.GPSTrip;
import playground.dsantani.gpsProcessing.GPSTrips;

public class GPSTripCalcDuration extends GPSTripAlgorithm {

    public GPSTripCalcDuration() {
        super();
    }

    @Override
    public void run(GPSTrips trips) {
        Iterator<GPSTrip> it = trips.getTrips().iterator();
        while (it.hasNext()) {
            GPSTrip currentTrip = it.next();
            GPSCoord firstCoord = currentTrip.getTripCoords()[0];
            GPSCoord lastCoord = currentTrip.getTripCoords()[currentTrip.getTripCoords().length - 1];
            double tripDuration = ((double) (lastCoord.getTimestamp().getTimeInMillis() - firstCoord.getTimestamp().getTimeInMillis())) / 1000;
            currentTrip.setTripDuration(tripDuration);
        }
    }
}
