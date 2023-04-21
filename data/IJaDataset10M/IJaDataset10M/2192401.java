package org.openwar.victory.extra;

import java.util.Hashtable;
import java.util.Map;
import org.openwar.victory.GameLoop;
import org.openwar.victory.geom.Point;
import org.openwar.victory.geom.Volume;
import org.openwar.victory.math.Rotatable;
import org.openwar.victory.math.Vector2;

/**
 * A simple physics model. Objects have a position, speed, forces to manipulate it
 * and mass to determine the effect of forces. A simple rotation and collission checking
 * model is included as well.
 * @author Bart van Heukelom
 */
public class PhysicalObject {

    private Point position = new Point();

    private Vector2 speed = new Vector2();

    private Map<String, Vector2> forces = new Hashtable<String, Vector2>();

    private double mass = 1.0;

    private Volume bounds;

    private Volume boundsSrc;

    private boolean rotateBounds;

    private double rotation = 0;

    private double rotationSpeed = 0;

    private boolean linkRotationDirection = false;

    /**
     * Create a new object at the specified position.
     * 
     * @param aPosition The position to create this object at as
     * <code>Vector2</code>
     */
    public PhysicalObject(final Point aPosition) {
        position = aPosition.clone();
        speed = new Vector2();
    }

    /**
     * Create a new object at the specified position with a speed.
     * 
     * @param aPosition The position to create this object at as
     * <code>Vector2</code>
     * @param theSpeed The initial speed of this object as <code>Vector2</code>
     */
    public PhysicalObject(final Point aPosition, final Vector2 theSpeed) {
        position = aPosition.clone();
        speed = theSpeed.clone();
    }

    /**
     * Set the position of this object to the coordinates specified by a vector.<br/>
     * <i>This is shorthand for <code>GameObject.getPos().set(Point pos)</code></i>.
     * @param aPosition The <code>Point</code> holding the new position.
     */
    public void setPosition(final Point aPosition) {
        position.set(aPosition);
    }

    /**
     * Get the position vector of this object. Modify it to move this object.
     * @return The <code>Point</code> representing this object's position.
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Set the speed vector of this object.
     * @param theSpeed The speed of this object.
     */
    public void setSpeed(final Vector2 theSpeed) {
        speed.set(theSpeed);
    }

    /**
     * @return The <code>Vector2</code> representing this object's speed
     */
    public Vector2 getSpeed() {
        return speed;
    }

    /**
     * Set a named force. Each step, this force will be applied to this object's speed.
     * The force is represented by a Vector2, with "pixels per second" (second, not step) are the units.
     * @param name The name of the force, e.g. <code>"gravity"</code>. The value of this name
     * is irrelevant, but is used as an identifier to alter or remove it later.
     * If a force with this name is already set, this overrides it.
     * @param force The force represented by a <code>Vector2</code>.
     * @throws NullPointerException if the force is <code>null</code>.
     */
    public void setForce(final String name, final Vector2 force) {
        forces.put(name, force.clone());
    }

    /**
     * Get a named force.
     * @param name The name of the force.
     * @return The force, or <code>null</code>.
     */
    public Vector2 getForce(final String name) {
        return forces.get(name);
    }

    /**
     * Remove a named force from this object.
     * @param name The force to remove.
     * @return The force which was removed.
     * @see setForce
     */
    public Vector2 removeForce(final String name) {
        return forces.remove(name);
    }

    /**
     * Set the direction in which this object is facing. This does not affect it's speed
     * or movement, but rotates it's sprite (if draw() is not overridden).
     * @param aRotation The direction in which this object is facing, in radians.
     * @return The given direction.
     */
    public double setRotation(final double aRotation) {
        rotation = aRotation;
        if (linkRotationDirection && boundsSrc instanceof Rotatable) {
            bounds = (Volume) ((Rotatable) boundsSrc).rotated(rotation);
        } else {
            bounds = boundsSrc;
        }
        return aRotation;
    }

    /**
     * Get the direction in which this object is facing.
     * @return The direction in which this object is facing, in radians.
     */
    public double getRotation() {
        return rotation;
    }

    public void setRotationSpeed(final double newRotationSpeed) {
        rotationSpeed = newRotationSpeed;
    }

    public double getRotationSpeed() {
        return rotationSpeed;
    }

    /**
     * Set whether the rotation of this object should be linked to
     * it's direction. If true, the rotation will be set to equal the
     * direction of the object, and rotationSpeed will be ignored.<br/>
     * Default is false.
     * @param link Whether to link rotation and direction.
     */
    public void linkRotationToDirection(final boolean link) {
        linkRotationDirection = link;
    }

    /**
     * Execute default movement.
     */
    protected void move() {
        for (final Vector2 force : forces.values()) {
            speed.add(force.x() / mass, force.y() / mass);
        }
        position.add(speed.x() / GameLoop.get().getSps(), speed.y() / GameLoop.get().getSps());
        if (linkRotationDirection) {
            setRotation(speed.getDirection());
        } else {
            setRotation(rotation + (rotationSpeed / GameLoop.get().getSps()));
        }
    }

    /**
     * Set the bounding volume of this object.
     * @param theBounds The bounding volume of this object,
     * or <code>null</code> if this object is not solid (does not collide).
     */
    public void setBounds(final Volume theBounds) {
        if (theBounds == null) {
            boundsSrc = null;
            bounds = null;
        } else {
            if (boundsSrc != null && boundsSrc.getClass() == theBounds.getClass()) {
                boundsSrc.adapt(theBounds);
            } else {
                boundsSrc = theBounds.clone();
            }
            setRotation(getRotation());
        }
    }

    /**
     * Get the bounding volume of this object.
     * 
     * @return The bounding volume of this object.
     */
    public Volume getBounds() {
        return boundsSrc;
    }

    /**
     * See if this object collides with another.
     * 
     * @param other The other <code>PhysicalObject</code> to check this one with
     * @return Whether the bounding boxes of both objects overlap.
     */
    public boolean collidesWith(final PhysicalObject other) {
        return collidesWith(other, position);
    }

    /**
     * See if this object collides with another, if this object
     * was at a different position.
     * 
     * @param other The other <code>PhysicalObject</code> to check this one with
     * @param myPos The supposed position of this object.
     * @return Whether the bounding boxes of both objects overlap.
     */
    public boolean collidesWith(final PhysicalObject other, final Point myPos) {
        if (bounds == null || other.bounds == null) {
            return false;
        } else {
            return bounds.overlaps(other.bounds, myPos, other.position);
        }
    }

    public double getMass() {
        return mass;
    }

    public void setMass(final double newMass) {
        mass = Math.max(0, newMass);
    }

    public boolean isRotateBounds() {
        return rotateBounds;
    }

    public void setRotateBounds(boolean rotateBounds) {
        this.rotateBounds = rotateBounds;
    }
}
