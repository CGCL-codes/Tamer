package astroLib;

/**
 *
 * @author mgeden
 */
public class EarthPosition {

    private double mLatitude;

    private double mLongitude;

    private int mAltitude, mTemperature, mPressure;

    public EarthPosition(double latitude, double longitude) {
        this(latitude, longitude, 0, 10, 1010);
    }

    public EarthPosition(double latitude, double longitude, int altitude, int temperature, int pressure) {
        mLatitude = latitude;
        mLongitude = longitude;
        mTemperature = temperature;
        mPressure = pressure;
        mAltitude = altitude;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public short getAltitude() {
        return (short) mAltitude;
    }

    public short getPressure() {
        return (short) mPressure;
    }

    public short getTemperature() {
        return (short) mTemperature;
    }

    public EarthHeading toEarthHeading(EarthPosition target) {
        double radPerDeg = Math.PI / 180;
        double lat1 = Math.toRadians(mLatitude);
        double lat2 = Math.toRadians(target.getLatitude());
        double lon1 = Math.toRadians(-mLongitude);
        double lon2 = Math.toRadians(-target.getLongitude());
        double a = Math.sin((lat1 - lat2) / 2);
        double b = Math.sin((lon1 - lon2) / 2);
        double d = 2 * MATH.asin(Math.sqrt(a * a + Math.cos(lat1) * Math.cos(lat2) * b * b));
        double tc1 = 0;
        if (d > 0) if ((Math.sin(lon2 - lon1)) < 0) tc1 = MATH.acos((Math.sin(lat2) - Math.sin(lat1) * Math.cos(d)) / (Math.sin(d) * Math.cos(lat1))); else tc1 = 2 * Math.PI - MATH.acos((Math.sin(lat2) - Math.sin(lat1) * Math.cos(d)) / (Math.sin(d) * Math.cos(lat1)));
        return new EarthHeading((tc1 / radPerDeg), (long) (d * 6371000));
    }
}
