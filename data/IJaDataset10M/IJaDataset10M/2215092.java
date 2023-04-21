package com.myBrewingBuddy.core.calculators;

/**
 *
 * @author Ben Ahrens
 */
public class SpecificGravity implements ISpecificGravity {

    /**
     * Converts degrees plato to specific gravity.
     * @param plato Degrees plato.
     * @return Specific gravity.
     */
    public double platoToSpecificGravity(double plato) {
        double result = 0.0;
        result = (plato / (258.6 - ((plato / 258.2) * 227.1))) + 1;
        return result;
    }

    /**
     * Gets the gravity points based on the percentage extract, weight of the fermentable, and the efficiency.
     * @param percentExtract The percentage extract of the fermentable (50% should be passed as 0.50).
     * @param weight The weight of the fermentable.
     * @param efficiency The efficiency for extracting from the fermentable (50% should be passed as 0.50).
     * @return The gravity points.
     */
    public double getGravityPointsForWeight(double percentExtract, double weight, double efficiency) {
        double result = 0;
        result = getGravityPointsForWeight(percentExtract, weight) * efficiency;
        return result;
    }

    /**
     * Gets the gravity points based on the percentage extract and the weight of the fermentable.
     * @param percentExtract The percentage extract of the fermentable (50% should be passed as 0.50).
     * @param weight The weight of the fermentable.
     * @return The gravity points.
     */
    public double getGravityPointsForWeight(double percentExtract, double weight) {
        double result = 0;
        result = getGravityPoints(percentExtract) * weight;
        return result;
    }

    /**
     * Gets the number of gravity points based on the percentage of extract and the efficiency.
     * @param percentExtract The percentage extract as a decimal (50% should be passed as 0.50).
     * @param efficiency The percentage extract as a decimal (50% should be passed as 0.50).
     * @return The number of gravity points.
     */
    public double getGravityPoints(double percentExtract, double efficiency) {
        double result = 0;
        result = getGravityPoints(percentExtract) * efficiency;
        return result;
    }

    /**
     * Gets the number of gravity points based on the percentage of extract.
     * @param percentExtract The percentage extract as a decimal (50% should be passed as 0.50).
     * @return The number of gravity points.
     */
    public double getGravityPoints(double percentExtract) {
        double result = 0;
        double plato = ((percentExtract * 100) / 8.6);
        result = (platoToSpecificGravity(plato) - 1) * 1000;
        return result;
    }
}
