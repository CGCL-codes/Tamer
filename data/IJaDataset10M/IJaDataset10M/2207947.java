package org.genxdm.processor.xpath.v10.variants;

import org.genxdm.xpath.v10.Converter;

/**
 * a boolean which can provide its value as a String, Number or Object
 */
public final class BooleanVariant<N> extends VariantBase<N> {

    private final boolean b;

    public BooleanVariant(final boolean b) {
        this.b = b;
    }

    public String convertToString() {
        return Converter.toString(b);
    }

    public boolean convertToBoolean() {
        return b;
    }

    @Override
    public double convertToNumber() {
        return Converter.toNumber(b);
    }

    @Override
    public boolean isBoolean() {
        return true;
    }
}
