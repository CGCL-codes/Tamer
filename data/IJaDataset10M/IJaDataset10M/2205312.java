package org.jtools.classloader;

/**
 * Mandatory Interface for Parameters.
 */
public interface Parameters {

    /**
     * Gets the default handler for this descriptor.
     *
     * @return handler.
     */
    Handler getDefaultHandler();

    /**
     * Gets the valuable parameter object which is either the instance itself
     * or the resolved referenced parameters.
     *
     * @return parameters.
     */
    Parameters getParameters();
}
