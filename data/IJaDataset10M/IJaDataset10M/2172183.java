package net.sf.persistant.util;

/**
 * <p>
 * <code>Locator</code> defines the public interface to a generic object which finds or creates another object.
 * </p>
 */
public interface Locator<O> {

    /**
     * <p>
     * Create (or find) and return the object.
     * </p>
     *
     * @return {@link Object} the object.
     */
    public O locate();
}
