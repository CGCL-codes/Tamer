package org.databene.commons.converter;

import org.databene.commons.Converter;
import org.databene.commons.bean.HashCodeBuilder;

/**
 * Converter id class for the ConverterManager.<br/><br/>
 * Created: 27.02.2010 05:45:43
 * @since 0.5.0
 * @author Volker Bergmann
 */
class ConversionTypes {

    public final Class<?> sourceType;

    public final Class<?> targetType;

    public ConversionTypes(Converter<?, ?> converter) {
        this(converter.getSourceType(), converter.getTargetType());
    }

    public ConversionTypes(Class<?> sourceType, Class<?> targetType) {
        this.sourceType = sourceType;
        this.targetType = targetType;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.hashCode(sourceType, targetType);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        ConversionTypes that = (ConversionTypes) obj;
        return (this.sourceType == that.sourceType && this.targetType == that.targetType);
    }

    @Override
    public String toString() {
        return sourceType.getName() + "->" + targetType.getName();
    }
}
