package org.corrib.jonto.tagging.beans;

/**
 * Interface for ROI types
 * @author Jakub Demczuk
 *
 */
public interface ROI extends Clipping {

    /**
	 * Gets the x coordinate of this ROI
	 * @return <tt>int</tt> value of the x coordinate
	 */
    int getXCoordinate();

    /**
	 * Sets the x coordinate
	 * @param _xCoordinate <tt>int</tt> new value for x coordinate
	 */
    void setXCoordinate(int _xCoordinate);

    /**
	 * Gets the y coordinate of this ROI
	 * @return <tt>int</tt> value of the y coordinate
	 */
    int getYCoordinate();

    /**
	 * Sets the y coordinate
	 * @param _yCoordinate <tt>int</tt> new value for y coordinate
	 */
    void setYCoordinate(int _yCoordinate);
}
