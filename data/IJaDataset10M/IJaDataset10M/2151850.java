package net.sourceforge.chimeralibrary.core.typecast;

/**
 * Manages casting from Object to Double.
 * 
 * @author Christian Cruz
 * @version 1.0.000
 * @since 1.0.000
 */
public class DoublePrimitiveTypeCast extends DoubleTypeCast implements PrimitiveTypeCast<Double> {

    @Override
    public Class<?> getPrimitiveType() {
        return double.class;
    }
}
