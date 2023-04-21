package org.antdepo.common;

import java.util.Collection;

/**
 * a set of interfaces for managing child {@link IFrameworkResource} instances. Each child is keyed by
 * its name.
 */
public interface IFrameworkResourceParent extends IFrameworkResource {

    /**
     * Create a new FrameworkResource child.
     *
     * @param name name of child
     * @return new child
     */
    IFrameworkResource createChild(String name);

    /**
     * Gets specified child
     *
     * @param name Name of child
     * @return child instance
     */
    IFrameworkResource getChild(String name);

    /**
     * Checks if a child by that name exists
     *
     * @param name key to lookup child
     * @return true if a child by that name exists
     */
    boolean existsChild(String name);

    /**
     * Return true if the child resource could be loaded from a file resource
     * @param name
     * @return
     */
    boolean childCouldBeLoaded(String name);

    /**
     * Load a specified child by name, returning null if it does not exist
     *
     * @param name
     *
     * @return
     */
    IFrameworkResource loadChild(String name);

    /**
     * List all children.
     *
     * @return A Collection of {@link IFrameworkResource} children
     */
    Collection listChildren();

    /**
     * List all child names.
     *
     * @return A Collection of Strings
     */
    Collection listChildNames();

    /**
     * Remove the child's base directory
     *
     * @param name          Name of child
     */
    void remove(String name);

    /**
     * initialize the parent. This may ask it to load in all children
     */
    void initialize();

    /**
     * Checks if a child's base dir exists
     *
     * @param name name of child
     * @return true if it exists
     */
    boolean existsChildResourceDirectory(String name);
}
