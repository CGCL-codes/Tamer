package org.geoforge.worldwind._tempo.projection.conical;

import org.geoforge.worldwind._tempo.ellipsoids.ElpClarke1880;
import org.geoforge.worldwind._tempo.maths.BigMath;
import java.math.BigDecimal;

/**
 *
 * @author Amadeus.Sowerby
 *
 * email: Amadeus.Sowerby_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 */
public class PrjConLambert3 extends PrjConAbs {

    private PrjConLambert3() {
        super(2154, ElpClarke1880.s_getInstance(), BigMath.degToRad(new BigDecimal("44.1"), PrjConAbs.SCALE), BigMath.degToRad(new BigDecimal("43.19929139"), PrjConAbs.SCALE), BigMath.degToRad(new BigDecimal("44.99309389"), PrjConAbs.SCALE), BigMath.degToRad(BigMath.ddecToDms(2, 20, 14.025D, PrjConAbs.SCALE), PrjConAbs.SCALE), new BigDecimal("600000"), new BigDecimal("200000"), PrjConAbs.SCALE);
    }

    public static PrjConLambert3 s_getInstance() {
        if (_INSTANCE_ == null) {
            _INSTANCE_ = new PrjConLambert3();
        }
        return _INSTANCE_;
    }

    public static void s_destroyInstance() {
        _INSTANCE_ = null;
    }

    private static PrjConLambert3 _INSTANCE_ = null;
}
