package org.zebrafish.fill;

import org.apache.commons.lang.StringUtils;
import org.zebrafish.field.Color;

/**
 * Gradient Fill Area.
 * 
 * @author Max Chu
 * @author Joseph S. Kuo
 * @version $Revision: 25 $, $Date: 2008-09-05 06:20:13 -0400 (Fri, 05 Sep 2008) $
 * @since 0.1
 */
public class GradientFill extends AbstractFill {

    private Float offset;

    /**
	 * Constructs a <code>GradientFill</code> object.
	 * 
	 * @param color the color of fill
	 * @param offset the offset of fill
	 * @throws IllegalArgumentException if the specified color is null
	 */
    public GradientFill(Color color, Float offset) {
        super(color);
        setOffset(offset);
    }

    /**
	 * Returns what point the color is pure where: 0 specifies the right-most 
	 * chart position and 1 the left-most.
	 * 
	 * @return what point the color is pure
	 */
    public Float getOffset() {
        return offset;
    }

    /**
	 * Sets what point the color is pure where: 0 specifies the right-most 
	 * chart position and 1 the left-most.
	 * 
	 * @param offset what point the color is pure
	 */
    public void setOffset(Float offset) {
        this.offset = offset;
    }

    @Override
    public String toURLString() {
        return getColor() != null && getOffset() != null ? getColor().toURLString() + "," + getOffset() : StringUtils.EMPTY;
    }
}
