package org.nakedobjects.nof.reflect.java.reflect;

/**
 * Base class for implementations of {@link ClassStrategy} that decorates an underlying implementation with
 * new functionality.
 */
public abstract class DelegatingClassStrategy implements ClassStrategy {

    public DelegatingClassStrategy(final ClassStrategy decorated) {
        this.decorated = decorated;
    }

    private final ClassStrategy decorated;

    public Class getClass(final Class cls) {
        return decorated.getClass(cls);
    }

    public Class[] getInvalidFieldTypes() {
        return decorated.getInvalidFieldTypes();
    }

    public Class[] getValueTypes() {
        return decorated.getValueTypes();
    }

    public void addValueType(final Class clazz) {
        decorated.addValueType(clazz);
    }

    public boolean isValueType(final Class clazz) {
        return decorated.isValueType(clazz);
    }

    public boolean isSystemClass(final Class cls) {
        return decorated.isSystemClass(cls);
    }
}
