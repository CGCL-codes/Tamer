package com.jmex.terrain.util;

import com.jme.math.Ray;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;

/**
 * AbstractBresenhamTracer is a simple base class for using Bresenham's line
 * equation in jME. Bresenham's line equation is useful for doing various 3d
 * tasks that involve regularly spaced grids (such as picking against a
 * terrain.)
 * 
 * @author Joshua Slack
 */
public abstract class AbstractBresenhamTracer {

    protected final Vector3f _gridOrigin = new Vector3f();

    protected final Vector3f _gridSpacing = new Vector3f();

    protected final Vector2f _gridLocation = new Vector2f();

    protected final Vector3f _rayLocation = new Vector3f();

    protected final Ray _walkRay = new Ray();

    protected Direction stepDirection = Direction.None;

    protected float rayLength;

    public static enum Direction {

        None, PositiveX, NegativeX, PositiveY, NegativeY, PositiveZ, NegativeZ
    }

    ;

    /**
     * 
     * @return the direction of our last step on the grid.
     */
    public Direction getLastStepDirection() {
        return stepDirection;
    }

    /**
     * 
     * @return the row and column we are currently in on the grid.
     */
    public Vector2f getGridLocation() {
        return _gridLocation;
    }

    /**
     * 
     * @return how far we traveled down the ray on our last call to next().
     */
    public float getRayTraveled() {
        return rayLength;
    }

    /**
     * Set the world origin of our grid. This is useful to the tracer when doing
     * conversion between world coordinates and grid locations.
     * 
     * @param origin
     *            our new origin (copied into the tracer)
     */
    public void setGridOrigin(Vector3f origin) {
        _gridOrigin.set(origin);
    }

    /**
     * 
     * @return the current grid origin
     * @see #setGridOrigin(Vector3f)
     */
    public Vector3f getGridOrigin() {
        return _gridOrigin;
    }

    /**
     * Set the world spacing (scale) of our grid. Also useful for converting
     * between world coordinates and grid location.
     * 
     * @param spacing
     *            our new spacing (copied into the tracer)
     */
    public void setGridSpacing(Vector3f spacing) {
        this._gridSpacing.set(spacing);
    }

    /**
     * 
     * @return the current grid spacing
     * @see #setGridSpacing(Vector3f)
     */
    public Vector3f getGridSpacing() {
        return _gridSpacing;
    }

    /**
     * Set up our position on the grid and initialize the tracer using the
     * provided ray.
     * 
     * @param walkRay
     *            the world ray along which we we walk the grid.
     */
    public abstract void startWalk(final Ray walkRay);

    /**
     * Move us along our walkRay to the next grid location.
     */
    public abstract void next();

    /**
     * 
     * @return true if our walkRay, specified in startWalk, ended up being
     *         perpendicular to the grid (and therefore can not move to a new
     *         grid location on calls to next(). You should test this after
     *         calling startWalk and before calling next().
     */
    public abstract boolean isRayPerpendicularToGrid();
}
