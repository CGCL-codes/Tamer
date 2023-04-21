package com.github.ignition.location.templates;

import android.location.Location;
import android.location.LocationListener;

/**
 * Interface definition for a Last Location Finder.
 * 
 * Classes that implement this interface must provide methods to find the "best" (most accurate and
 * timely) previously detected location using whatever providers are available.
 * 
 * Where a timely / accurate previous location is not detected, classes should return the last
 * location and create a one-shot update to find the current location. The one-shot update should be
 * returned via the Location Listener passed in through setChangedLocationListener.
 */
public interface ILastLocationFinder {

    static final String LOG_TAG = "IgnitedLastLocationFinder";

    static final String LAST_LOCATION_TOO_OLD_OR_INACCURATE_EXTRA = "last_location_too_old_or_inaccurate_extra";

    /**
     * Find the most accurate and timely previously detected location using all the location
     * providers. Where the last result is beyond the acceptable maximum distance or latency create
     * a one-shot update of the current location to be returned using the {@link LocationListener}
     * passed in through {@link setChangedLocationListener}
     * 
     * @param minDistance
     *            Minimum distance before we require a location update.
     * @param minTime
     *            Minimum time required between location updates.
     * @param passiveRequest
     *            The method is invoked from a passive location updater.
     * @return The most accurate and / or timely previously detected location.
     */
    Location getLastBestLocation(int minDistance, long minTime, boolean passiveRequest);

    /**
     * Cancel the one-shot current location update.
     */
    void cancel();

    /**
     * Retrieve a one-shot location update.
     */
    void retrieveSingleLocationUpdate();
}
