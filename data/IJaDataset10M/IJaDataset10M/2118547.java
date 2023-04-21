package fi.hip.gb.bluetooth.coordconv;

import fi.hip.gb.midlet.util.Float;

/**
 * An object to represent a latitude and longitude pair
 * 
 * @author Jonathan Stott
 * @version 0.2
 * @since 0.1
 */
public class LatitudeLongitude {

    private int dLong;

    private int mLong;

    private Float sLong;

    private int dLat;

    private int mLat;

    private Float sLat;

    /**
   * Construct a latitude and longitude pair
   * 
   * @param degreesN degrees of latitude
   * @param minutesN minutes of latitude
   * @param secondsN seconds of latitude
   * @param degreesE degrees of longitude
   * @param minutesE minutes of longitude
   * @param secondsE seconds of longitude
   */
    public LatitudeLongitude(int degreesN, int minutesN, Float secondsN, int degreesE, int minutesE, Float secondsE) {
        dLong = degreesE;
        mLong = minutesE;
        sLong = secondsE;
        dLat = degreesN;
        mLat = minutesN;
        sLat = secondsN;
    }

    /**
   * Get the latitude
   * 
   * @return the latitude
   */
    public String[] getLatitude() {
        return new String[] { Integer.toString(dLat), Integer.toString(mLat), sLat.toString() };
    }

    /**
   * Get the longitude
   * 
   * @return the longitude
   */
    public String[] getLongitude() {
        return new String[] { Integer.toString(dLong), Integer.toString(mLong), sLong.toString() };
    }

    /**
   * Gets a string representation of the latitude in the form
   * 52d39m27.2531s (52�39'27.2531")
   * @return string presentation of the latitude
   */
    public String getMinSec(String[] arr) {
        return arr[0] + "d" + arr[1] + "m" + arr[2] + "s";
    }

    /**
   * Gets the north or south side of latitude.
   * @return S or N depending on the current latitude
   */
    public String getLatitudeNS() {
        if (dLat < 0) {
            return "S";
        }
        return "N";
    }

    /**
   * Gets the east or west side of longitude.
   * @return E or W depending on the current longitude
   */
    public String getLongitudeEW() {
        if (dLong < 0) {
            return "W";
        }
        return "E";
    }

    /**
   * Get a string representation of the latitude and longitude in the form
   * 52d39m27.2531sN 1d43m4.5177sE
   * 
   * @return string presentation of the latitude and longitude
   */
    public String toString() {
        return getMinSec(getLatitude()) + getLatitudeNS() + " " + getMinSec(getLongitude()) + getLongitudeEW();
    }
}
