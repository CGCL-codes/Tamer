package com.github.ignition.location.templates;

import android.app.PendingIntent;
import android.location.Criteria;
import android.location.LocationManager;

/**
 * Abstract base class that can be extended to provide active and passive location updates optimized
 * for each platform release.
 * 
 * Uses broadcast Intents to notify the app of location changes.
 */
public abstract class IgnitedAbstractLocationUpdateRequester {

    protected static final String TAG = "IgnitedLocationUpdateRequester";

    protected LocationManager locationManager;

    private PendingIntent locationUpdatesPendingIntent;

    protected IgnitedAbstractLocationUpdateRequester(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    /**
     * Request active location updates. These updates will be triggered by a direct request from the
     * Location Manager.
     * 
     * @param minTime
     *            Minimum time that should elapse between location update broadcasts.
     * @param minDistance
     *            Minimum distance that should have been moved between location update broadcasts.
     * @param criteria
     *            Criteria that define the Location Provider to use to detect the Location.
     * @param pendingIntent
     *            The Pending Intent to broadcast to notify the app of active location changes.
     */
    public void requestLocationUpdates(long minTime, long minDistance, Criteria criteria, PendingIntent pendingIntent) {
        this.locationUpdatesPendingIntent = pendingIntent;
    }

    public void removeLocationUpdates() {
        locationManager.removeUpdates(locationUpdatesPendingIntent);
    }

    /**
     * Request passive location updates. These updates will be triggered by locations received by
     * 3rd party apps that have requested location updates. The miniumim time and distance for
     * passive updates will typically be longer than for active updates. The trick is to balance the
     * difference to minimize battery drain by maximize freshness.
     * 
     * @param minTime
     *            Minimum time that should elapse between location update broadcasts.
     * @param minDistance
     *            Minimum distance that should have been moved between location update broadcasts.
     * @param pendingIntent
     *            The Pending Intent to broadcast to notify the app of passive location changes.
     */
    public void requestPassiveLocationUpdates(long minTime, long minDistance, PendingIntent pendingIntent) {
    }
}
