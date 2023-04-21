package net.sourceforge.magex.mobile.astronomic;

import net.sourceforge.magex.mobile.Texts;
import net.sourceforge.magex.mobile.utils.*;

public class MoonEquations {

    /** Moon phase: dark moon. value = 0. */
    public static final int PHASE_DARK_MOON = 0;

    /** Moon phase: waxing crescent moon. value = 1. */
    public static final int PHASE_WAXING_CRESCENT = 1;

    /** Moon phase: first quarter moon. value = 2. */
    public static final int PHASE_FIRST_QUARTER = 2;

    /** Moon phase: waxing gibbous moon. value = 3. */
    public static final int PHASE_WAXING_GIBBOUS = 3;

    /** Moon phase: full moon. value = 4. */
    public static final int PHASE_FULL_MOON = 4;

    /** Moon phase: waning gibbous moon. value = 5. */
    public static final int PHASE_WANING_GIBBOUS = 5;

    /** Moon phase: third quarter moon. value = 6. */
    public static final int PHASE_THIRD_QUARTER = 6;

    /** Moon phase: . waning crescent moon. value = 7. */
    public static final int PHASE_WANING_CRESCENT = 7;

    /**
     * Creates a new instance of MoonEquations.
     * Empty constructor.
     */
    public MoonEquations() {
    }

    /**
     * Counts Moon phase.
     * @param julianDay Actual Julian day.
     * @return Moon phase as number feom 0.0 to 1.0.
     */
    public static double getPhase(double julianDay) {
        double ph = (julianDay - 1721088.5) / 29.53059 + 0.25;
        double ph1 = Math.floor(ph);
        ph = ph - ph1;
        return ph;
    }

    /**
     * Counts Moon phase.
     * @param julianDay Actual Julian day.
     * @return One of eight phases. See phase constants.
     */
    public static int getNumPhase(double julianDay) {
        double phase = getPhase(julianDay);
        double phase_ = phase + 0.0625;
        if (phase < 0.125) {
            return PHASE_DARK_MOON;
        } else if (phase_ < 0.25) {
            return PHASE_WAXING_CRESCENT;
        } else if (phase_ < 0.375) {
            return PHASE_FIRST_QUARTER;
        } else if (phase_ < 0.5) {
            return PHASE_WAXING_GIBBOUS;
        } else if (phase_ < 0.625) {
            return PHASE_FULL_MOON;
        } else if (phase_ < 0.75) {
            return PHASE_WANING_GIBBOUS;
        } else if (phase_ < 0.875) {
            return PHASE_THIRD_QUARTER;
        } else {
            return PHASE_WANING_CRESCENT;
        }
    }

    /**
     * @param phase Phase of the moon. See phase constants.
     * @return Name of given phase.
     */
    public static String getPhaseName(int phase) {
        switch(phase) {
            case PHASE_DARK_MOON:
                {
                    return Texts.MOON_PHASE_DARK;
                }
            case PHASE_WAXING_CRESCENT:
                {
                    return Texts.MOON_PHASE_WAX_CRES;
                }
            case PHASE_FIRST_QUARTER:
                {
                    return Texts.MOON_PHASE_FIRST_Q;
                }
            case PHASE_WAXING_GIBBOUS:
                {
                    return Texts.MOON_PHASE_WAX_GIB;
                }
            case PHASE_FULL_MOON:
                {
                    return Texts.MOON_PHASE_FULL;
                }
            case PHASE_WANING_GIBBOUS:
                {
                    return Texts.MOON_PHASE_WAN_GIB;
                }
            case PHASE_THIRD_QUARTER:
                {
                    return Texts.MOON_PHASE_THIRD_Q;
                }
            case PHASE_WANING_CRESCENT:
                {
                    return Texts.MOON_PHASE_WAN_CRES;
                }
            default:
                return Texts.UNI_NONE;
        }
    }

    /**
     * Counts ecliptical length of the Moon.
     * @param julianCenturies Number of Julian centuries (from Julian Day).
     * @return Ecliptical length of the Moon.
     */
    private static double eclipticalLength(double julianCenturies) {
        double rad = Math.PI / 180.;
        return 218.3166544 + 481267.8813 * julianCenturies + 6.29 * Math.sin((134.9 + 477198.85 * julianCenturies) * rad) - 1.27 * Math.sin((259.2 - 413335.38 * julianCenturies) * rad) + 0.66 * Math.sin((235.7 + 890534.23 * julianCenturies) * rad) + 0.21 * Math.sin((269.9 + 954397.7 * julianCenturies) * rad) - 0.19 * Math.sin((357.5 + 35999.05 * julianCenturies) * rad) - 0.11 * Math.sin((186.6 + 966404.05 * julianCenturies) * rad);
    }

    /**
     * Counts ecliptical width of the Moon.
     * @param julianCenturies Number of Julian centuries (from Julian Day).
     * @return Ecliptical width of the Moon.
     */
    private static double eclipticalWidth(double julianCenturies) {
        double rad = Math.PI / 180.;
        return 5.13 * Math.sin((93.3 + 483202.03 * julianCenturies) * rad) + 0.28 * Math.sin((228.2 + 960400.87 * julianCenturies) * rad) - 0.28 * Math.sin((318.3 + 6003.18 * julianCenturies) * rad) - 0.17 * Math.sin((217.6 - 407332.2 * julianCenturies) * rad);
    }

