package com.riseOfPeople.gameObjects.units.flametank;

import com.riseOfPeople.gameObjects.units.Unit;

/**
 * @author Erwin
 *
 */
public class FlameTank extends Unit {

    /***
	 * Constructor.
	 * sets FlameTank specific variables
	 */
    public FlameTank() {
        super.setName("FlameTank");
        super.setObjectID();
        super.setAttackPoints(10);
        super.setDefencePoints(4);
        super.setHitPoints(200);
        super.setPercentage(super.getHitPoints() / 100);
        super.setRange(50);
        super.setFireRate(200);
    }
}
