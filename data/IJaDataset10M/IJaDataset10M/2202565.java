package de.fhg.igd.semoa.bin.starter;

/**
 * This interface must be implemented by all parameter types.
 *
 * @author Matthias Pressfreund
 * @version "$Id: Parameter.java 1616 2005-07-06 10:34:39Z jpeters $"
 */
public interface Parameter {

    /**
     * Indicate whether or not this <code>Parameter</code> is partly invalid
     * but still useable, e.g. by processing contained invalid elements anyway.
     *
     * @return Whether or not this <code>Parameter</code> contains
     *   invalid elements
     */
    boolean isFlawed();

    /**
     * Get the full <code>String</code> representation instead of
     * the {@link #toShortString() abbreviation}.
     *
     * @return The full <code>String</code> representation
     */
    String toFullString();

    /**
     * Get the abbreviated <code>String</code> representation instead of
     * the {@link #toFullString() full version}.
     *
     * @return The abbreviated <code>String</code> representation
     */
    String toShortString();
}
