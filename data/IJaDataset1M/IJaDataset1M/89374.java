package playground.dsantani.gpsProcessing.coordAlgorithms;

import java.util.ArrayList;
import java.util.Iterator;
import org.matsim.core.config.Config;
import playground.dsantani.gpsProcessing.GPSCoord;
import playground.dsantani.gpsProcessing.GPSCoordFactory;

public class GPSCoordDetermineActivityBreakPoints extends GPSCoordAlgorithm {

    private Config config;

    private String CONFIG_MODULE;

    private String activityStart = "activityStart";

    private String activityEnd = "activityEnd";

    private boolean useZeroSpeeds;

    private boolean useCoordDensity;

    private boolean useDwellTime;

    private boolean useHeadingChange;

    private int minZeroSpeedTime;

    private int minDensity;

    private int minNoPointsBetweenActivities;

    private int minCloudLength;

    private int minCloudDuration;

    private double minDensityRatio;

    private int minDwellTime;

    private int headingChangeRange;

    private double headingChangeBoundary;

    public GPSCoordDetermineActivityBreakPoints(Config config, String CONFIG_MODULE) {
        super();
        this.config = config;
        this.CONFIG_MODULE = CONFIG_MODULE;
        this.useZeroSpeeds = Boolean.parseBoolean(config.getParam(CONFIG_MODULE, "useZeroSpeeds"));
        this.useCoordDensity = Boolean.parseBoolean(config.getParam(CONFIG_MODULE, "useCoordDensity"));
        this.useDwellTime = Boolean.parseBoolean(config.getParam(CONFIG_MODULE, "useDwellTime"));
        this.useHeadingChange = Boolean.parseBoolean(config.getParam(CONFIG_MODULE, "useHeadingChange"));
        this.minZeroSpeedTime = Integer.parseInt(config.getParam(CONFIG_MODULE, "minZeroSpeedTime"));
        this.minDensity = Integer.parseInt(config.getParam(CONFIG_MODULE, "minDensity"));
        this.minNoPointsBetweenActivities = Integer.parseInt(config.getParam(CONFIG_MODULE, "minNoPointsBetweenActivities"));
        this.minCloudLength = Integer.parseInt(config.getParam(CONFIG_MODULE, "minCloudLength"));
        this.minCloudDuration = Integer.parseInt(config.getParam(CONFIG_MODULE, "minCloudDuration"));
        this.minDensityRatio = Double.parseDouble(config.getParam(CONFIG_MODULE, "minDensityRatio"));
        this.minDwellTime = Integer.parseInt(config.getParam(CONFIG_MODULE, "minDwellTime"));
        this.headingChangeRange = Integer.parseInt(config.getParam(CONFIG_MODULE, "headingChangeRange"));
        this.headingChangeBoundary = Double.parseDouble(config.getParam(CONFIG_MODULE, "headingChangeBoundary"));
    }

