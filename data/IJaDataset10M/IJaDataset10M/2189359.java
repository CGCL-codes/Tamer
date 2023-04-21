package com.bluestone.util.location;

/**
 * A interface that should be implemented by objects knowning their location (i.e. where they
 * have been created from).
 */
public interface Locatable {

    /**
     * Get the location of this object
     * 
     * @return the location
     */
    public Location getLocation();
}
