package com.teletalk.jserver.property;

/**
 * PropertyOwner class for handling properties that require calculation.
 * 
 * @author Tobias L�fstrand
 * 
 * @since Alpha  
 */
public interface CalculatedPropertyOwner extends PropertyOwner {

    /**
	 * Called to calculade properties that need calculation.
	 */
    public void calculateProperties();
}
