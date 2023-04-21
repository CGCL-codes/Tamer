package org.peaseplate.typeconversion.conversion;

import org.peaseplate.typeconversion.ConvertibleType;
import org.peaseplate.typeconversion.TypeConversion;
import org.peaseplate.typeconversion.TypeConversionContext;
import org.peaseplate.typeconversion.TypeConversionException;
import org.peaseplate.typeconversion.TypeConversionService;

/**
 * Null will become zero.
 * 
 * @author Manfred HANTSCHEL
 */
public class VoidToFloatConversion implements TypeConversion<Void, Float> {

    public static final Float ZERO = Float.valueOf(0);

    public VoidToFloatConversion() {
        super();
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public Class<Void> getSourceType() {
        return Void.class;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public Class<Float> getTargetType() {
        return Float.class;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public Float convert(final TypeConversionService service, final Void value, final Class<Float> targetType, final TypeConversionContext context, final boolean convertNull) throws TypeConversionException {
        return ZERO;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public ConvertibleType isConvertable(final TypeConversionService service, final Class<Void> sourceType, final Class<Float> targetType, final TypeConversionContext context, final boolean convertNull) {
        return ConvertibleType.ASSURED;
    }
}
