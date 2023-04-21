package org.geoforge.worldwind._tempo.ellipsoids;

import java.math.BigDecimal;

/**
 *
 * @author Amadeus.Sowerby
 *
 * email: Amadeus.Sowerby_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 */
public class ElpWSG84 extends ElpAbs {

    public ElpWSG84() {
        super("WGS84", new BigDecimal("6378137"), new BigDecimal(1D / 298.257223563D), SCALE);
    }

    public static ElpWSG84 s_getInstance() {
        if (_INSTANCE_ == null) _INSTANCE_ = new ElpWSG84();
        return _INSTANCE_;
    }

    public static void s_destroyInstance() {
        _INSTANCE_ = null;
    }

    private static ElpWSG84 _INSTANCE_ = null;
}
