package org.chargecar.algodev;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.chargecar.algodev.knn.KnnKdTreePolicy;
import org.chargecar.algodev.knn.KnnPolicy;
import org.chargecar.algodev.knn.KnnTableTrainer;
import org.chargecar.prize.battery.BatteryModel;
import org.chargecar.prize.battery.LiFePo4;
import org.chargecar.prize.battery.SimpleCapacitor;
import org.chargecar.prize.policies.NoCapPolicy;
import org.chargecar.prize.policies.Policy;
import org.chargecar.prize.util.GPXTripParser;
import org.chargecar.prize.util.PointFeatures;
import org.chargecar.prize.util.PowerFlowException;
import org.chargecar.prize.util.PowerFlows;
import org.chargecar.prize.util.SimulationResults;
import org.chargecar.prize.util.Trip;
import org.chargecar.prize.util.TripFeatures;
import org.chargecar.prize.util.Vehicle;
import org.chargecar.prize.visualization.ConsoleWriter;
import org.chargecar.prize.visualization.Visualizer;

/**
 * DO NOT EDIT 
 * Simulates the car over trips, making estimates for demand based on
 * a KNN lookup over past history of driving.  History is defined by
 * the points stored in the *.knn files created using SimulatorTrainer
 * @author Alex Styler
 * 
 */
public class SimulatorKNN {

    static Vehicle civic = new Vehicle(1200, 1.988, 0.31, 0.015);

    static Visualizer visualizer = new ConsoleWriter();

    static double systemVoltage = 96;

    static double batteryWhr = 50000;

    static double capWhr = 50;

    /**
     * @param args
     *            A pathname to a folder containing *.knn files for each driver
     *            to be tested
     *        	  e.g. java SimulatorKNN "C:\ccpdata\gpxdata\test" "C:\ccpdata\knnfolder" 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        if (args == null || args.length < 2) {
            System.err.println("ERROR: Provide both GPX directory and KNN directory");
            System.exit(1);
        }
        String gpxFolder = args[0];
        String knnFolder = args[1];
        File folder = new File(gpxFolder);
        List<File> gpxFiles;
        if (folder.isDirectory()) {
            gpxFiles = getGPXFiles(folder);
        } else {
            gpxFiles = new ArrayList<File>();
            gpxFiles.add(folder);
        }
        System.out.println("Testing on " + gpxFiles.size() + " GPX files.");
        List<Policy> policies = new ArrayList<Policy>();
        policies.add(new NoCapPolicy());
        policies.add(new KnnKdTreePolicy(knnFolder, 7, 240));
        for (Policy p : policies) {
            p.loadState();
        }
        List<SimulationResults> results = simulateTrips(policies, gpxFiles);
        visualizer.visualizeSummary(results);
    }

    private static List<SimulationResults> simulateTrips(List<Policy> policies, List<File> tripFiles) throws IOException {
        List<SimulationResults> results = new ArrayList<SimulationResults>();
        for (Policy p : policies) {
            results.add(new SimulationResults(p.getName()));
        }
        int count = 0;
        for (File tripFile : tripFiles) {
            List<Trip> tripsToTest = parseTrips(tripFile);
            for (Trip t : tripsToTest) {
                count++;
                System.out.println("Trip " + count + ": " + t.getPoints().size() + "points.");
                for (int i = 0; i < policies.size(); i++) {
                    try {
                        simulateTrip(policies.get(i), t, results.get(i));
                    } catch (PowerFlowException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        System.out.println();
        System.out.println("Trips tested: " + count);
        return results;
    }

    private static void simulateTrip(Policy policy, Trip trip, SimulationResults results) throws PowerFlowException {
        BatteryModel tripBattery = new LiFePo4(batteryWhr, batteryWhr, systemVoltage);
        BatteryModel tripCap = new SimpleCapacitor(capWhr, 0, systemVoltage);
        simulate(policy, trip, tripBattery, tripCap);
        results.addTrip(trip, tripBattery, tripCap);
    }

    private static void simulate(Policy policy, Trip trip, BatteryModel battery, BatteryModel cap) throws PowerFlowException {
        policy.beginTrip(trip.getFeatures(), battery.createClone(), cap.createClone());
        for (PointFeatures point : trip.getPoints()) {
            PowerFlows pf = policy.calculatePowerFlows(point);
            battery.drawPower(pf.getBatteryToCapacitor() + pf.getBatteryToMotor(), point);
            cap.drawPower(pf.getCapacitorToMotor() - pf.getBatteryToCapacitor(), point);
        }
        policy.endTrip();
    }

    private static List<Trip> parseTrips(File gpxFile) throws IOException {
        List<Trip> trips = new ArrayList<Trip>();
        int i = 0;
        GPXTripParser gpxparser = new GPXTripParser();
        for (List<PointFeatures> tripPoints : gpxparser.read(gpxFile, civic)) {
            String driverName = gpxFile.getParentFile().getName();
            String fileName = driverName + gpxFile.getName().substring(0, gpxFile.getName().lastIndexOf('.')) + "_" + i;
            TripFeatures tf = new TripFeatures(driverName, fileName, civic, tripPoints.get(0));
            trips.add(new Trip(tf, tripPoints));
            gpxparser.clear();
            i++;
        }
        return trips;
    }

    static List<File> getGPXFiles(File gpxFolder) {
        List<File> gpxFiles = new ArrayList<File>();
        File[] files = gpxFolder.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                gpxFiles.addAll(getGPXFiles(f));
            } else if (f.isFile() && (f.getAbsolutePath().endsWith("gpx") || f.getAbsolutePath().endsWith("GPX"))) {
                gpxFiles.add(f);
            }
        }
        return gpxFiles;
    }
}
