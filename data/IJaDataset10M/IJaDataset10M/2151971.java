package org.strategyca.scenario.world;

import org.strategyca.scenario.util.IdClass;
import org.strategyca.scenario.economics.ResourceTable;

/**
 * @author Adolfo
 *
 */
public class Terrain extends IdClass {

    /** Base movement cost of the hex */
    private Integer movementCost = 1;

    /** Base production the hex */
    private ResourceTable production = new ResourceTable();

    /** River info of the hex */
    private Integer[] rivers = new Integer[6];

    /** Road info of the hex */
    private Integer[] roads = new Integer[6];

    /** Bridge info of the hex */
    private Integer[] bridges = new Integer[6];

    /**
	 * Constructor
	 */
    public Terrain() {
        super();
    }

    /**
	 * @return Returns the bridges.
	 */
    public Integer[] getBridges() {
        return this.bridges;
    }

    /**
	 * @param bridges The bridges to set.
	 */
    public void setBridges(Integer[] bridges) {
        this.bridges = bridges;
    }

    /**
	 * @return Returns the movementCost.
	 */
    public Integer getMovementCost() {
        return this.movementCost;
    }

    /**
	 * @param movementCost The movementCost to set.
	 */
    public void setMovementCost(Integer movementCost) {
        this.movementCost = movementCost;
    }

    /**
	 * @return Returns the production.
	 */
    public ResourceTable getProduction() {
        return this.production;
    }

    /**
	 * @param production The production to set.
	 */
    public void setProduction(ResourceTable production) {
        this.production = production;
    }

    /**
	 * @return Returns the rivers.
	 */
    public Integer[] getRivers() {
        return this.rivers;
    }

    /**
	 * @param rivers The rivers to set.
	 */
    public void setRivers(Integer[] rivers) {
        this.rivers = rivers;
    }

    /**
	 * @return Returns the roads.
	 */
    public Integer[] getRoads() {
        return this.roads;
    }

    /**
	 * @param roads The roads to set.
	 */
    public void setRoads(Integer[] roads) {
        this.roads = roads;
    }
}
