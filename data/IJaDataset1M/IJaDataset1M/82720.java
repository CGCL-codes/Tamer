package com.bluebrim.layoutmanager;

/**
 * Defines the interface for the Placement policy that is to be
 * applied while selecting a rectangle from the collection
 * rectangles and while placing a rectangle into target container
 * @author Murali Ravirala
 * @version 1.0
 */
public interface PlacementPolicy {

    /**
	 * Checks if we can place any more elements into the given container
	 * @param container the container that is to be checked
	 * @return boolean  indicates whether we can place any more elements
	 */
    boolean canPlaceMoreElements(MrContainer container);

    /**
	* This method decides the rectangles that are to be placed into the target
	* and the actual locations for those locations
	* @param target  the target rectangle for the problem
	* @param collection  the collection of rectangles for this problem
	* @param c   the constraints to be followed while placing the collection rectangles
	* @param corner  specifies the corner where the rectangles have to be placed
	* @return Object  the config object for the solution containing the
	*                 rectangles that have been chosen and the actual locations
	*                 for them
	*/
    Object place(Object target, MrCollection collection, MrConstraint c, int corner);
}
