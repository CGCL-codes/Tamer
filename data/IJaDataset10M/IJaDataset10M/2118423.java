package org.antdepo.common;

import java.io.File;

/**
 * Implementations of this interface provide a resource in a composition hierarchy of resources.
 * Conceptually, one can imagine a framework resource as a node in a acyclic directed graph. The word
 * "Node" was not chosen to avoid confusion with "machine nodes".
 * <p/>
 * ControlTier Software Inc.
 * User: alexh
 * Date: Jul 13, 2005
 * Time: 5:42:38 PM
 */
public interface IFrameworkResource {

    /**
     * Getter to resource name
     *
     * @return
     */
    String getName();

    /**
     * Getter to resource base dir
     *
     * @return
     */
    File getBaseDir();

    /**
     * Get the parent of this resource
     */
    IFrameworkResourceParent getParent();

    boolean isValid();
}
