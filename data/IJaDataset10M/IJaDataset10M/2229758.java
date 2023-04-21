package net.sf.dozer.util.mapping.converters;

import net.sf.dozer.util.mapping.MappingException;

/**
 * Only intended for internal use.
 * 
 * @author tierney.matt
 */
public class ConversionException extends MappingException {

    public ConversionException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public ConversionException(Throwable throwable) {
        super(throwable);
    }
}
