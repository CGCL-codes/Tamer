package hu.openig.model;

import hu.openig.core.Func0;

/**
 * Contains game simulation related parameters.
 * @author akarnokd, 2011.12.20.
 */
public class Parameters {

    /** The speed factor callback. */
    protected final Func0<Integer> speed;

    /**
	 * Constructor. Initializes the speed factor callback field.
	 * @param speed the speed callback
	 */
    public Parameters(Func0<Integer> speed) {
        this.speed = speed;
    }

    /** @return the speed multiplier. */
    public int speed() {
        return speed.invoke();
    }

    /**
	 * @return the multiplier for radar-range in pixels for ground radars
	 */
    public int groundRadarUnitSize() {
        return 35;
    }

    /**
	 * @return the multiplier for radar-range in pixels for fleet radars
	 */
    public int fleetRadarUnitSize() {
        return 25;
    }

    /** @return the research speed in terms of money / simulation step. */
    public int researchSpeed() {
        return 8 * speed();
    }

    /** @return the production unit per simulation step. The lower the faster the production is. */
    public double productionUnit() {
        return 250d / speed();
    }

    /** @return the construction points per simulation step. */
    public int constructionSpeed() {
        return 20 * speed();
    }

    /** @return the construction cost per simulation step. Not used now. */
    public int constructionCost() {
        return 20 * speed();
    }

    /** @return the hitpoints improved per simulation step. */
    public int repairSpeed() {
        return 10 * speed();
    }

    /** @return the repair cost per simulation step. */
    public int repairCost() {
        return 2 * speed();
    }

    /** @return the denominator to compute hitpoints from costs in battles. */
    public int costToHitpoints() {
        return 10;
    }

    /**
	 * What is considered a planet-nearby.
	 * @return the distance
	 */
    public int nearbyDistance() {
        return 10;
    }

    /**
	 * @return the ratio between the fleet movement timer and speed() minute simulation time.
	 */
    public int simulationRatio() {
        return 4;
    }
}
