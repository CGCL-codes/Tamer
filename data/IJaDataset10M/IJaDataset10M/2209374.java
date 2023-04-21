package net.walkingtools.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;
import net.walkingtools.gpsTypes.Coordinates;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author Brett Stalbaum
 * @version 0.1.1
 *
 */
public class Utilities {

    /**
	 * Method to read text files in the android asset directory into a String
	 * @param am the AssetManager instance from the Activity context (use getAssets())
	 * @param assetPath the path within the assets directory
	 * @return a string containing the file
	 * @throws IOException 
	 */
    public static String readTextAsset(AssetManager am, String assetPath) throws IOException {
        InputStream in = null;
        in = am.open(assetPath);
        StringBuffer buf = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String temp = null;
        while ((temp = reader.readLine()) != null) {
            buf.append(temp);
        }
        return buf.toString();
    }

    /**
     * Searches the Coordinates[] for the waypoint closest to the second parameter
     * Returns element 0 of wpts if no closer can be found or there is only one
     * element in the array.
     * @param wpts the array of Coordinates to search
     * @param coords the coordinates to search for closest to
     * @return the closest Coordinate in wpts
     */
    public static Coordinates findClosestWaypoint(Coordinates[] wpts, Coordinates coords) {
        return wpts[findClosestWaypointIndex(wpts, coords)];
    }

    /**
     * Searches the Coordinates[] for the waypoint closest to the second parameter
     * returns sentinel value -1 if no closest was found (case if wpts has 0 elements.)
     * @param wpts the array of Coordinates to search
     * @param coords the coordinates to search for closest to
     * @return the index into wpts of the closest waypoint to current
     */
    public static int findClosestWaypointIndex(Coordinates[] wpts, Coordinates coords) {
        int closestIndex = -1;
        double distance = 999999999;
        for (int i = 0; i < wpts.length; i++) {
            double tmpDis = getSimpleDistance(wpts[i], coords);
            if (tmpDis < distance) {
                distance = tmpDis;
                closestIndex = i;
            }
        }
        return closestIndex;
    }

    /**
     * Searches the Coordinates[] for the waypoint closest to the second parameter
     * returns index 0 of wpts if there are no other matches.
     * Exclusion is based on the indexes into wpts found in int[] exclude.
     * @param wpts the array of Coordinates to search
     * @param coords Coordinates to search for closest to
     * @param exclude excludes consideration of the indexes
     * @return the Coordinates of the closest waypoint to current
     */
    public static Coordinates findClosestWaypoint(Coordinates[] wpts, Coordinates coords, Vector<Integer> exclude) {
        return wpts[findClosestWaypointIndex(wpts, coords, exclude)];
    }

    /**
     * Searches the Coordinates[]  for the waypoint closest to the second parameter
     * returns sentinel value -1 if no closest was found (perhaps all are excluded or
     * wpts has 0 elements). Exclusion is based on the indexes into wpts found in int[] exclude.
     * @param wpts the array of Coordinates to search
     * @param coords Coordinates to search for closest to
     * @param exclude excludes consideration of the indexes into wpts contained in int[] exclude
     * @return the index into wpts of the closest waypoint to current
     */
    public static int findClosestWaypointIndex(Coordinates[] wpts, Coordinates coords, Vector<Integer> exclude) {
        int closestIndex = -1;
        double distance = 999999999;
        search: for (int i = 0; i < wpts.length; i++) {
            for (int j = 0; j < exclude.size(); j++) {
                int ex = exclude.get(j);
                if (ex == i) {
                    continue search;
                }
            }
            double tmpDis = getSimpleDistance(wpts[i], coords);
            if (tmpDis < distance) {
                distance = tmpDis;
                closestIndex = i;
            }
        }
        return closestIndex;
    }

    /**
     * Computes the distance in Meters between two Coordinates. This is a fast, simple method
     * using basic trigonometry.
     * @param from the start Coordinates
     * @param to the to Coordinates
     * @return the distance in meters
     * 
     */
    public static double getSimpleDistance(Coordinates from, Coordinates to) {
        double fromLatRads = Math.toRadians(from.getLatitude());
        double toLatRads = Math.toRadians(to.getLatitude());
        double deltaLonRads = Math.toRadians(to.getLongitude() - from.getLongitude());
        return Math.acos(Math.sin(fromLatRads) * Math.sin(toLatRads) + Math.cos(fromLatRads) * Math.cos(toLatRads) * Math.cos(deltaLonRads)) * 6371000;
    }

    /** Allows any activity to check the status of the network
	 * @param activity the calling Activity
	 * @return true if the network is available
	 * @since 0.1.1
	 */
    public static boolean networkIsAvailable(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null;
    }

    /**
	 * Gets estimate of the text width of a single character for a given paint in pixels.
	 * Based on the letter "B".
	 * @param paint
	 * @return a good guess a the text width of a single in pixels
	 */
    public static float calculateTextSize(Paint paint) {
        float[] test = { 0 };
        paint.getTextWidths("B", test);
        return test[0];
    }
}
