package org.peaseplate.typeconversion.conversion;

import org.peaseplate.typeconversion.ConvertibleType;
import org.peaseplate.typeconversion.TypeConversion;
import org.peaseplate.typeconversion.TypeConversionContext;
import org.peaseplate.typeconversion.TypeConversionException;
import org.peaseplate.typeconversion.TypeConversionService;

/**
 * Null will become an empty {@link StringBuffer}
 * 
 * @author Manfred HANTSCHEL
 */
public class VoidToStringBufferConversion implements TypeConversion<Void, StringBuffer> {

    public VoidToStringBufferConversion() {
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
    public Class<StringBuffer> getTargetType() {
        return StringBuffer.class;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public StringBuffer convert(final TypeConversionService service, final Void value, final Class<StringBuffer> targetType, final TypeConversionContext context, final boolean convertNull) throws TypeConversionException {
        return new StringBuffer();
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public ConvertibleType isConvertable(final TypeConversionService service, final Class<Void> sourceType, final Class<StringBuffer> targetType, final TypeConversionContext context, final boolean convertNull) {
        return ConvertibleType.ASSURED;
    }
}
