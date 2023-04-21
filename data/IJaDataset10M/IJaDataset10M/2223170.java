package org.eigenbase.resgen;

/**
 * A <code>ResourceInstance</code> is an occurrence of a {@link
 * ResourceDefinition} with a set of arguments. It can later be formatted to a
 * specific locale.
 */
public interface ResourceInstance {

    public String toString();
}
