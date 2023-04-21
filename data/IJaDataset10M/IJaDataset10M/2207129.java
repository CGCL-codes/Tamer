package org.gps.types;

/**
 * Array of all GPS satellites in orbit.
 * Keeps track of all the satellites in orbit and which of these can be seen
 * by the GPS device as well as which satellites that are used for the current
 * fix. This array starts counting at 1 instead of 0 as is the norm for other
 * arrays. This is so that the array index is consistent with the PRN
 * identificator of each satellite.
 */
public class GpsSatelliteArray {

    /**
     * The maximum number of satellites orbiting the earth.
     */
    public static final int MAX_NO_OF_SATELLITES = 32;

    private GpsSatellite[] satellites = new GpsSatellite[MAX_NO_OF_SATELLITES];

    /**
     * Create a new array of satellites.
     * None of the satellites are initially used.
     */
    public GpsSatelliteArray() {
        for (int i = 1; i <= MAX_NO_OF_SATELLITES; i++) {
            GpsSatellite sat = new GpsSatellite();
            String PRN = new Integer(i).toString();
            if (i < 10) {
                PRN = "0" + PRN;
            }
            sat.setPRN(PRN);
            add(i, sat);
        }
    }

    /**
     * Set the "in use" status for the satellite with the given PRN number.
     * This status is used to tell if this satellite is being used for the
     * current GPS fix or not.
     * @param status    True if this satellite is used.
     * @param PRN       The integer equivalent to the satellites PRN number ("07" equals 7)
     */
    public void setUseStatus(int PRN, boolean status) {
        GpsSatellite sat = (GpsSatellite) get(PRN);
        sat.setInUse(status);
        add(PRN, sat);
    }

    /**
     * Set the "in use" status for all the satellites in the array.
     * This status is used to tell if the satellites are being used for the
     * current GPS fix or not. This method should not be called with a true
     * value as it should be used to reset the array. All the satellites in an
     * array can never, at the same time, be used for a GPS fix.
     * @param status    False to set all satellites as not being used.
     */
    public void setUseStatusAll(boolean status) {
        for (int i = 1; i <= MAX_NO_OF_SATELLITES; i++) {
            GpsSatellite sat = (GpsSatellite) get(i);
            sat.setInUse(status);
            add(i, sat);
        }
    }

    /**
     * Set the availability status for the satellite with the given PRN number.
     * This status is used to tell if this satellite can be seen by the GPS
     * device or not.
     * @param status    True if this satellite is visible.
     * @param PRN       The integer equivalent to the satellites PRN number ("07" equals 7)
     */
    public void setAvailableStatus(int PRN, boolean status) {
        GpsSatellite sat = (GpsSatellite) get(PRN);
        sat.setAvailable(status);
        add(PRN, sat);
    }

    /**
     * Set the availability status for all the satellites in the array.
     * This status is used to tell if the satellites can be seen by the GPS
     * device or not. This method should not be called with a true
     * value as it should be used to reset the array. All the satellites in an
     * array can never, at the same time, be seen by a GPS device
     * @param status    False to set all satellites as not visible.
     */
    public void setAvailabeStatusAll(boolean status) {
        for (int i = 1; i <= MAX_NO_OF_SATELLITES; i++) {
            GpsSatellite sat = (GpsSatellite) get(i);
            sat.setAvailable(status);
            add(i, sat);
        }
    }

    /**
     * Add/Update new satellite data to the array
     * @param element   The satellite data to add/update
     * @param index     The integer equivalent to the satellites PRN number ("07" equals 7)
     */
    public void add(int index, GpsSatellite element) {
        satellites[index - 1] = element;
    }

    /**
     * Get the satellite data for the requested satellite
     * @return          Requested data
     * @param index     The integer equivalent to the satellites PRN number ("07" equals 7)
     */
    public GpsSatellite get(int index) {
        return satellites[index - 1];
    }
}
