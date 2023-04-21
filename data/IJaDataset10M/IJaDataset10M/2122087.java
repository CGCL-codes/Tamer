package org.shapelogic.color;

import java.util.Collection;

/** ColorHypothesis interface. <br />
 * 
 * @author Sami Badawi
 *
 */
public interface ColorHypothesis {

    Collection<IColorAndVariance> getColors();

    /** Try to add a color. <br />
	 * 
	 * This might cause a merge instead of a new color.
	 * 
	 * Return true if a new color was created.
	 * 
	 * */
    boolean addColor(IColorAndVariance color);

    IColorAndVariance getBackground();

    void setBackground(IColorAndVariance color);

    /** There will not always be a global maxDistance.<br />
	 * 
	 *  */
    double getMaxDistance();

    void setMaxDistance(double maxDistance);

    /** Is it possible to merge the 2 colors in the given context. */
    boolean mergable(IColorAndVariance color1, IColorAndVariance color2);
}
