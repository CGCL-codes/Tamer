package org.powermock.reflect.internal.matcherstrategies;

import java.lang.reflect.Field;

public class AssignableToFieldTypeMatcherStrategy extends FieldTypeMatcherStrategy {

    public AssignableToFieldTypeMatcherStrategy(Class<?> fieldType) {
        super(fieldType);
    }

    @Override
    public boolean matches(Field field) {
        return expectedFieldType.isAssignableFrom((Class<?>) field.getType());
    }
}
