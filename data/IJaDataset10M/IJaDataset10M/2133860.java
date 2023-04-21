package com.anodyzed.onyx.type;

import java.math.BigDecimal;

public class BigDecimalDoubleConverter implements Converter {

    /**
   * Convert BigDecimals to/from Double's
   *
   * @param from Either a BigDecimal or a Double 
   * @param to The type to convert to 
   * @return Either a Double or a BigDecimal
   */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T convert(Object from, Class<T> to) {
        if (from instanceof BigDecimal) {
            return (T) new Double(((BigDecimal) from).doubleValue());
        } else if (from instanceof Double) {
            return to.cast(BigDecimal.valueOf((Double) from));
        }
        throw new ConversionException("{0.class.name} is neither a BigDecimal nor a Double", from);
    }
}
