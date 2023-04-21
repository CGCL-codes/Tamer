package ch.fusun.baron.unit.service;

import java.util.Collection;
import java.util.Map;
import ch.fusun.baron.data.DataProvider;
import ch.fusun.baron.map.Tile;
import ch.fusun.baron.unit.Unit;

/**
 * Service for all unit related problems
 */
public interface UnitService extends DataProvider {

    /**
	 * @param tile
	 *            The tile
	 * @return All units on the tile
	 */
    Collection<Unit> getUnits(Tile tile);

    /**
	 * @param location
	 *            The location
	 * @param numberOfUnits
	 *            The number of units
	 * @return The newly created unit on that tile
	 */
    Unit createUnit(Tile location, int numberOfUnits);

    /**
	 * @param tileUnitMap
	 *            the entiry unit map
	 */
    void setUnitMap(Map<Tile, Collection<Unit>> tileUnitMap);

    /**
	 * @param unit
	 *            The unit
	 * @return The tile of the unit
	 */
    Tile getLocation(Unit unit);

    /**
	 * @param unit
	 *            The unit
	 * @param destination
	 *            The new tile
	 */
    void moveUnit(Unit unit, Tile destination);

    /**
	 * @param unit
	 *            The unit
	 * @return true iff the unit exists
	 */
    boolean unitExists(Unit unit);

    /**
	 * @param unit
	 *            The unit to remove
	 */
    void removeUnit(Unit unit);

    /**
	 * @return All units in the world
	 */
    Collection<Unit> getAllUnits();

    /**
	 * @param index
	 *            The index for unique unit creation
	 */
    void setIndex(int index);

    /**
	 * @param location
	 * @param unit
	 */
    public void addUnitToLocation(Tile location, Unit unit);
}
