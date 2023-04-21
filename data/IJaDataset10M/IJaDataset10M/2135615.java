package org.powermock.reflect.internal.matcherstrategies;

import org.powermock.reflect.exceptions.FieldNotFoundException;
import java.lang.reflect.Field;

public class FieldTypeMatcherStrategy extends FieldMatcherStrategy {

    final Class<?> expectedFieldType;

    public FieldTypeMatcherStrategy(Class<?> fieldType) {
        if (fieldType == null) {
            throw new IllegalArgumentException("field type cannot be null.");
        }
        this.expectedFieldType = fieldType;
    }

    @Override
    public boolean matches(Field field) {
        return expectedFieldType.equals(field.getType());
    }

    @Override
    public void notFound(Class<?> type, boolean isInstanceField) throws FieldNotFoundException {
        throw new FieldNotFoundException(String.format("No %s field of type \"%s\" could be found in the class hierarchy of %s.", isInstanceField ? "instance" : "static", expectedFieldType.getName(), type.getName()));
    }

    @Override
    public String toString() {
        return "type " + expectedFieldType.getName();
    }
}
