package fate.spatial.spaces.universal;

import fate.gameUnit.GameUnit;
import fate.gameUnit.Ship;
import fate.gameUnit.units.celestial.Planet;
import fate.spatial.*;
import fate.spatial.spaces.galactic.Galaxy;
import fate.spatial.spaces.solar.SolarSystem;
import fate.spatial.spaces.util.SphericalSpace;
import fate.spatial.spaces.util.SphericalSpace.Location;
import java.io.Serializable;
import java.util.Vector;

/** 
 * The base storage object of the Fate "World".
 *
 * @author  preylude@s3m.com
 * @version 0.1.0
 */
public class Universe extends SphericalSpace implements Serializable {

    /** Map of all galaxies in the Universe */
    public fate.spatial.coordinateSystems.CoordinateSystem.Vector mapGalaxies;

    /** Map of all players in the Universe */
    public fate.spatial.coordinateSystems.CoordinateSystem.Vector mapPlayers;

    /** Highest numbered id generated */
    int lastID;

    public Galaxy defaultGalaxy;

    public SolarSystem defaultSolarSystem;

    public Planet defaultPlanet;

    /** Constructs Default Universe */
    public Universe() {
        super((float) 10000000);
        lastID = 100;
        mapPlayers = (fate.spatial.coordinateSystems.CoordinateSystem.Vector) new java.util.Vector();
        this.createDefault();
    }

    public synchronized int getNextID() {
        lastID++;
        return lastID;
    }

    private void createDefault() {
        defaultGalaxy = new Galaxy((float) 100000);
        ;
        defaultSolarSystem = new SolarSystem((float) 1000);
        ;
        defaultPlanet = new Planet();
    }

    public String toString() {
        return "Fate Universe";
    }

    public void enterSpace(GameUnit moveUnit, Position enterFrom) {
    }
}
