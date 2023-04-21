package org.zebrafish.fill;

import static org.apache.commons.lang.StringUtils.EMPTY;
import org.zebrafish.field.Color;

/**
 * Solid Fill area.
 * 
 * @author Max Chu
 * @author Joseph S. Kuo
 * @version $Revision: 25 $, $Date: 2008-09-05 06:20:13 -0400 (Fri, 05 Sep 2008) $
 * @since 0.1
 */
public class SolidFill extends AbstractFill {

    private String type;

    /**
	 * Constructs a <code>SolidFill</code> object.
	 * 
	 * @param type the fill type
	 * @param color the color of fill
	 * @throws IllegalArgumentException if the specified color is null
	 */
    public SolidFill(String type, Color color) {
        super(color);
        this.type = type;
    }

    /**
	 * Returns the fill type which 'bg' is for background fill, 'c' for chart 
	 * area fill or 'a' to apply transparency to the whole chart.
	 * 
	 * @return the fill type
	 */
    public String getType() {
        return type;
    }

    /**
	 * Sets the fill type which 'bg' is for background fill, 'c' for chart 
	 * area fill or 'a' to apply transparency to the whole chart.
	 * 
	 * @param type the fill type
	 */
    public void setType(String type) {
        this.type = type;
    }

    public final String toURLString() {
        if (getColor() == null) {
            return EMPTY;
        }
        return getType() + ",s," + getColor().toURLString();
    }
}
