package org.jtools.classloader;

/**
 * Handler interface.
 * @since Ant1.7
 */
public interface ClHandler {

    /**
     * Checks whether this handler is properly configured.
     *
     * @throws RuntimeException If not properly defined.
     */
    void check();

    /**
     * Gets an adapter instance.
     *
     * @param context The calling context (f.e.classloader task).
     * @return The newly created adapter or null if an error occured.
     */
    ClAdapter getAdapter(ClContext context);

    /**
     * Gets the classloaders classname.
     *
     * @return The classloader classname.
     */
    String getLoader();
}
