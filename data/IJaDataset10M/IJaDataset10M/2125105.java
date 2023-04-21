package de.grogra.annotation;

import de.grogra.reflect.*;
import de.grogra.util.EnumerationType;

public final class Choice extends AnnotationBase implements FieldTypeAnnotation {

    private String[] value;

    private EnumerationType type;

    public void init_value(String[] value) {
        checkFinished();
        this.value = value.clone();
        this.type = new EnumerationType("choice", value);
    }

    public String[] value() {
        return value.clone();
    }

    public Type getType(Field field) {
        return Reflection.isIntegral(field.getType()) ? type : null;
    }
}
