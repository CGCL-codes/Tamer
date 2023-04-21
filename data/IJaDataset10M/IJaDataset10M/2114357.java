package org.jazzteam.edu.patterns.decoratorStarbuzzCofe.components;

import org.jazzteam.edu.patterns.decoratorStarbuzzCofe.beverage.Beverage;
import org.jazzteam.edu.patterns.decoratorStarbuzzCofe.beverage.CondimentDecorator;

/**
 * Additional ingredient - soy
 * 
 * @author Hor1zonT
 * @version $Rev: $
 */
public class Soy extends CondimentDecorator {

    /**
	 * Variable for save link for beverage
	 */
    Beverage beverage;

    /**
	 * Constructor for set link of object
	 */
    public Soy(Beverage beverage) {
        this.beverage = beverage;
    }

    /**
	 * Return name off this additional ingredient and name of beverage
	 */
    @Override
    public String getDescription() {
        return beverage.getDescription() + ", Soy";
    }

    /**
	 * Calculate cost
	 */
    @Override
    public double cost() {
        return .36 + beverage.cost();
    }
}
