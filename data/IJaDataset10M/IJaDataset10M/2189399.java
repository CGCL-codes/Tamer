package net.sf.javareflector;

import com.google.common.base.Preconditions;

/**
 * Created: 4/10/11
 */
class PropertyReferenceSegment {

    private final Reflector<?> reflector;

    private final Object target;

    private final String reference;

    private final int dotIndex;

    PropertyReferenceSegment(Reflector<?> reflector, Object target, String reference) {
        this.reflector = reflector;
        this.reference = reference;
        this.target = target;
        dotIndex = reference.indexOf('.');
    }

    private String getName() {
        return dotIndex < 0 ? reference : reference.substring(0, dotIndex);
    }

    Object getValue() {
        Object value = evaluate();
        return hasNext() && value != null ? next(value).getValue() : value;
    }

    private Object evaluate() {
        return getProperty().getValue(target);
    }

    void setValue(Object value) {
        if (hasNext()) {
            next().setValue(value);
        } else {
            getProperty().setValue(target, value);
        }
    }

    Object parseValueFrom(String valueAsString) {
        if (hasNext()) {
            return next().parseValueFrom(valueAsString);
        } else {
            return getProperty().parseValueFrom(target, valueAsString);
        }
    }

    private Property<?> getProperty() {
        return reflector.getProperty(getName());
    }

    boolean hasNext() {
        return dotIndex >= 0;
    }

    PropertyReferenceSegment next() {
        return next(evaluate());
    }

    PropertyReferenceSegment next(Object value) {
        validateNext(value);
        Reflector<?> nextReflector = reflector.getReflector(value.getClass());
        return new PropertyReferenceSegment(nextReflector, value, reference.substring(dotIndex + 1));
    }

    private void validateNext(Object value) {
        Preconditions.checkState(hasNext());
        if (value == null) {
            throw new IllegalArgumentException("The property reference " + reference + " could not be evaluated for an instance of " + target.getClass() + " because the first segment evaluated to null.");
        }
    }
}
