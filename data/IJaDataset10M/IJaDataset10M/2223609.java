package org.middleheaven.quantity.convertion;

import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.measure.DecimalMeasure;
import org.middleheaven.quantity.measure.Measurable;
import org.middleheaven.quantity.unit.Unit;

public final class AditiveConverter<E extends Measurable> extends AbstractUnitConverter<E> {

    @SuppressWarnings("unchecked")
    public static <E extends Measurable> AditiveConverter<E> convert(Unit<E> originalUnit, Unit<E> resultUnit, Real shift) {
        return new AditiveConverter(shift, originalUnit, resultUnit);
    }

    private final Real shift;

    private AditiveConverter(Real shift, Unit<E> originalUnit, Unit<E> resultUnit) {
        super(originalUnit, resultUnit);
        this.shift = shift;
    }

    @Override
    public DecimalMeasure<E> convertFoward(DecimalMeasure<E> value) {
        if (!value.unit().equals(this.originalUnit)) {
            throw new IllegalArgumentException("Expected unit " + this.originalUnit + " but was " + value.unit());
        }
        final DecimalMeasure<E> diff = value.one().times(shift, this.resultUnit);
        return value.times(Real.ONE(), this.resultUnit).minus(diff);
    }

    @Override
    public DecimalMeasure<E> convertReverse(DecimalMeasure<E> value) {
        if (!value.unit().equals(this.resultUnit)) {
            throw new IllegalArgumentException("Expected unit " + this.originalUnit + " but was " + value.unit());
        }
        final DecimalMeasure<E> diff = value.one().times(shift, this.originalUnit);
        return value.times(Real.ONE(), this.originalUnit).plus(diff);
    }
}
