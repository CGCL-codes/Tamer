package com.roslan.games.moo3d.pojo;

/**
 * @author Roslan Amir
 * @date Jun 25, 2009
 * 
 */
public class ShipType {

    /**
	 * The maximum number of slots of ship designs.
	 */
    public static final int TYPE_COUNT = 6;

    /**
	 * The name of this ship.
	 */
    private String name;

    /**
	 * The cost of building this ship in BC.
	 */
    private int cost;

    /**
	 * The warp speed of this ship type.
	 */
    private int warp;

    /**
	 * Constructor.
	 * 
	 * @param name
	 * @param cost
	 * @param warp
	 */
    public ShipType(String name, int cost, int warp) {
        this.name = name;
        this.cost = cost;
        this.warp = warp;
    }

    /**
	 * Getter for <code>name</code>.
	 * 
	 * @return String - The value to return.
	 */
    public String getName() {
        return name;
    }

    /**
	 * Setter for <code>name</code>.
	 * 
	 * @param name String - The new value to set.
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * Getter for <code>cost</code>.
	 * 
	 * @return int - The value to return.
	 */
    public int getCost() {
        return cost;
    }

    /**
	 * Setter for <code>cost</code>.
	 * 
	 * @param cost int - The new value to set.
	 */
    public void setCost(int cost) {
        this.cost = cost;
    }

    /**
	 * Getter for <code>warp</code>.
	 * 
	 * @return int - The value to return.
	 */
    public int getWarp() {
        return warp;
    }

    /**
	 * Setter for <code>warp</code>.
	 * 
	 * @param warp int - The new value to set.
	 */
    public void setWarp(int warp) {
        this.warp = warp;
    }
}
