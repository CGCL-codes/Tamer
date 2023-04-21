package com.enerjy.common.util;

/**
 * An interface for acquiring a set of paths. Used for source and classpath queries.
 */
public interface IPathProvider {

    /**
     * Obtain the paths provided by this, err, path provider.
     * 
     * @return Array of paths. Should not be null.
     */
    String[] getPaths();
}
