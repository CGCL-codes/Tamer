package com.volantis.mock.samples;

/**
 * Makes sure that an interface that overrides hashCode() is mockable.
 *
 * @mock.generate
 */
public interface InterfaceWithHashCode {

    /**
     * Override {@link Object#hashCode()}.
     */
    int hashCode();
}
