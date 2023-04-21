package org.enkryption.starsClone.entity.planet;

import java.io.Serializable;
import org.enkryption.starsClone.Empire;
import org.enkryption.starsClone.Resources;
import org.enkryption.starsClone.entity.Coordinates;
import org.enkryption.starsClone.entity.ILocatable;

/**
 * Represents a planet
 * @author Hsiu-Fan
 */
public class Planet implements ILocatable, Serializable {

    /** Coordinates the planet is located at */
    private Coordinates coordinates;

    /** Number of factories constructed */
    private int factory = 0;

    /** Number of mines constructed */
    private int mine = 0;

    /** Name of the planet */
    private String name;

    /** The name of the owning empire (null if no one) */
    private Empire owner;

    /** Construction queue */
    private BuildQueue queue;

    /** Resources currently available on planet */
    private Resources resources;

    /**
	 * Creates a new planet at the given coordinates, named "unnamed planet"
	 * @param coordinates Coordinates the planet is to be created at
	 */
    public Planet(Coordinates coordinates) {
        this(coordinates, "Unnamed Planet");
    }

    /**
	 * Creates a new planet at the given coordinates with the given name
	 * @param coordinates Coordinates the planet is to be created at
	 * @param name        Name the planet is to be given
	 */
    public Planet(Coordinates coordinates, String name) {
        this.coordinates = coordinates;
        this.name = name;
        this.queue = new BuildQueue(this);
        this.resources = new Resources();
    }

    /**
	 * Builds factories on the planet
	 * @param number number of factories to be built
	 * @return number of factories total
	 */
    public int buildFactory(int number) {
        factory += number;
        return mine;
    }

    /**
	 * Builds mines on the planet
	 * @param number number of mines to be built
	 * @return number of mines total
	 */
    public int buildMine(int number) {
        mine += number;
        return mine;
    }

    /**
	 * Items on the construction queue are produced
	 */
    private void constructQueue() {
        queue.process();
    }

    /**
	 * Mines produce minerals, and factories construction
	 */
    private void gatherResources() {
        getResources().setConstruction(0);
        setResources(getResources().add(getOwner().resourceProduction(this)));
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
	 * Retrieves the number of factories that have been constructed on the
	 * planet
	 * @return number of factories
	 */
    public int getFactory() {
        return factory;
    }

    public ILocatable getLocation() {
        return this;
    }

    /**
	 * Retrieves the number of mines currently built on the planet
	 * @return number of mines built on planet
	 */
    public int getMine() {
        return mine;
    }

    /**
	 * Retrieves the planet's name
	 * @return planet's name
	 */
    public String getName() {
        return name;
    }

    /**
	 * Retrieves the planet's owning empire
	 * @return empire currently in control of planet
	 */
    public Empire getOwner() {
        return owner;
    }

    /**
	 * Retrieves the Resources representation of the planet's available
	 * resources
	 * @return Planet's available resources
	 */
    public Resources getResources() {
        return resources;
    }

    /**
	 * Runs one year of production on the planet. A year of production involves
	 * resource gathering, construction and reproduction.
	 */
    public void produce() {
        if (isOccupied()) {
            gatherResources();
            constructQueue();
            reproduce();
        }
    }

    /**
	 * Is the planet occupied?
	 * @return true if an empire is in possession of a planet
	 */
    public boolean isOccupied() {
        return getOwner() != null;
    }

    /**
	 * Colonists reproduce and the numbers are added
	 */
    private void reproduce() {
        setResources(Resources.add(getResources(), getOwner().reproduction(this)));
    }

    /**
	 * Sets planet's name
	 * @param string planet's name
	 */
    public void setName(String string) {
        name = string;
    }

    /**
	 * Sets planet's owner
	 * @param empire planet's owner
	 */
    public void setOwner(Empire empire) {
        owner = empire;
    }

    /**
	 * Sets planet's resource levels
	 * @param resources planet's resources
	 */
    public void setResources(Resources resources) {
        this.resources = resources;
    }

    /**
	 * Retrieves the planet's BuildQueue
	 * @return BuildQueue responsible for the planet's construction
	 */
    public BuildQueue getQueue() {
        return queue;
    }

    public String toString() {
        return "Planet: " + getName() + (getOwner() != null ? ", owned by " + getOwner().getName() : "");
    }
}
