package net.entropysoft.transmorph.converters;

import net.entropysoft.transmorph.ConversionContext;
import net.entropysoft.transmorph.ConverterException;
import net.entropysoft.transmorph.type.TypeReference;

/**
 * converter used when source object type and destination type are compatible.
 * No conversion is done
 * 
 * @author Cedric Chabanois (cchabanois at gmail.com)
 * 
 */
public class IdentityConverter extends AbstractConverter {

    public IdentityConverter() {
        this.useObjectPool = false;
    }

    public Object doConvert(ConversionContext context, Object sourceObject, TypeReference<?> destinationType) throws ConverterException {
        return sourceObject;
    }

    protected boolean canHandleDestinationType(TypeReference<?> destinationType) {
        return true;
    }

    protected boolean canHandleSourceObject(Object sourceObject) {
        return true;
    }

    @Override
    public boolean canHandle(ConversionContext context, Object sourceObject, TypeReference<?> destinationType) {
        if (sourceObject == null && !destinationType.isPrimitive()) {
            return true;
        }
        return destinationType.isRawTypeInstance(sourceObject);
    }
}
