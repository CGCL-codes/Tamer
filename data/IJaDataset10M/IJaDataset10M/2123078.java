package org.dasein.util.uom.area;

import org.dasein.util.uom.UnitOfMeasure;
import org.dasein.util.uom.UnknownUnitOfMeasure;
import javax.annotation.Nonnull;

public abstract class AreaUnit extends UnitOfMeasure {

    @Nonnull
    public static AreaUnit valueOf(@Nonnull String uom) {
        if (uom.length() < 1 || uom.equals("m") || uom.equals("meter") || uom.equals("meters") || uom.equals("metre") || uom.equals("metres")) {
            return Area.METER_SQUARED;
        }
        throw new UnknownUnitOfMeasure(uom);
    }
}
