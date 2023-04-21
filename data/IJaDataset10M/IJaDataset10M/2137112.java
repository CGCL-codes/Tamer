package playground.scnadine.gpsProcessingV2.stopPointStageAlgorithms;

import java.io.File;
import org.matsim.core.config.Config;
import playground.scnadine.gpsProcessingV2.GPSStopPointStages;

public class GPSStopPointStageCalcCharacteristics extends GPSStopPointStageAlgorithm {

    private String dataorigin;

    private Boolean useTransitStops;

    private GPSStopPointStageCalcDistanceSquareNearestStop distancesSquareNearestStop;

    public GPSStopPointStageCalcCharacteristics(Config config, String CONFIG_MODULE) {
        super();
        this.dataorigin = config.findParam(CONFIG_MODULE, "dataorigin");
        this.useTransitStops = Boolean.valueOf(config.findParam(CONFIG_MODULE, "useNearestStop"));
        if (this.useTransitStops) {
            String filename = config.findParam(CONFIG_MODULE, "sourcedir") + File.separator + "didok201101_transitSchedule.xml";
            this.distancesSquareNearestStop = new GPSStopPointStageCalcDistanceSquareNearestStop(filename);
        }
    }

    @Override
    public void run(GPSStopPointStages stages) {
        GPSStopPointStageCalcDistance distances = new GPSStopPointStageCalcDistance();
        distances.run(stages);
        GPSStopPointStageCalcDuration durations = new GPSStopPointStageCalcDuration();
        durations.run(stages);
        GPSStopPointStageCalcMedianSpeed medianSpeed = new GPSStopPointStageCalcMedianSpeed();
        medianSpeed.run(stages);
        GPSStopPointStageCalcMedianAcceleration medianAcc = new GPSStopPointStageCalcMedianAcceleration();
        medianAcc.run(stages);
        GPSStopPointStageCalc95PercentileAcceleration calc95PercAcc = new GPSStopPointStageCalc95PercentileAcceleration();
        calc95PercAcc.run(stages);
        GPSStopPointStageCalc95PercentileSpeed calc95PercSpeed = new GPSStopPointStageCalc95PercentileSpeed();
        calc95PercSpeed.run(stages);
        GPSStopPointStageCalcAverageSpeed calcAvSpeed = new GPSStopPointStageCalcAverageSpeed();
        calcAvSpeed.run(stages);
        if (this.useTransitStops) {
            this.distancesSquareNearestStop.run(stages);
        }
        if (this.dataorigin.contains("mge")) {
            GPSStopPointStageCalcAccelerometerStatistics calcAccelStats = new GPSStopPointStageCalcAccelerometerStatistics();
            calcAccelStats.run(stages);
            GPSStopPointStageCalcStDevAccelLengthPercentiles calcStDevAccel = new GPSStopPointStageCalcStDevAccelLengthPercentiles();
            calcStDevAccel.run(stages);
        }
    }
}
