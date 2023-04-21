package org.waveprotocol.wave.model.util;

/**
 * Boxes a value. Mainly useful as a workaround for the finality requirement of
 * values in a java closure's scope.
 *
 * @author danilatos@google.com (Daniel Danilatos)
 *
 * @param <T>
 */
public class Box<T> {

    /**
   * Settable value.
   */
    public T boxed;

    /**
   * Convenience factory method.
   */
    public static <T> Box<T> create() {
        return new Box<T>();
    }

    /**
   * Convenience factory method.
   */
    public static <T> Box<T> create(T initial) {
        return new Box<T>(initial);
    }

    /** No initial value. */
    public Box() {
        this(null);
    }

    /**
   * @param boxed initial value.
   */
    public Box(T boxed) {
        this.boxed = boxed;
    }

    /**
   * Sets the boxed value to the given new value.
   */
    public void set(T newVal) {
        this.boxed = newVal;
    }

    /**
   * @return the boxed value.
   */
    public T get() {
        return this.boxed;
    }

    /**
   * Sets the boxed value to null.
   */
    public void clear() {
        this.boxed = null;
    }
}
