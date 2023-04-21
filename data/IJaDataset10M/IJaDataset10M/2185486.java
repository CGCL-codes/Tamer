package com.bradsbrain.simpleastronomy;

import java.util.Calendar;

public class SunPosition {

    private double EPOCH = 2447891.5;

    private double ECLIPTIC_LONGITUDE_OF_PERIGREE = 282.768422;

    private double ECLIPTIC_LONGITUDE_AT_EPOCH_1990 = 279.403303;

    private double ECCENTRICITY_OF_ORBIT = 0.016713;

    /**
     * The geocentric ecliptic longitude.  <br>
     * Calculation is good to 3 decimal places <br>
     * me: 337.44406603442917,   book: 337.444194
     */
    private double geoEclipticLongitude = 0;

    /**
     * The mean anomaly
     */
    private double meanAnomaly = 0;

    public SunPosition(Calendar cal) {
        Calendar myCal = BaseUtils.getSafeLocalCopy(cal.getTimeInMillis());
        double daysSince = BaseUtils.exactDaysSince(myCal, EPOCH);
        double N = (360 / 365.242191 * daysSince) % 360;
        if (N < 0) {
            N += 360;
        }
        meanAnomaly = computeMeanAnomaly(N);
        geoEclipticLongitude = computeGeoEclipticLongitude(N);
    }

    private double computeGeoEclipticLongitude(double nValue) {
        double Ec = (360.0 / Math.PI) * ECCENTRICITY_OF_ORBIT * Math.sin(Math.toRadians(meanAnomaly));
        double preliminaryLongitude = nValue + Ec + ECLIPTIC_LONGITUDE_AT_EPOCH_1990;
        if (preliminaryLongitude > 360) {
            preliminaryLongitude -= 360;
        }
        return preliminaryLongitude;
    }

    private double computeMeanAnomaly(double nValue) {
        double someMean = nValue + ECLIPTIC_LONGITUDE_AT_EPOCH_1990 - ECLIPTIC_LONGITUDE_OF_PERIGREE;
        return someMean < 0 ? someMean + 360 : someMean;
    }

    /**
     * TODO: implement this someday
     */
    public RightAscension getRightAscension() {
        return null;
    }

    /**
     * TODO: implement this someday
     */
    public Declination getDeclination() {
        return null;
    }

    public double getEclipticLongitude() {
        return geoEclipticLongitude;
    }

    public double getMeanAnomaly() {
        return meanAnomaly;
    }
}
