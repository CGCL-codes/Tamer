package universe.server.database;

import java.io.Serializable;
import java.util.Vector;
import java.util.Enumeration;
import universe.server.*;
import universe.common.*;
import universe.common.database.*;

/**  Abstract class from which all concrete classes used
 *   to represent galaxies in the game are to be derived.
 *
 *   This class contains implementation of basic primitive
 *   methods for the storage and manipulation of standard
 *   galaxy attributes.  Subclasses merely have to override
 *   the generateSystems method to ensure that star systems
 *   and their planets are created as per the nature of the
 *   galaxy (e.g. randomly determined, loaded from a data 
 *   file, a combination of the above, etc.).
 *
 *   @see GalaxyStd
 *   @see GalaxyGliese
 *   @version $Id: GalaxyBase.java,v 1.8 2003/04/18 21:41:20 sstarkey Exp $
 */
public abstract class GalaxyBase extends DBItem {

    private GalaxyID ID;

    private String name;

    private Vector systems;

    private boolean HasGeneratedSystems = false;

    /**
     * Designated constructor - generates unique ID for the galaxy
     * object and adds it to the list of galaxies in the game.
     *
     * Subclasses must call this method in their constructor
     * to ensure proper registration with the game.
     */
    GalaxyBase() {
        ID = (GalaxyID) GameEngine.getDatabase().getNextIndex(new GalaxyID());
        systems = new Vector();
        name = "Galaxy " + ID;
    }

    /**
     * Constructor for GalaxyUndefined.
     *
     * N.B. Does not register this instance with the game as one
     * present in the game since its used to represent bogus
     * or unknown galaxy information. 
     */
    GalaxyBase(boolean dummy) {
        ID = new GalaxyID(0);
        systems = new Vector();
        name = "Unknown Galaxy";
    }

    /**
     * Return the ID
     */
    public GalaxyID getID() {
        return ID;
    }

    public Index getIndex() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String in_name) {
        name = in_name;
    }

    /**
     * Adds a System to the Galaxy
     */
    public void addSystem(SystemID SID) {
        Log.debug("adding system " + SID + " to galaxy " + ID);
        systems.add(SID);
    }

    /**
     * Returns the galaxy's system list
     */
    public Vector getSystems() {
        return systems;
    }

    /**
     * Returns true if the galaxy has been populated with
     * star systems and their planets, and false if this
     * has not yet been done.
     *
     * This method is primarily used as a guard to prevent
     * the calling of generateSystems() more than once.
     *
     * @see generateSystems()
     */
    public boolean hasGeneratedSystems() {
        return HasGeneratedSystems;
    }

    /** This method should be extended by subclasses to causes the
         *  receiving galaxy to generate the collection of star systems
         *  and associated planets which it will contain.
         *
         *  Calls to this method after the galaxy is already populated
         *  with star systems should have no effect, and it is the
	 *  responsiblity of subclasses to do this by checking the
         *  value returned from the hasGeneratedSystems method.
         *
         * @param planetDistribution Array of floating point values representing the probability of planetary mass at successive distances from its primary.
         *
         * @throws SchemeUnknownException If unable to determine a ID of the galaxy to which systems are to be added.
         * @throws InterruptedException If access to a resource needed for star system generation has been interrupted.
         *
         * @see hasGeneratedSystems
         */
    public void generateSystems(float[] planetDistribution) throws SchemeUnknownException, InterruptedException {
        HasGeneratedSystems = true;
    }
}
