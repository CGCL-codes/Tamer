package playground.scnadine.GPSDaten;

import java.util.Iterator;
import java.util.TreeSet;

public class GPSCalcSegmentCharacteristics {

    public static double calcDistance(GPSCoord[] coords) {
        double dist = 0;
        for (int i = 1; i < coords.length; i++) {
            dist = dist + coords[i].getDistanceToPredecessor();
        }
        return dist;
    }

    public static double calcTravelTime(GPSCoord[] coords) {
        double travelTime = (coords[coords.length - 1].getTimestamp().getTimeInMillis() - coords[0].getTimestamp().getTimeInMillis()) / 1000;
        return travelTime;
    }

    public static TreeSet<Double> sortSpeeds(GPSCoord[] Coordinates) {
        TreeSet<Double> speeds = new TreeSet<Double>();
        for (int i = 1; i < Coordinates.length; i++) {
            speeds.add(Math.abs(Coordinates[i].getSpeed()));
        }
        return speeds;
    }

    public static TreeSet<Double> sortAccelerations(GPSCoord[] Coordinates) {
        TreeSet<Double> accs = new TreeSet<Double>();
        for (int i = 1; i < Coordinates.length; i++) {
            accs.add(Math.abs(Coordinates[i].getAcceleration()));
        }
        return accs;
    }

    public static double calcAverage(TreeSet<Double> list) {
        double sum = 0;
        Iterator<Double> it = list.iterator();
        while (it.hasNext()) {
            double nextnumber = (Double) it.next();
            sum = sum + nextnumber;
        }
        return sum / (list.size());
    }

    public static double calcStDeviation(TreeSet<Double> list, double average) {
        double stDev = 0;
        double sum = 0;
        double quadOfSum = 0;
        double variance = 0;
        Iterator<Double> it = list.iterator();
        while (it.hasNext()) {
            double nextnumber = (Double) it.next();
            sum = sum + nextnumber;
        }
        quadOfSum = sum * sum;
        variance = (quadOfSum / list.size()) - (average * average);
        stDev = Math.sqrt(variance);
        return stDev;
    }

    public static double calcPercentile(TreeSet<Double> list, double alpha) {
        Double[] listarray = list.toArray(new Double[list.size()]);
        double perc;
        int n = (int) (list.size() * alpha);
        if (n == 0) perc = list.first(); else perc = listarray[n - 1];
        return perc;
    }
}
