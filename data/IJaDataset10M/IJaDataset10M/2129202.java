package org.openstreetmap.travelingsalesman.gps;

import org.openstreetmap.osm.Plugins.IPlugin;

/**
 * This interface is implemented by plugins that can
 * provide the current position of the user('s vehicle).
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 */
public interface IGPSProvider extends IPlugin {

    /**
     * Interface for listeners to get informed about
     * changes of the location of the user.
     * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
     */
    public interface IGPSListener {

        /**
         * The location of the user has changed.
         * @param lat the latitude
         * @param lon the longitude
         */
        void gpsLocationChanged(final double lat, final double lon);

        /**
         * We have no location-fix anymore.
         */
        void gpsLocationLost();

        /**
         * We are have location-fix.
         */
        void gpsLocationObtained();

        /**
         * GPS course over ground has changed.
         * If course can not derive from gps (NMEA) data directly,
         * it should be derived from difference latitude and longitude.
         *
         * @param course Track angle in degrees
         */
        void gpsCourseChanged(final double course);
    }

    /**
     * Interface for listeners to get informed about
     * changes of the location and other GPS data, parsed from various gps streams.
     * @author <a href="mailto:oleg_chubaryov@mail.ru">Oleg Chubaryov</a>
     */
    public interface IExtendedGPSListener extends IGPSListener {

        /**
         * GPS date and time has changed.
         * @param date the date
         * @param time the time
         */
        void gpsDateTimeChanged(final long date, final long time);

        /**
         * GPS fix quality has changed.
         * @param fixQuality 0 - invalid, 1 - GPS fix, 2 - DGPS fix.
         */
        void gpsFixQualityChanged(final int fixQuality);

        /**
         * GPS amount of tracked satellites changed.
         * @param satellites new amount of used / tracked satellites.
         */
        void gpsUsedSattelitesChanged(final int satellites);

        /**
         * GPS altitude has changed.
         * @param altitude new altitude in meters.
         */
        void gpsAltitudeChanged(final double altitude);

        /**
         * GPS Dilution of precision has changed.
         * @param hdop new horizontal dilution of precision
         * @param vdop new vertical dilution of precision
         * @param pdop new position dilution of precision
         */
        void gpsDopChanged(final double hdop, final double vdop, final double pdop);

        /**
         * GPS Speed over ground has changed.
         * @param speed new speed value in knots.
         */
        void gpsSpeedChanged(final double speed);
    }

    /**
     * Add a listener to get informed about
     * changes of the location of the user.
     * @param listener the observer
     */
    void addGPSListener(final IGPSListener listener);

    /**
     * Remove a listener from informed about changes.
     * @param listener the observer to remove
     */
    void removeGPSListener(final IGPSListener listener);
}
