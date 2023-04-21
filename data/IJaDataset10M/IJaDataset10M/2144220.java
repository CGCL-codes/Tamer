package playground.scnadine.gpsProcessingV2.coordAlgorithms;

import org.matsim.core.config.Config;
import playground.scnadine.gpsProcessingV2.GPSCoord;
import playground.scnadine.gpsProcessingV2.GPSCoordFactory;

public class GPSCoordCalcTimeDiffAndDistanceToPredecessor extends GPSCoordAlgorithm {

    private int coordDimensions;

    public GPSCoordCalcTimeDiffAndDistanceToPredecessor(String coordDimensionType, Config config, String CONFIG_MODULE) {
        super();
        this.coordDimensions = Integer.parseInt(config.findParam(CONFIG_MODULE, coordDimensionType));
    }

    @Override
    public void run(GPSCoordFactory gpsFactory) {
        GPSCoord lastCoord = null;
        for (GPSCoord coord : gpsFactory.getCoords()) {
            double distance = 0;
            long timeDiffToPred = 0;
            if (lastCoord != null) {
                timeDiffToPred = (coord.getTimestamp().getTimeInMillis() - lastCoord.getTimestamp().getTimeInMillis()) / 1000;
                if (timeDiffToPred <= 0) {
                    System.out.println("time diff: " + timeDiffToPred + ", coord: " + coord.getId() + ", " + coord.getDate() + " " + coord.getTime() + ", last coord: " + lastCoord.getId() + ", " + lastCoord.getDate() + " " + lastCoord.getTime());
                }
                double[] distanceVector = new double[3];
                distanceVector[0] = coord.getX() - lastCoord.getX();
                distanceVector[1] = coord.getY() - lastCoord.getY();
                distanceVector[2] = coord.getZ() - lastCoord.getZ();
                coord.setDistanceVector(distanceVector);
                if (this.coordDimensions == 2) distance = Math.sqrt(Math.pow(distanceVector[0], 2) + Math.pow(distanceVector[1], 2)); else if (this.coordDimensions == 3) distance = Math.sqrt(Math.pow(distanceVector[0], 2) + Math.pow(distanceVector[1], 2) + Math.pow(distanceVector[2], 2));
            }
            coord.setTimeDiffToPred(timeDiffToPred);
            coord.setDistance(distance);
            lastCoord = coord;
        }
    }
}
