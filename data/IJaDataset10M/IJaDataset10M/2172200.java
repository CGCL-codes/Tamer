package uchicago.src.sim.space;

import java.util.Iterator;
import java.util.List;

/**
 * Interface for grids and tori whose cells can hold more than one
 * object.
 *
 * @version $Revision: 1.5 $ $Date: 2004/11/03 19:50:57 $
 */
public interface IMulti2DGrid extends Discrete2DSpace {

    /**
     * Gets the List of objects at the specified coordinates. An ordered
     * torus will return the first object inserted at the beginning of the
     * list and the last object inserted at the end of the list. The
     * list order is undetermined for an unordered torus.
     */
    public List getObjectsAt(int x, int y);

    /**
     * Gets the iterator for the collection of objects at the specified
     * coordinates. For an ordered torus the order of iteration will be first
     * object inserted, first returned and so on. For an unordered torus,
     * order is undefined.
     */
    public Iterator getIteratorAt(int x, int y);

    /**
     * Gets the Cell object at the specified coordinates.
     */
    public Cell getCellAt(int x, int y);

    /**
     * Gets the size (number of occupants) of the cell at
     * the specified location.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public int getCellSizeAt(int x, int y);

    /**
     * Removes the specified object from the specified location.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param obj the object to remove
     */
    public void removeObjectAt(int x, int y, Object obj);
}