    @Override
    public void run(GPSCoordFactory gpsFactory) {
        if (this.useZeroSpeeds) {
            GPSCoordCalcZeroSpeedCounter zeroSpeedCalculator = new GPSCoordCalcZeroSpeedCounter(this.config, this.CONFIG_MODULE);
            zeroSpeedCalculator.run(gpsFactory);
            Iterator<GPSCoord> itZS = gpsFactory.getCoords().iterator();
            boolean zeroSpeed = false;
            while (itZS.hasNext()) {
                GPSCoord currentCoord = itZS.next();
                if (currentCoord.getZeroSpeedCounter() > this.minZeroSpeedTime) {
                    zeroSpeed = true;
                }
                if (zeroSpeed && currentCoord.getZeroSpeedCounter() == 0) {
                    gpsFactory.getCoords().get(currentCoord.getCoordID()).setPotentialZeroSpeedBreakPoint(this.activityEnd);
                    gpsFactory.getCoords().get(currentCoord.getCoordID() - 1).getFirstZeroSpeedCoord().setPotentialZeroSpeedBreakPoint(this.activityStart);
                    zeroSpeed = false;
                }
            }
        }
        if (this.useCoordDensity) {
            GPSCoordCalcDensity densityCalculator = new GPSCoordCalcDensity(this.config, this.CONFIG_MODULE);
            densityCalculator.run(gpsFactory);
            ArrayList<GPSCoord> cloudPoints = new ArrayList<GPSCoord>();
            int distanceSinceCloud = 0;
            Iterator<GPSCoord> itCl = gpsFactory.getCoords().iterator();
            while (itCl.hasNext()) {
                GPSCoord currentCoord = itCl.next();
                if (currentCoord.getDensity() >= this.minDensity) {
                    cloudPoints.add(currentCoord);
                    distanceSinceCloud = 0;
                } else if (!cloudPoints.isEmpty()) {
                    if (distanceSinceCloud <= this.minNoPointsBetweenActivities) {
                        distanceSinceCloud++;
                    } else {
                        double sequenceLength = cloudPoints.get(cloudPoints.size() - 1).getCoordID() - cloudPoints.get(0).getCoordID() + 1;
                        long sequenceTime = cloudPoints.get(cloudPoints.size() - 1).getTimestamp().getTimeInMillis() / 1000 - cloudPoints.get(0).getTimestamp().getTimeInMillis() / 1000;
                        if ((sequenceLength > this.minCloudLength || sequenceTime > this.minCloudDuration) && (cloudPoints.size() / sequenceLength > this.minDensityRatio)) {
                            gpsFactory.getCoords().get(cloudPoints.get(0).getCoordID()).setPotentialCloudBreakPoint(this.activityStart);
                            gpsFactory.getCoords().get(cloudPoints.get(cloudPoints.size() - 1).getCoordID()).setPotentialCloudBreakPoint(this.activityEnd);
                        }
                        cloudPoints.clear();
                        distanceSinceCloud = 0;
                    }
                }
            }
        }
        if (this.useDwellTime) {
            Iterator<GPSCoord> itDT = gpsFactory.getCoords().iterator();
            while (itDT.hasNext()) {
                GPSCoord currentCoord = itDT.next();
                if (currentCoord.getTimeDiffToPredecessor() > this.minDwellTime) {
                    currentCoord.setPotentialDwellTimeBreakPoint(this.activityEnd);
                    gpsFactory.getCoords().get(currentCoord.getCoordID() - 1).setPotentialDwellTimeBreakPoint(this.activityStart);
                }
            }
        }
        if (this.useHeadingChange) {
            GPSCoordCalcHeadingChange headingChangeCalculator = new GPSCoordCalcHeadingChange(this.config, this.CONFIG_MODULE);
            headingChangeCalculator.run(gpsFactory);
            Iterator<GPSCoord> itHC = gpsFactory.getCoords().iterator();
            while (itHC.hasNext()) {
                GPSCoord currentCoord = itHC.next();
                GPSCoord predecessor = null;
                GPSCoord successor = null;
                if (currentCoord.getCoordID() > (0 + this.headingChangeRange / 2)) predecessor = gpsFactory.getCoords().get(currentCoord.getCoordID() - this.headingChangeRange / 2);
                if (currentCoord.getCoordID() <= (gpsFactory.getCoords().size() - 1 - this.headingChangeRange / 2)) successor = gpsFactory.getCoords().get(currentCoord.getCoordID() + this.headingChangeRange / 2);
                if (currentCoord.getHeadingChange() > this.headingChangeBoundary && predecessor != null && successor != null) {
                    predecessor.setPotentialHeadingChangeBreakPoint(this.activityEnd);
                    successor.setPotentialHeadingChangeBreakPoint(this.activityStart);
                }
            }
        }
        GPSCoordJoinPotentialActivityBreakPoints joinBreakPoints = new GPSCoordJoinPotentialActivityBreakPoints(this.config, this.CONFIG_MODULE);
        joinBreakPoints.run(gpsFactory);
        if (gpsFactory.getCoords().get(0).getActivityBreakPoint().equals("none")) gpsFactory.getCoords().get(0).setActivityBreakPoint(this.activityEnd);
        if (gpsFactory.getCoords().get(gpsFactory.getCoords().size() - 1).getActivityBreakPoint().equals("none")) gpsFactory.getCoords().get(gpsFactory.getCoords().size() - 1).setActivityBreakPoint(this.activityStart);
    }
}
