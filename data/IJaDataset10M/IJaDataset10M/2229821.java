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
public class PrjConLambertIV_Carto extends PrjConAbs {

    private PrjConLambertIV_Carto() {
        super(27584, ElpClarke1880.s_getInstance(), BigMath.degToRad(new BigDecimal("42.165"), PrjConAbs.SCALE), BigMath.degToRad(new BigDecimal("41.5603877777777777777777"), PrjConAbs.SCALE), BigMath.degToRad(BigMath.ddecToDms(42, 46, 0.3588D, PrjConAbs.SCALE), PrjConAbs.SCALE), BigMath.degToRad(BigMath.ddecToDms(2, 20, 14.025D, PrjConAbs.SCALE), PrjConAbs.SCALE), new BigDecimal("234.358"), new BigDecimal("4185861.369"), PrjConAbs.SCALE);
    }

    public static PrjConLambertIV_Carto s_getInstance() {
        if (_INSTANCE_ == null) _INSTANCE_ = new PrjConLambertIV_Carto();
        return _INSTANCE_;
    }

    public static void s_destroyInstance() {
        _INSTANCE_ = null;
    }

    private static PrjConLambertIV_Carto _INSTANCE_ = null;
}
