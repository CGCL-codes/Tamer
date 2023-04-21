package skycastle.huntergatherers.model.location;

import skycastle.huntergatherers.model.space.Space;

/**
 * An interface that collects together location related methods.
 *
 * TODO: Add listener..  Or rather, move to the already existing location class...
 *
 * @author Hans H�ggstr�m
 */
public interface Location {

    /**
     * @return the radius of a bounding sphere around the center of the object, that contains the object completely.
     *         Used e.g. for initial visibility checks, quadtree search, etc.
     */
    float getBoundingSphereRadius_m();

    /**
     * @return position in world along the x axis (ground plane axis) in meters.
     */
    float getX();

    /**
     * @return position in world along the y axis (vertical axis) in meters.
     */
    float getY();

    /**
     * @return position in world along the z axis (ground plane axis) in meters.
     */
    float getZ();

    /**
     * Sets the position of this entity in world coordinates.  May be placed in air or below ground.
     *
     * @param x position along ground plane in meters.
     * @param y vertical position in meters.
     * @param z position along ground plane in meters.
     */
    void setPosition(float x, float y, float z);

    /**
     * Sets the position of this entity in world coordinates.  The entity will be placed at the ground at the specified
     * ground plane position.
     *
     * @param x position along ground plane in meters.
     * @param z position along ground plane in meters.
     */
    void setPosition(float x, float z);

    /**
     * @param location the location to get the distance to
     *
     * @return the distance from this location to the specified location.
     */
    float distanceTo(final Location location);

    /**
     * @param space the place / space container that this location is in.
     * Could be used e.g. for different coordinate systems in different space (moving vehicles, planets, interstellar space,
     * or even just different rooms in a house, to implement portal based visibility culling and collision detection.
     */
    void setSpace(final Space space);

    /**
     *
     * @return the place / space container that this location is in.  May contain coordinate transformation matrix etc.
     */
    Space getSpace();

    /**
     * Adds the specified LocationChangeListener.
     *
     * @param addedLocationChangeListener should not be null or already added.
     */
    void addLocationChangeListener(LocationChangeListener addedLocationChangeListener);

    /**
     * Removes the specified LocationChangeListener.
     *
     * @param removedLocationChangeListener should not be null.
     */
    void removeLocationChangeListener(LocationChangeListener removedLocationChangeListener);
}
