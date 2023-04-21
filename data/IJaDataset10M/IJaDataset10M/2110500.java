package com.jmex.physics.contact;

import com.jme.math.Vector3f;
import com.jmex.physics.PhysicsCollisionGeometry;
import com.jmex.physics.PhysicsNode;

/**
 * Interface to query details for a contact of two collision geometries.
 *
 * @author Irrisor
 */
public interface ContactInfo {

    /**
     * @return elapsed time after the start of the simulation step before the contact occured
     */
    float getTime();

    /**
     * First geometry in this contact. Order is not necessarily defined!
     *
     * @return contacting geometry
     * @see #getGeometry2()
     */
    PhysicsCollisionGeometry getGeometry1();

    /**
     * Second geometry in this contact. Order is not necessarily defined!
     *
     * @return contacting geometry
     * @see #getGeometry1()
     */
    PhysicsCollisionGeometry getGeometry2();

    /**
     * First node in this contact. Order is not necessarily defined!
     *
     * @return contacting node
     * @see #getNode2()
     */
    PhysicsNode getNode1();

    /**
     * Second node in this contact. Order is not necessarily defined!
     *
     * @return contacting node
     * @see #getNode1()
     */
    PhysicsNode getNode2();

    /**
     * @param store where to put the value, null to create a new Vector
     * @return the velocity with which the two objects hit (in direction 'into' object 1)
     */
    public Vector3f getContactVelocity(Vector3f store);

    /**
     * @param store where to put the value, null to create a new Vector
     * @return the position of this contact in world coordinate space
     */
    public Vector3f getContactPosition(Vector3f store);

    /**
     * @param store where to put the value, null to create a new Vector
     * @return the normal of the contact (vector that is orthogonal to both contacting surfaces in the contact point)
     */
    public Vector3f getContactNormal(Vector3f store);

    /**
     * @return distance the geometries intersect in normal direction, ideally 0 (if contact is detected early enough)
     * @see #getContactNormal(com.jme.math.Vector3f)
     */
    public float getPenetrationDepth();

    /**
     * Query the default for the directions of the friction. Both directions are perpendicular to each other and
     * perpendicular to the contact normal.
     *
     * @param primaryStore   store for first friction direction as specified in
     *                       {@link ContactHandlingDetails#getFrictionDirection(com.jme.math.Vector3f)} (cannot be null)
     * @param secondaryStore second direction store (may be null)
     */
    public void getDefaultFrictionDirections(Vector3f primaryStore, Vector3f secondaryStore);
}
