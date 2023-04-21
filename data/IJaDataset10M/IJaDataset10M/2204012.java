package com.flagstone.transform;

/**
 * The DefineTag interface is used to identify object that are used to add
 * definitions for shapes, images, sounds ,etc. to a Flash file.
 */
public interface DefineTag extends MovieTag {

    /**
     * Get the unique identifier assigned to this object.
     *
     * @return the unique identifier used by the Flash Player to reference this
     * definition.
     */
    int getIdentifier();

    /**
     * Sets the unique identifier for an object within a given Movie.
     *
     * @param uid
     *            a unique identifier for the object. Must be in the range
     *            1..65535.
     */
    void setIdentifier(final int uid);
}
