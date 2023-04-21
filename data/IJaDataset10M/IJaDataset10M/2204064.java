package org.enkryption.starsClone.entity.buildable;

import org.enkryption.starsClone.Resources;
import org.enkryption.starsClone.entity.planet.Planet;

/**
 * Objects that are able to be built by planetary queues must implement this
 * interface.
 * 
 * All Buildables are instantiated on game intialization and are queried
 * to check if they can be built by a race and when a Buildable is completed at
 * a Planet.
 * @author Hsiu-Fan Wang
 */
public interface IBuildable {

    /**
	 * Construct the number of buildables produced at the passed location.
	 * @param number   number of buildables built
	 * @param location planet buildables were built at
	 */
    public void build(int number, Planet location);

    /**
	 * Checks whether a buildable can be constructed at particular planet.
	 * @param planet Planet buildable is to be constructed on
	 * @return if a buildable can be constructed
	 */
    public boolean checkPrereqs(Planet planet);

    /**
	 * Get the cost to build one of the buildables at a specific planet
	 * @param planet Planet buildable's cost is to be calculated for
	 * @return cost to build one buildable
	 */
    public Resources getCost(Planet planet);

    /**
	 * The maximum number of this buildable that can be built at a planet.
	 * (If the most a planet can hold is 10 buildings, and 2 have been built,
	 * maxBuildable will return 8)
	 * @param planet Planet the buildable is to be built at
	 * @return maximum number of a buildable that can be built at a planet
	 */
    public int maxBuildable(Planet planet);

    /**
	 * The "name" of the buildable (IE: "Planetary: Factory", or "Ship:
	 * Marauder")
	 * @return buildable's name
	 */
    public String name();
}
