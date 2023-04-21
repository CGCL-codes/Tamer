package org.yacmmf.core;

/**
 * Helper class to work with {@link AtomicComponent}s.
 */
public class AtomicAttribute<T> {

    private final ReferenceAttribute attribute;

    public AtomicAttribute(ReferenceAttribute attribute) {
        this.attribute = attribute;
    }

    @SuppressWarnings("unchecked")
    public T get(Component container) {
        return ((AtomicComponent<T>) attribute.get(container)).getValue();
    }

    public boolean isSettable() {
        return attribute.isSettable();
    }

    public void set(Component container, T value) {
        attribute.set(container, new AtomicComponent<T>(value));
    }
}
