package org.elogistics.calculation.distance;

import org.elogistics.domain.entities.location.City;

/**
 * The Strategie-Interface to declare Objects that calculate
 * the Distance between two cities.
 * 
 * It can be used to calculate the distance based on kilometers or
 * miles, for instance. 
 * 
 * @author Jurkschat, Oliver
 *
 */
public interface IDistanceCalculatorStrategie {

    /**
	 * Calculates the distance between two cities, based on their 
	 * coordinates in longitude / latitude.
	 * 
	 * @param citya	The first city.
	 * @param cityb	The second city.
	 * 
	 * @return	The Distance between them. 	
	 * 			The unit-length depends on the concrete implementation. (Km / Miles / feet / ...)
	 * 
	 */
    public double calculateDistance(City citya, City cityb);
}
