package netgraph;

/**
 * This interface represents koordinates in the NetView space. It makes it easy to switch
 * from a 2D (default) view to a 3D view.
 * @author  Dom
 * @version 
 */
public interface NetViewCoordinates {

    /** for normal visualization every coordinates should be convertable to 2D.
     * @return Koordinates2D This coordinates translated into 2D.
     */
    public Coordinates2D get2DCoordinates();

    /** Tests if these coordinates are equal to the given.
     * @param obj The object to test against.
     * @returns true if the two objects are equal.
     */
    public boolean equals(Object obj);
}
