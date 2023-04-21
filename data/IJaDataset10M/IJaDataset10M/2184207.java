package net.entropysoft.transmorph.converters;

import net.entropysoft.transmorph.ConversionContext;
import net.entropysoft.transmorph.ConverterException;
import net.entropysoft.transmorph.type.TypeReference;

/**
 * Converter used when source is an array of chars and destination is a String
 * 
 * @author Cedric Chabanois (cchabanois at gmail.com)
 * 
 */
public class CharacterArrayToString extends AbstractConverter {

    public CharacterArrayToString() {
        this.useObjectPool = false;
    }

    public Object doConvert(ConversionContext context, Object sourceObject, TypeReference<?> destinationType) throws ConverterException {
        if (sourceObject == null) {
            return null;
        }
        char[] charArray = (char[]) sourceObject;
        return String.valueOf(charArray);
    }

    protected boolean canHandleDestinationType(TypeReference<?> destinationType) {
        return destinationType.isType(String.class);
    }

    protected boolean canHandleSourceObject(Object sourceObject) {
        if (sourceObject == null) {
            return true;
        }
        return sourceObject instanceof char[];
    }
}
