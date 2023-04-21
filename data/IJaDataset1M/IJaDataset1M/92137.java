package com.c5corp.c5gps;

/**
* A CoordinateTranslation specifies some translation between two different
* WayPoints; base and baseTranslator. The base is a waypoint around which 
* the baseTranslator in other regions is defined. The baseTranslator
* data is an X,Y cartesian translation (maintaining north/south references) to 
* project the base data onto another location for phychogeographic purposes
* @see WayPoint
* @author  Brett Stalbaum
* @version 2.0
* @since 2.0
*/
public interface CoordinateTranslation {

    /** returns the base translator waypoint, relative to the terrain into which
	 * the data will be translated.
	 * @return the base WayPoint
	 */
    public WayPoint getBaseTranslator();

    /** returns the base waypoint point, relative to the original 
	 * or base terrain.
	 * @return the base WayPoint
	 */
    public WayPoint getBase();
}
