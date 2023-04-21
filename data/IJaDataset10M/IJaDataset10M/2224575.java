package playground.scnadine.gpsProcessingV2.qualitySegmentAlgorithms;

import java.util.Iterator;
import java.util.ListIterator;
import org.matsim.core.config.Config;
import playground.scnadine.gpsProcessingV2.GPSCoord;
import playground.scnadine.gpsProcessingV2.GPSQualitySegment;
import playground.scnadine.gpsProcessingV2.GPSQualitySegments;

public class GPSQualitySegmentAssignCuts extends GPSQualitySegmentAlgorithm {

    private double speedBoundary;

    private double randomErrorBuffer;

    private int coordDimensions;

    public GPSQualitySegmentAssignCuts(Config config, String CONFIG_MODULE) {
        super();
        this.speedBoundary = Double.parseDouble(config.findParam(CONFIG_MODULE, "speedBoundary"));
        this.randomErrorBuffer = Double.parseDouble(config.findParam(CONFIG_MODULE, "randomErrorBuffer"));
        this.coordDimensions = Integer.parseInt(config.findParam(CONFIG_MODULE, "coordDimensionsFiltering"));
    }

    @Override
    public void run(GPSQualitySegments qualitySegments) {
        Iterator<GPSQualitySegment> it = qualitySegments.getQualitySegments().iterator();
        GPSQualitySegment lastQualitySegment = qualitySegments.getQualitySegments().get(0);
        while (it.hasNext()) {
            GPSQualitySegment currentQualitySegment = it.next();
            if (currentQualitySegment != lastQualitySegment) {
                if (currentQualitySegment.getQualitySegmentDuration() <= lastQualitySegment.getQualitySegmentDuration()) {
                    GPSCoord referenceCoord = lastQualitySegment.getQualitySegmentCoords().get(lastQualitySegment.getQualitySegmentCoords().size() - 1);
                    Iterator<GPSCoord> itt = currentQualitySegment.getQualitySegmentCoords().iterator();
                    while (itt.hasNext()) {
                        GPSCoord coord = itt.next();
                        if (calcDistance(referenceCoord, coord) > Math.abs(calcTimeDiff(referenceCoord, coord) * this.speedBoundary + this.randomErrorBuffer)) {
                            coord.setToBeRemoved(true);
                        } else break;
                    }
                } else {
                    GPSCoord referenceCoord = currentQualitySegment.getQualitySegmentCoords().get(0);
                    ListIterator<GPSCoord> itt = lastQualitySegment.getQualitySegmentCoords().listIterator(lastQualitySegment.getQualitySegmentCoords().size());
                    while (itt.hasPrevious()) {
                        GPSCoord coord = itt.previous();
                        if (calcDistance(referenceCoord, coord) > Math.abs(calcTimeDiff(referenceCoord, coord) * this.speedBoundary + this.randomErrorBuffer)) {
                            coord.setToBeRemoved(true);
                        } else break;
                    }
                }
            }
            lastQualitySegment = currentQualitySegment;
        }
    }

    public double calcDistance(GPSCoord coord1, GPSCoord coord2) {
        double distance = 0;
        double[] distanceVector = new double[3];
        distanceVector[0] = coord1.getX() - coord2.getX();
        distanceVector[1] = coord1.getY() - coord2.getY();
        distanceVector[2] = coord1.getZ() - coord2.getZ();
        if (this.coordDimensions == 2) distance = Math.sqrt(Math.pow(distanceVector[0], 2) + Math.pow(distanceVector[1], 2)); else if (this.coordDimensions == 3) distance = Math.sqrt(Math.pow(distanceVector[0], 2) + Math.pow(distanceVector[1], 2) + Math.pow(distanceVector[2], 2));
        return distance;
    }

    public double calcTimeDiff(GPSCoord coord1, GPSCoord coord2) {
        return (coord1.getTimestamp().getTimeInMillis() - coord2.getTimestamp().getTimeInMillis()) / (double) 1000;
    }
}
