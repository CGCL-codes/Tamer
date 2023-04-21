package de.rockon.fuzzy.controller.operators.defuzzyfy;

import de.rockon.fuzzy.controller.model.FuzzyPoint;
import de.rockon.fuzzy.controller.model.FuzzySet;

/**
 * Defuzzyfizierungsalgorithmus welcher das letzte Maximum ermittelt
 */
public class LastMaximumDefuzzyfier extends AbstractDefuzzyfier {

    /**
	 * Konstruktor
	 */
    public LastMaximumDefuzzyfier(FuzzySet resultSet) {
        super(resultSet);
    }

    @Override
    public double defuzzyfy() {
        double maxY = -1;
        boolean lower = false;
        for (FuzzyPoint point : resultSet) {
            if (point.getY() < maxY) {
                lower = true;
            }
            if (point.getY() > maxY) {
                lower = false;
            }
            if (point.getY() >= maxY && !lower) {
                maxY = point.getY();
                resultX = point.getX();
            }
        }
        return resultX;
    }

    @Override
    public String getDescription() {
        return "Defuzzyfizierungsalgorithmus welcher das letzte Maximum ermittelt";
    }

    @Override
    public String toString() {
        return "LastMaximum";
    }
}
