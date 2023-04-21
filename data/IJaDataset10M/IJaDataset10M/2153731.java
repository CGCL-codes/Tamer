package org.salamandra.web.core.property.converter;

import java.math.BigDecimal;
import org.salamandra.web.core.property.AnnotationProperty;
import org.salamandra.web.core.property.converter.adapt.BigDecimalAdapter;
import org.salamandra.web.core.property.converter.annotation.ABigDecimalConverter;

public class BigDecimalConverter extends AnnotationProperty<ABigDecimalConverter, BigDecimal> {

    /**
     * Field adapter.
     */
    protected BigDecimalAdapter adapter;

    @Override
    public BigDecimal convert(String value) {
        try {
            return adapter.format(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public void initialize(ABigDecimalConverter annotation) {
        adapter = new BigDecimalAdapter();
    }
}
