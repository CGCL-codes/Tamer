package org.sourceforge.jemm.database.components.interfaces;

import org.sourceforge.jemm.types.ID;

/**
 * @author Rory Graves
 *
 */
public interface DBRootHandler {

    /**
     * Set a persistent root
     * @param rootName The name of the root.
     * @param newValue The ID of the object held at that root.
     */
    void setRoot(String rootName, ID newValue);

    /**
     * Set the given root, if the current value of the root is null.
     * @param rootName The name of the root.
     * @param newValue The new value to set
     * @return The old value of the root, (i.e. the current value if the set fails, or null if it does)
     */
    ID setRootIfNull(String rootName, ID newValue);

    /**
     * Return the current value of the root.
     * @param rootName The name of the root.
     * @return The current value of the root reference 'rootName'
     */
    ID getRoot(String rootName);
}
