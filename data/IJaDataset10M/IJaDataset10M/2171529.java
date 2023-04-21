package com.googlecode.simpleobjectassembler.registry;

import com.googlecode.simpleobjectassembler.ConverterRegistry;
import com.googlecode.simpleobjectassembler.ObjectAssembler;
import com.googlecode.simpleobjectassembler.converter.mapping.ConverterMappingKey;

/**
 * A key used to store a converter in the {@link ConverterRegistry} of an
 * {@link ObjectAssembler}.
 * 
 */
public class TypeBasedTransformerMappingKey implements ConverterMappingKey {

    private final Class<?> sourceClass;

    private final Class<?> destinationClass;

    public TypeBasedTransformerMappingKey(final Class<?> sourceClass, final Class<?> destinationClass) {
        super();
        this.sourceClass = sourceClass;
        this.destinationClass = destinationClass;
    }

    public Class<?> getSourceClass() {
        return sourceClass;
    }

    public Class<?> getDestinationClass() {
        return destinationClass;
    }

    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        } else if (object instanceof TypeBasedTransformerMappingKey) {
            final TypeBasedTransformerMappingKey other = (TypeBasedTransformerMappingKey) object;
            if (other.sourceClass.equals(sourceClass) && other.destinationClass.equals(destinationClass)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = sourceClass != null ? sourceClass.hashCode() : 0;
        result = 31 * result + (destinationClass != null ? destinationClass.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return sourceClass.getName() + " to " + destinationClass.getName();
    }
}
