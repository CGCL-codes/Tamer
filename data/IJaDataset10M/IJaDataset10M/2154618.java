package com.lordjoe.collectiveintelligence;

/**
 * com.lordjoe.collectiveintelligence.FlightCost
 *
 * @author Steve Lewis
 * @date Mar 24, 2009
 */
public class FlightItineryCost implements ICostFunction<FlightItinery> {

    public static FlightItineryCost[] EMPTY_ARRAY = {};

    public static Class THIS_CLASS = FlightItineryCost.class;

    public static final FlightItineryCost INSTANCE = new FlightItineryCost();

    public static final double DOLLAR_COST_FACTOR = 1.0;

    public static final double MINUTE_WAIT_COST_FACTOR = 0.1;

    private FlightItineryCost() {
    }

    public double computeCost(FlightItinery flt, Object... otherData) {
        return ItineryCost.INSTANCE.computeCost(flt.getItems());
    }

    public double lowestCost() {
        return 0;
    }
}
