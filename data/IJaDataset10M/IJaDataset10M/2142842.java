package org.geoforge.worldwind._tempo.projection.conical;

import org.geoforge.worldwind._tempo.ellipsoids.ElpWSG84;
import org.geoforge.worldwind._tempo.maths.BigMath;
import java.math.BigDecimal;

/**
 *
 * @author Amadeus.Sowerby
 *
 * email: Amadeus.Sowerby_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 */
public class PrjConLambertMexico extends PrjConAbs {

    private PrjConLambertMexico() {
        super(39, ElpWSG84.s_getInstance(), BigMath.degToRad(new BigDecimal("12"), PrjConAbs.SCALE), BigMath.degToRad(new BigDecimal("17.5"), PrjConAbs.SCALE), BigMath.degToRad(new BigDecimal("29.5"), PrjConAbs.SCALE), BigMath.degToRad(new BigDecimal("-102"), PrjConAbs.SCALE), new BigDecimal("2500000"), new BigDecimal("0"), PrjConAbs.SCALE);
    }

    public static PrjConLambertMexico s_getInstance() {
        if (_INSTANCE_ == null) {
            _INSTANCE_ = new PrjConLambertMexico();
        }
        return _INSTANCE_;
    }

    public static void s_destroyInstance() {
        _INSTANCE_ = null;
    }

    private static PrjConLambertMexico _INSTANCE_ = null;
}
