package org.chargecar.prize.visualization;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.chargecar.prize.battery.BatteryModel;
import org.chargecar.prize.util.DriverResults;
import org.chargecar.prize.util.PointFeatures;
import org.chargecar.prize.util.SimulationResults;
import org.chargecar.prize.util.Trip;

/**
 * DO NOT EDIT
 * 
 * A simple visualizer that outputs simulation details to the console screen.
 * 
 * @author Alex Styler
 */
public class CSVWriter implements Visualizer {

    private final DecimalFormat d = new DecimalFormat("0.000E0");

    private final DecimalFormat p = new DecimalFormat("0.000%");

    private final String filename;

    public CSVWriter(String filename) {
        this.filename = filename;
    }

    public void visualizeSummary(List<SimulationResults> results) {
        FileWriter fstream;
        try {
            fstream = new FileWriter(filename);
            BufferedWriter out = new BufferedWriter(fstream);
            for (SimulationResults r : results) {
                double currentSquaredSum = 0.0;
                for (Double d : r.getBatteryCurrentSquaredIntegrals()) {
                    currentSquaredSum += d;
                    out.write(currentSquaredSum + ",");
                }
                out.write("0.0\n");
                double chargeSpentSum = 0.0;
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeTripPowers(List<PointFeatures> pfs) {
        FileWriter fstream;
        try {
            fstream = new FileWriter(filename);
            BufferedWriter out = new BufferedWriter(fstream);
            for (PointFeatures pf : pfs) {
                out.write(pf.getTime().getTimeInMillis() / 1000 + ",");
                out.write(pf.getSpeed() + ",");
                out.write(pf.getPowerDemand() + "\n");
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void visualizeTrip(Trip trip, BatteryModel battery, BatteryModel capacitor) {
        System.out.println(trip);
        System.out.println("Integral of current squared: " + d.format(battery.getCurrentSquaredIntegral()));
    }

    public void visualizeTrips(SimulationResults simResults) {
    }

    @Override
    public void visualizeDrivers(List<DriverResults> driverResults) {
    }

    public void writeBatteryPowers(BatteryModel tripBattery) {
        FileWriter fstream;
        try {
            File tfile = new File(filename);
            tfile.getParentFile().mkdirs();
            fstream = new FileWriter(tfile);
            BufferedWriter out = new BufferedWriter(fstream);
            for (int i = 0; i < tripBattery.getCurrentDrawHistory().size(); i++) {
                double current = tripBattery.getCurrentDrawHistory().get(i);
                double time = (double) tripBattery.getPeriodHistory().get(i) / 1000.0;
                double power = current * tripBattery.getVoltage();
                out.write(time + ",");
                out.write(power + "\n");
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