    /**
     * Direction cosine.
     * @param julianCenturies
     * @return Direction cosine.
     */
    private static double l(double julianCenturies) {
        double rad = Math.PI / 180.;
        double beta = eclipticalWidth(julianCenturies);
        double lambda = eclipticalLength(julianCenturies);
        return Math.cos(beta * rad) * Math.cos(lambda * rad);
    }

    /**
     * 
     * @param julianCenturies
     * @return Direction cosine.
     */
    private static double m(double julianCenturies) {
        double rad = Math.PI / 180.;
        double beta = eclipticalWidth(julianCenturies);
        double lambda = eclipticalLength(julianCenturies);
        return 0.9175 * Math.cos(beta * rad) * Math.sin(lambda * rad) - 0.3978 * Math.sin(beta * rad);
    }

    /**
     * 
     * @param julianCenturies
     * @return Direction cosine.
     */
    private static double n(double julianCenturies) {
        double rad = Math.PI / 180.;
        double beta = eclipticalWidth(julianCenturies);
        double lambda = eclipticalLength(julianCenturies);
        return 0.3978 * Math.cos(beta * rad) * Math.sin(lambda * rad) + 0.9175 * Math.sin(beta * rad);
    }

    /**
     * Counts declination of the Moon.
     * @param julianCenturies
     * @return declination of the Moon
     */
    private static double declination(double julianCenturies) {
        return Cyclometric.asin(n(julianCenturies)) * 180 / Math.PI;
    }

    /**
     * Counts right ascession of the Moon.
     * @param julianCenturies
     * @return Right ascession of the Moon.
     */
    private static double rightAscession(double julianCenturies) {
        double l = l(julianCenturies);
        double m = m(julianCenturies);
        double alpha = Cyclometric.arctan(m / l) * 180 / Math.PI;
        if (l < 0 && m > 0) {
            alpha += 180;
        }
        if (l > 0 && m < 0) {
            alpha += 360;
        }
        if (l > 0 && m > 0) {
            alpha += 0;
        }
        if (l < 0 && m < 0) {
            alpha += 180;
        }
        alpha /= 15;
        alpha = Geographic.toRange(alpha, 24);
        return alpha;
    }

    /**
     * Counts hour angle of the Moon.
     * @param longitude
     * @param julianDay
     * @return Hour angle of the Moon.
     */
    private static double getHA(double longitude, double julianDay) {
        double UT = ((julianDay + 0.5) - Math.floor(julianDay + 0.5)) * 24;
        double T = Times.SJD(julianDay);
        double HA = (Times.S0(julianDay) + UT - rightAscession(T)) * 15 + longitude;
        return Geographic.toRange(HA, 360);
    }

    /**
     * Counts azimuth of the Moon.
     * @param longitude
     * @param latitude
     * @param julianDay
     * @return Azimuth of the Moon.
     */
    public static double getAzimuth(double longitude, double latitude, double julianDay) {
        double HA = getHA(longitude, julianDay);
        double T = Times.SJD(julianDay);
        double rad = Math.PI / 180.;
        double denom = (Math.sin(declination(T) * rad) * Math.cos(latitude * rad)) - (Math.cos(declination(T) * rad) * Math.cos(HA * rad) * Math.sin(latitude * rad));
        double az = Cyclometric.arctan(-(Math.sin(HA * rad) * Math.cos(declination(T) * rad)) / denom) * 180. / Math.PI;
        if (denom < 0) {
            az += 180;
        }
        return Geographic.toRange(az, 360);
    }

    /**
     * Counts altitude of the Moon.
     * @param longitude
     * @param latitude
     * @param julianDay
     * @return Altitude of the Moon.
     */
    public static double getAltitude(double longitude, double latitude, double julianDay) {
        double HA = getHA(longitude, julianDay);
        double T = Times.SJD(julianDay);
        double rad = Math.PI / 180.;
        double alt = Cyclometric.asin(Math.sin(latitude * rad) * Math.sin(declination(T) * rad) + Math.cos(latitude * rad) * Math.cos(declination(T) * rad) * (Math.cos(HA * rad))) * 180. / Math.PI;
        return alt;
    }

    /**
     * Counts rise, transit and set of Moon.
     * @param longitude
     * @param latitude
     * @param h 
     * @param a If rise, transit or set required.
     * @return When the required time comes.
     */
    public static double getRTS(double longitude, double latitude, double h, int a) {
        Times t = new Times();
        double JD = t.getJD2();
        double T = t.SJD(JD - 1);
        double Alpha1 = rightAscession(T);
        double Delta1 = declination(T);
        T = t.SJD(JD);
        double Alpha2 = rightAscession(T);
        double Delta2 = declination(T);
        T = t.SJD(JD + 1);
        double Alpha3 = rightAscession(T);
        double Delta3 = declination(T);
        RTS.RiseTransitSetDetails details = RTS.Rise(JD, Alpha1, Delta1, Alpha2, Delta2, Alpha3, Delta3, longitude, latitude, h);
        if (a == 1) {
            return details.Rise;
        }
        if (a == 2) {
            return details.Transit;
        }
        if (a == 3) {
            return details.Set;
        }
        return -1;
    }
}
