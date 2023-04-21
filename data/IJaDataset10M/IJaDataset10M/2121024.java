package playground.scnadine.GPSDaten.coordAlgorithms;

import java.util.ArrayList;
import java.util.Iterator;
import org.matsim.gbl.Gbl;
import playground.scnadine.GPSDaten.GPSCoord;
import playground.scnadine.GPSDaten.GPSCoordFactory;

public class GPSCoordDetermineActivityBreakPoints extends GPSCoordAlgorithm {

    private boolean useZeroSpeeds;

    private boolean useCoordDensity;

    private boolean useDwellTime;

    private boolean useHeadingChange;

    private int minZeroSpeedTime;

    private int minDensity;

    private int minDistanceBetweenActivities;

    private int minCloudLength;

    private int minCloudDuration;

    private double minDensityRatio;

    private int minDwellTime;

    private int headingChangeRange;

    private double headingChangeBoundary;

    public GPSCoordDetermineActivityBreakPoints() {
        super();
        this.useZeroSpeeds = Boolean.parseBoolean(Gbl.getConfig().getParam("GPS", "useZeroSpeeds"));
        this.useCoordDensity = Boolean.parseBoolean(Gbl.getConfig().getParam("GPS", "useCoordDensity"));
        this.useDwellTime = Boolean.parseBoolean(Gbl.getConfig().getParam("GPS", "useDwellTime"));
        this.useHeadingChange = Boolean.parseBoolean(Gbl.getConfig().getParam("GPS", "useHeadingChange"));
        this.minZeroSpeedTime = Integer.parseInt(Gbl.getConfig().getParam("GPS", "minZeroSpeedTime"));
        this.minDensity = Integer.parseInt(Gbl.getConfig().getParam("GPS", "minDensity"));
        this.minDistanceBetweenActivities = Integer.parseInt(Gbl.getConfig().getParam("GPS", "minDistanceBetweenActivities"));
        this.minCloudLength = Integer.parseInt(Gbl.getConfig().getParam("GPS", "minCloudLength"));
        this.minCloudDuration = Integer.parseInt(Gbl.getConfig().getParam("GPS", "minCloudDuration"));
        this.minDensityRatio = Double.parseDouble(Gbl.getConfig().getParam("GPS", "minDensityRatio"));
        this.minDwellTime = Integer.parseInt(Gbl.getConfig().getParam("GPS", "minDwellTime"));
        this.headingChangeRange = Integer.parseInt(Gbl.getConfig().getParam("GPS", "headingChangeRange"));
        this.headingChangeBoundary = Double.parseDouble(Gbl.getConfig().getParam("GPS", "headingChangeBoundary"));
    }

    @Override
    public void run(GPSCoordFactory gpsFactory) {
        if (this.useZeroSpeeds) {
            GPSCoordCalcZeroSpeedCounter zeroSpeedCalculator = new GPSCoordCalcZeroSpeedCounter();
            zeroSpeedCalculator.run(gpsFactory);
            Iterator<GPSCoord> itZS = gpsFactory.getCoords().iterator();
            boolean zeroSpeed = false;
            while (itZS.hasNext()) {
                GPSCoord currentCoord = itZS.next();
                if (currentCoord.getZeroSpeedCounter() > this.minZeroSpeedTime) {
                    zeroSpeed = true;
                }
                if (zeroSpeed && currentCoord.getZeroSpeedCounter() == 0) {
                    gpsFactory.getCoords().get(currentCoord.getCoordID() - 1).setPotentialZeroSpeedBreakPoint("activityEnd");
                    gpsFactory.getCoords().get(currentCoord.getCoordID() - 1).getFirstZeroSpeedCoord().setPotentialZeroSpeedBreakPoint("activityStart");
                    zeroSpeed = false;
                }
            }
        }
        if (this.useCoordDensity) {
            GPSCoordCalcDensity densityCalculator = new GPSCoordCalcDensity();
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
                    if (distanceSinceCloud <= this.minDistanceBetweenActivities) {
                        distanceSinceCloud++;
                    } else {
                        double sequenceLength = cloudPoints.get(cloudPoints.size() - 1).getCoordID() - cloudPoints.get(0).getCoordID() + 1;
                        long sequenceTime = cloudPoints.get(cloudPoints.size() - 1).getTimestamp().getTimeInMillis() / 1000 - cloudPoints.get(0).getTimestamp().getTimeInMillis() / 1000;
                        if ((sequenceLength > this.minCloudLength || sequenceTime > this.minCloudDuration) && (cloudPoints.size() / sequenceLength > this.minDensityRatio)) {
                            gpsFactory.getCoords().get(cloudPoints.get(0).getCoordID()).setPotentialCloudBreakPoint("activityStart");
                            gpsFactory.getCoords().get(cloudPoints.get(cloudPoints.size() - 1).getCoordID()).setPotentialCloudBreakPoint("activityEnd");
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
                    currentCoord.setPotentialDwellTimeBreakPoint("activityEnd");
                    gpsFactory.getCoords().get(currentCoord.getCoordID() - 1).setPotentialDwellTimeBreakPoint("activityStart");
                }
            }
        }
        if (this.useHeadingChange) {
            GPSCoordCalcHeadingChange headingChangeCalculator = new GPSCoordCalcHeadingChange();
            headingChangeCalculator.run(gpsFactory);
            Iterator<GPSCoord> itHC = gpsFactory.getCoords().iterator();
            while (itHC.hasNext()) {
                GPSCoord currentCoord = itHC.next();
                GPSCoord predecessor = null;
                GPSCoord successor = null;
                if (currentCoord.getCoordID() > (0 + this.headingChangeRange / 2)) predecessor = gpsFactory.getCoords().get(currentCoord.getCoordID() - this.headingChangeRange / 2);
                if (currentCoord.getCoordID() <= (gpsFactory.getCoords().size() - 1 - this.headingChangeRange / 2)) successor = gpsFactory.getCoords().get(currentCoord.getCoordID() + this.headingChangeRange / 2);
                if (currentCoord.getHeadingChange() > this.headingChangeBoundary && predecessor != null && successor != null) {
                    predecessor.setPotentialHeadingChangeBreakPoint("activityEnd");
                    successor.setPotentialHeadingChangeBreakPoint("activityStart");
                }
            }
        }
        GPSCoordJoinPotentialActivityBreakPoints joinBreakPoints = new GPSCoordJoinPotentialActivityBreakPoints();
        joinBreakPoints.run(gpsFactory);
        if (gpsFactory.getCoords().get(0).getActivityBreakPoint() == "none") gpsFactory.getCoords().get(0).setActivityBreakPoint("activityEnd");
        if (gpsFactory.getCoords().get(gpsFactory.getCoords().size() - 1).getActivityBreakPoint() == "none") gpsFactory.getCoords().get(gpsFactory.getCoords().size() - 1).setActivityBreakPoint("activityStart");
    }
}
