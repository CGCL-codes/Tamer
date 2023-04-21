package org.odejava;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.odejava.ode.Ode;
import org.odejava.ode.SWIGTYPE_p_dSpaceID;

/**
 * A space is a non-placeable geom that can contain other geoms. It is similar
 * to the rigid body concept of the world, except that it applies to collision
 * instead of dynamics.
 * <p/>
 * Space objects exist to make collision detection go faster. Without spaces,
 * you might generate contacts in your simulation by calling dCollide() to get
 * contact points for every single pair of geoms. For N geoms this is O(N2)
 * tests, which is too computationally expensive if your environment has many
 * objects.
 * <p/>
 * Developer note: currently all Geoms and even Body objects are stored also to
 * Space object's as LinkedLists. This is for convenience. Spaces however have
 * to do with collision only.
 * <p/>
 * Created 16.12.2003 (dd.mm.yyyy)
 *
 * @author Jani Laakso E-mail: jani.laakso@itmill.com
 *         see http://odejava.dev.java.net
 */
public abstract class Space {

    /**
     * Our ODE space identifier
     */
    protected SWIGTYPE_p_dSpaceID spaceId;

    /**
     * The identifier or the parent ODE space object to us
     */
    protected SWIGTYPE_p_dSpaceID parentId;

    protected ArrayList<Geom> geomList;

    /**
     * @deprecated This is no longer needed and will be deleted shortly. Zero IDs
     *             are automatically derived from null references to objects
     */
    public static final SWIGTYPE_p_dSpaceID SPACEID_ZERO = Ode.getPARENTSPACEID_ZERO();

    /**
     * Flag indicating that this world has been requested to be deleted. After
     * this is set to true, none of the methods should allow further calls to
     * ODE as the values are invalid, and may well cause a crash of the library
     * or other strange error.
     */
    protected boolean deleted;

    /**
     * Create a generic space object that does not have a parent space.
     */
    public Space() {
        this(null);
    }

    /**
     * Create a generic space object that belongs to the given parent space. If
     * the parent space reference is null, then this space is placed as a
     * top-level space.
     *
     * @param parent A reference to the parent space, or null
     */
    public Space(Space parent) {
        parentId = parent == null ? Ode.getPARENTSPACEID_ZERO() : parent.spaceId;
        geomList = new ArrayList<Geom>();
        deleted = false;
    }

    /**
     * Add a single item of geometry to this space. A check is performed to
     * make sure that the geometry is not already part of this space. If it is,
     * an exception is generated.
     *
     * @param geom The geometry instance to add
     * @throws IllegalOdejavaOperation The geom is already part of this space
     */
    public void addGeom(Geom geom) {
        if (geomList.contains(geom)) {
            throw new IllegalOdejavaOperation("Attempting to add a Geom which already belongs to this Body!");
        }
        geomList.add(geom);
        geom.addToSpace(this);
    }

    /**
     * Add a single item of geometry to this space. A check is performed to
     * make sure that the geometry is not already part of this space. If it is,
     * an exception is generated.
     *
     * @param geom The geometry instance to add
     * @throws IllegalOdejavaOperation The geom is already part of this space
     */
    public void add(Geom geom) {
        addGeom(geom);
    }

    /**
     * Adds all of the geoms that belong to the passed Body. A check is
     * performed to make sure that no geometries are multiply added. If one is
     * found, an exception is generated.
     *
     * @param body A body to get the geometry(s) from
     * @throws IllegalOdejavaOperation The geom is already part of this space
     */
    public void addBodyGeoms(Body body) {
        for (Iterator i = body.getGeoms().iterator(); i.hasNext(); ) {
            addGeom((Geom) i.next());
        }
    }

    /**
     * Removes a Geom from this Space. This Geom can then be added to another
     * Space if desired.
     *
     * @throws IllegalArgumentException if the Geom is not currently in this space.
     */
    public void remove(Geom geom) {
        if (!geomList.contains(geom)) {
            return;
        }
        geom.removeFromSpace();
        geomList.remove(geom);
    }

    /**
     * Gets a list of the Geoms in the scene.
     *
     * @return Returns the geoms.
     */
    public List<Geom> getGeoms() {
        return new LinkedList<Geom>(geomList);
    }

    /**
     * Set the cleanup mode to be used by this space when it is deleted. A
     * value of true says that child geometry should also be deleted when this
     * space is deleted. A value of false leaves the child geometry alone.
     *
     * @param killKids true to delete the child geometry too
     */
    public void setChildCleanupMode(boolean killKids) {
        Ode.dSpaceSetCleanup(spaceId, killKids ? 1 : 0);
    }

    /**
     * Get the child cleanup mode to be used after the next delete.
     *
     * @return true if child geometry is being deleted too
     */
    public boolean getChildCleanupMode() {
        return Ode.dSpaceGetCleanup(spaceId) == 1;
    }

    /**
     * Request deletion of this space. Any further calls to this space
     * instance after this has been called will be met with an error.
     */
    public void delete() {
        Iterator<Geom> i;
        i = getGeoms().iterator();
        while (i.hasNext()) {
            (i.next()).delete();
        }
        Ode.dSpaceDestroy(spaceId);
        deleted = true;
    }

    public SWIGTYPE_p_dSpaceID getId() {
        return spaceId;
    }
}
