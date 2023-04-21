package playground.scnadine.gpsProcessingV2.coordAlgorithms;

import java.util.ArrayList;
import org.matsim.core.config.Config;
import playground.scnadine.gpsProcessingV2.GPSCoord;
import playground.scnadine.gpsProcessingV2.GPSCoordFactory;

public class GPSCoordDetermineAccelerometerWalkBreakPoints extends GPSCoordAlgorithm {

    private Config config;

    private String CONFIG_MODULE;

    public GPSCoordDetermineAccelerometerWalkBreakPoints(Config config, String CONFIG_MODULE) {
        super();
        this.config = config;
        this.CONFIG_MODULE = CONFIG_MODULE;
    }

    @Override
    public void run(GPSCoordFactory gpsFactory) {
        ArrayList<GPSCoord> nonStopPointCoords = new ArrayList<GPSCoord>();
        boolean stopPointOngoing = false;
        GPSCoordDetectStartAndEndOfWalkingByAccelerometer detectSOWAndEOW = new GPSCoordDetectStartAndEndOfWalkingByAccelerometer(this.config, this.CONFIG_MODULE);
        GPSCoordJoinModeWalkByAccelerometerTransferPoints joinWalkStages = new GPSCoordJoinModeWalkByAccelerometerTransferPoints(this.config, this.CONFIG_MODULE);
        GPSCoordSetStopPointBreakPointsForAccelerometerMTPs setBPs = new GPSCoordSetStopPointBreakPointsForAccelerometerMTPs();
        for (GPSCoord coord : gpsFactory.getCoords()) {
            if (coord.getStopPointBreakPoint().equals("stopPointStart")) {
                if (nonStopPointCoords.size() > 0) {
                    detectSOWAndEOW.run(nonStopPointCoords);
                    joinWalkStages.run(nonStopPointCoords);
                    setBPs.run(nonStopPointCoords);
                    nonStopPointCoords.clear();
                }
                stopPointOngoing = true;
            } else if (coord.getStopPointBreakPoint().equals("stopPointEnd")) {
                stopPointOngoing = false;
            }
            if (!stopPointOngoing) {
                nonStopPointCoords.add(coord);
            }
        }
        if (nonStopPointCoords.size() > 0) {
            detectSOWAndEOW.run(nonStopPointCoords);
            joinWalkStages.run(nonStopPointCoords);
            setBPs.run(nonStopPointCoords);
            nonStopPointCoords.clear();
        }
    }
}
