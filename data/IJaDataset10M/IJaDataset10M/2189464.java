package xutools.universe.inventory.items;

import xutools.config.ObjectType;
import xutools.config.race.Race;
import xutools.universe.core.Sector;

/**
 * A generic space object that is owned by a particular race.
 * 
 * @author Tobias Weigel
 * @date 09.10.2008
 * 
 */
public abstract class RaceObject extends X2SpaceObject {

    /**
     * @param sector
     * @param objectType
     * @param race
     * @param collisionRange
     */
    public RaceObject(Sector sector, ObjectType objectType, Race race, double collisionRange) {
        super(sector, objectType, collisionRange);
        this.race = race;
    }

    public Race race;

    /**
     * @return the race
     */
    public Race getRace() {
        return race;
    }

    /**
     * Sets a new race for the object. Note that changing the owner race by
     * setting a different race than specified in the constructor may have
     * unknown side effects!
     * 
     * @param race
     *            the race to set
     */
    public void setRace(Race race) {
        if (!objectType.getOwnerRaces().contains(race)) throw new IllegalArgumentException(objectType + " cannot be owned by " + race + "!");
        this.race = race;
    }
}
