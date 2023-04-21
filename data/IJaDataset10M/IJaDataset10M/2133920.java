package org.metawb.astro;

import org.metawb.lib.DMS;

/**
 * @author erra
 */
public class Sign {

    private final SignId sid;

    private final double angle;

    public Sign(double longitude) {
        if (longitude > 360.) throw new IllegalArgumentException("Longitude > 360: " + longitude);
        int idx = (int) (longitude / 30.0);
        if (idx > 11) idx = 11;
        sid = SignId.values()[idx];
        double t = longitude - idx * 30;
        if (t < 0) t = 0;
        if (t >= 30.0) t = 30.0;
        angle = t;
    }

    public String getName() {
        return sid.getName();
    }

    public double getAngle() {
        return angle;
    }

    public SignId getSign() {
        return sid;
    }

    public static String getDisplayString(double longitude) {
        return new Sign(longitude).toString();
    }

    @Override
    public String toString() {
        DMS dms = new DMS(angle);
        int secfrac = (int) (dms.getSecFrac() * 10);
        if (secfrac >= 10) secfrac = 9;
        String s = String.format("%2d %2s %02d'%02d.%d", dms.getDegrees(), sid.getAbrev(), dms.getMin(), dms.getSecInt(), secfrac);
        return s;
    }
}
