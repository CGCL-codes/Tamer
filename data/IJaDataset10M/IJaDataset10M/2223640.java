package net.sf.dozer.util.mapping.converters;

/**
 * @author tierney.matt
 * 
 */
public class ThrowExceptionCustomConverter implements CustomConverter {

    public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {
        throw new RuntimeException("throwing exception from custom converter");
    }
}
