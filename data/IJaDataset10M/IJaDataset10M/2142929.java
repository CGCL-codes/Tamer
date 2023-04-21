package com.PrayTime.HijriCalendar;

/**
 * 
 * @author http://www.cepmuvakkit.com
 */
public class NewMoon extends MoonPhases {

    EclipticPosition eclipPos = new EclipticPosition();

    @Override
    public double calculatePhase(double T) {
        double tau_Sun = 8.32 / (1440.0 * 36525.0);
        double beta, l_Moon, l_Sun;
        double[] moonLongLat;
        double pi2 = 2 * Math.PI;
        l_Sun = EclipticPosition.getMiniSunLongitude(T - tau_Sun);
        moonLongLat = EclipticPosition.getMiniMoon(T);
        l_Moon = moonLongLat[0];
        beta = moonLongLat[1];
        double LongDiff = l_Moon - l_Sun;
        return Modulo(LongDiff + Math.PI, pi2) - Math.PI;
    }

    private double Modulo(double x, double y) {
        return y * Frac(x / y);
    }

    private double Frac(double x) {
        return x - Math.floor(x);
    }
}
