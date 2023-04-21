package de.grogra.annotation;

import de.grogra.reflect.*;

public final class Range extends AnnotationBase implements FieldTypeAnnotation {

    private double min, max;

    public void init_min(double min) {
        checkFinished();
        this.min = min;
    }

    public void init_max(double max) {
        checkFinished();
        this.max = max;
    }

    public double min() {
        return min;
    }

    public double max() {
        return max;
    }

    public Type getType(Field field) {
        return (((1 << field.getType().getTypeId()) & TypeId.NUMERIC_NONCHAR_MASK) != 0) ? new BoundedType("bounded", field.getType().getTypeId(), new Double(min), new Double(max)) : null;
    }
}
