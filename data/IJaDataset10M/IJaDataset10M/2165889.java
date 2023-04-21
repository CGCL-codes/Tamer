package dazlyn.geoit.mapper.app;

/**
 *
 * @author matt.tyler
 */
public class FormatUtils {

    public static String formatLongitude(double lon) {
        int hours = Math.abs((int) lon);
        int mins = Math.abs((int) ((Math.abs(lon) - (double) hours) * 60d));
        int secs = Math.abs((int) ((Math.abs(lon) - (double) hours - (double) mins / 60d) * 3600d));
        return new String("" + hours + "° " + mins + "' " + secs + "\" " + (lon < 0d ? "W" : "E"));
    }

    public static String formatLatitude(double lat) {
        int hours = Math.abs((int) lat);
        int mins = Math.abs((int) ((Math.abs(lat) - (double) hours) * 60d));
        int secs = Math.abs((int) ((Math.abs(lat) - (double) hours - (double) mins / 60d) * 3600d));
        return new String("" + hours + "° " + mins + "' " + secs + "\" " + (lat < 0d ? "S" : "N"));
    }

    public static double metersToFeet(double meters) {
        double feet = meters * 3.28084d;
        return feet;
    }

    public static double metersSecondToMilesHour(double mps) {
        double mph = mps * 2.2369362920544d;
        return mph;
    }
}
