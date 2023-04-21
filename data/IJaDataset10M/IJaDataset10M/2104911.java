package org.jazzteam.edu.patterns.decoratorStarbuzzCofe.components;

import org.jazzteam.edu.patterns.decoratorStarbuzzCofe.beverage.Beverage;
import org.jazzteam.edu.patterns.decoratorStarbuzzCofe.beverage.CondimentDecorator;

/**
 * Additional ingredient - whip
 * 
 * @author Hor1zonT
 * @version $Rev: $
 */
public class Whip extends CondimentDecorator {

    /**
	 * Variable for save link for beverage
	 */
    Beverage beverage;

    /**
	 * Constructor for set link of object
	 */
    public Whip(Beverage beverage) {
        this.beverage = beverage;
    }

    /**
	 * Return name off this additional ingredient and name of beverage
	 */
    @Override
    public String getDescription() {
        return beverage.getDescription() + ", Whip";
    }

    /**
	 * Calculate cost
	 */
    @Override
    public double cost() {
        return .13 + beverage.cost();
    }
}
