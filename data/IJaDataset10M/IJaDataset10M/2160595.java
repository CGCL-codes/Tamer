package org.zebrafish.field.marker;

import org.zebrafish.field.Color;
import org.zebrafish.field.Field;
import org.zebrafish.util.ChartHelper;

/**
 * Marker.
 * 
 * @author Max Chu
 * @author Joseph S. Kuo
 * @version $Revision: 24 $, $Date: 2008-09-05 06:15:32 -0400 (Fri, 05 Sep 2008) $
 * @since 0.1
 */
public interface Marker extends Field {

    /**
	 * Returns the marker type.
	 * 
	 * Note: each implementation of <code>Marker</code> may have different kind
	 * of types.
	 * 
	 * @return the marker type
	 */
    public String getType();

    /**
	 * Sets the marker type. This method will invoke 
	 * {@link ChartHelper#getLegalSingleLetter(String, String, boolean)}
	 * to get a legal type specified in {@link #getTypes()}.
	 * 
	 * Note: each implementation of <code>Marker</code> may have different kind
	 * of types.
	 * 
	 * @param type the marker type
	 * @throws IllegalArgumentException if {@link #getTypes()} returns a empty
	 * 		string
	 * @see ChartHelper#getLegalSingleLetter(String, String, boolean)
	 */
    public void setType(String type);

    /**
	 * Returns the color of this marker.
	 * 
	 * @return the color of this marker
	 */
    public Color getColor();

    /**
	 * Sets the color of this marker.
	 * 
	 * @param color the color of this marker
	 * @throws IllegalArgumentException if the specified color is null
	 */
    public void setColor(Color color);

    /**
	 * Returns a string contains all types of this <code>Marker</code>.
	 *  
	 * @return a string contains all types of this <code>Marker</code>
	 */
    public String getTypes();
}
