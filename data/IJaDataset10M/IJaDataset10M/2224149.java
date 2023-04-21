package org.jazzteam.edu.patterns.decoratorStarbuzzCofe.decorators;

import org.jazzteam.edu.patterns.decoratorStarbuzzCofe.beverage.Beverage;

/**
 * Beverage dark roast without ingredients
 * 
 * @author Hor1zonT
 * @version $Rev: $
 */
public class DarkRoast extends Beverage {

    /**
	 * Constructor, set name
	 */
    public DarkRoast() {
        description = "Dark Roast";
    }

    /**
	 * Return cost only of this beverage
	 */
    @Override
    public double cost() {
        return 2.01;
    }
}
