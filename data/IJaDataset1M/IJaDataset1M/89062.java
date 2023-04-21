package org.chargecar.prize.util;

import java.util.ArrayList;
import java.util.List;
import org.chargecar.prize.battery.BatteryModel;

/**
 * DO NOT EDIT Stores the result of running a simulation over multiple trips for
 * a single policy. At the moment, only stores the batteryCurrent integral for
 * each trip, as that is what we're interested in with the simple batteries.
 * 
 * @author Alex Styler
 * 
 */
public class SimulationResults {

    private final List<Double> batteryCurrentSquaredIntegrals;

    private final List<Double> whSpent;

    private final List<Double> distanceSums;

    private final List<String> tripStrings;

    private final String policyName;

    public SimulationResults(String policyName) {
        this.policyName = policyName;
        tripStrings = new ArrayList<String>();
        batteryCurrentSquaredIntegrals = new ArrayList<Double>();
        whSpent = new ArrayList<Double>();
        distanceSums = new ArrayList<Double>();
    }

    public void addTrip(Trip trip, BatteryModel battery, BatteryModel capacitor) {
        batteryCurrentSquaredIntegrals.add(battery.getCurrentSquaredIntegral());
        whSpent.add(battery.getMaxWattHours() - battery.getWattHours() - capacitor.getWattHours());
        distanceSums.add(getDistance(trip));
        tripStrings.add(trip.toString());
    }

    private double getDistance(Trip trip) {
        double dist = 0.0;
        for (PointFeatures pf : trip.getPoints()) {
            dist += pf.getPlanarDist();
        }
        return dist;
    }

    public List<Double> getDistances() {
        return this.distanceSums;
    }

    public List<Double> getBatteryCurrentSquaredIntegrals() {
        return this.batteryCurrentSquaredIntegrals;
    }

    public List<Double> getWhSpent() {
        return this.whSpent;
    }

    public List<String> getTripStrings() {
        return this.tripStrings;
    }

    public String getPolicyName() {
        return this.policyName;
    }
}
