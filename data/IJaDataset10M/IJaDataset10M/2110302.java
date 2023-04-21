package uchicago.src.sim.space;

import java.awt.Dimension;
import uchicago.src.collection.BaseMatrix;

/**
 * Interface for all Discrete two dimensional spaces.
 *
 * @author Nick Collier
 * @version $Revision: 1.5 $ $Date: 2004/11/03 19:50:57 $
 */
public interface Discrete2DSpace {

    public static final int VON_NEUMANN = 0;

    public static final int MOORE = 1;

    /**
     * Gets the size of the x dimension
     */
    public int getSizeX();

    /**
     * Gets the size of the y dimension
     */
    public int getSizeY();

    /**
     * Gets the dimension of the space
     */
    public Dimension getSize();

    /**
     * Gets the Object at the specified coordinate.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the Object at x,y
     */
    public Object getObjectAt(int x, int y);

    /**
     * Gets the value at the specified coordinate if appropriate.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the value at x, y
     */
    public double getValueAt(int x, int y);

    /**
     * Puts the specified Object at the specified coordinate.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param object the object to put
     */
    public void putObjectAt(int x, int y, Object object);

    /**
     * Puts the specified value at the specified coordinate.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param value the value to put at x,y
     */
    public void putValueAt(int x, int y, double value);

    /**
     * Gets the matrix collection class that contains all the values
     */
    public BaseMatrix getMatrix();
}
