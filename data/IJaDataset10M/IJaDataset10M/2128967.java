package org.inigma.utopia.calculator;

public class DefensiveEfficiency {

    private final double normal;

    private double aggression;

    private double townWatch;

    public DefensiveEfficiency(double normal) {
        this.normal = normal;
    }

    public double getAggression() {
        return aggression;
    }

    public double getNormal() {
        return normal;
    }

    public double getTownWatch() {
        return townWatch;
    }

    public void setAggression(double aggression) {
        this.aggression = aggression;
    }

    public void setTownWatch(double townWatch) {
        this.townWatch = townWatch;
    }
}
