package ro.mosc.reco.test;

import ro.mosc.reco.algebra.RelationResolver;

/**
 * Sample test class
 */
public class LessEqRelationResolver implements RelationResolver {

    public static final double MAX_DISTANCE = 1000;

    public double getRelationValue(Object[] elements) {
        if (((Number) elements[0]).doubleValue() <= ((Number) elements[1]).doubleValue()) {
            return 1 - (Math.abs(((Number) elements[0]).doubleValue() - ((Number) elements[1]).doubleValue()) / MAX_DISTANCE);
        }
        return 0;
    }
}
