package net.entropysoft.transmorph.converters.enums;

import java.text.MessageFormat;
import net.entropysoft.transmorph.ConversionContext;
import net.entropysoft.transmorph.ConverterException;
import net.entropysoft.transmorph.converters.AbstractConverter;
import net.entropysoft.transmorph.type.TypeReference;

/**
 * Convert a string to the corresponding enumeration
 * 
 * @author Cedric Chabanois (cchabanois at gmail.com)
 * 
 */
public class StringToEnum extends AbstractConverter {

    public StringToEnum() {
        this.useObjectPool = true;
    }

    public Object doConvert(ConversionContext context, Object sourceObject, TypeReference<?> destinationType) throws ConverterException {
        if (sourceObject == null) {
            return null;
        }
        String sourceString = (String) sourceObject;
        try {
            Class<Enum> enumType = (Class<Enum>) destinationType.getRawType();
            return Enum.valueOf(enumType, sourceString);
        } catch (IllegalArgumentException e) {
            throw new ConverterException(MessageFormat.format("Enum type ''{0}'' has no constant with the specified name ''{1}''", destinationType.toHumanString(), sourceString), e);
        }
    }

    protected boolean canHandleDestinationType(TypeReference<?> destinationType) {
        return destinationType.getRawType().isEnum();
    }

    protected boolean canHandleSourceObject(Object sourceObject) {
        if (sourceObject == null) {
            return true;
        }
        return sourceObject instanceof String;
    }
}
