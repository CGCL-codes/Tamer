package playground.dsantani.gpsProcessing.tripAlgorithms;

import java.util.Iterator;
import org.matsim.core.config.Config;
import playground.dsantani.gpsProcessing.GPSCoord;
import playground.dsantani.gpsProcessing.GPSTrip;
import playground.dsantani.gpsProcessing.GPSTrips;

public class GPSTripDetectEndOfGapPoints extends GPSTripAlgorithm {

    private int minGapLength;

    public GPSTripDetectEndOfGapPoints(Config config, String CONFIG_MODULE) {
        super();
        this.minGapLength = Integer.parseInt(config.getParam(CONFIG_MODULE, "minGapLength"));
    }

    @Override
    public void run(GPSTrips trips) {
        Iterator<GPSTrip> it = trips.getTrips().iterator();
        while (it.hasNext()) {
            GPSTrip currentTrip = it.next();
            GPSCoord[] tripCoords = currentTrip.getTripCoords();
            if (tripCoords[0].getPotentialModeTransferPoint().equals("none")) tripCoords[0].setPotentialModeTransferPoint("EOG");
            for (int i = 1; i < tripCoords.length; i++) {
                if (tripCoords[i].getTimeDiffToPredecessor() > this.minGapLength) tripCoords[i].setPotentialModeTransferPoint("EOG");
            }
        }
    }
}
