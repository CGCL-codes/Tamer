package org.jazzteam.edu.patterns.decoratorStarbuzzCofe.beverage;

/**
 * Abstract class with one abstract method and one realized method
 * 
 * @author Hor1zonT
 * @version $Rev: $
 */
public abstract class Beverage {

    /**
	 * Name of beverage
	 */
    protected String description = "Unknown beverage";

    /**
	 * Get full name of beverage from component and all decorators
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * Get cost off beverage
	 */
    public abstract double cost();
}
