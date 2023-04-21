package org.nightlabs.calendar.ui;

import org.eclipse.swt.graphics.RGB;

/**
 * This class holds either an RGB value or a system
 * color reference integer.
 * @author Marc Klinger - marc[at]nightlabs[dot]de
 */
public class ColorDescriptor {

    private Integer systemColorId = null;

    private RGB rgb = null;

    public ColorDescriptor(int systemColorId) {
        this.systemColorId = systemColorId;
    }

    public ColorDescriptor(RGB rgb) {
        if (rgb == null) throw new IllegalArgumentException("rgb is null");
        this.rgb = rgb;
    }

    public ColorDescriptor(int red, int green, int blue) {
        this.rgb = new RGB(red, green, blue);
    }

    /**
	 * Whether this represents a system color.
	 * @return <code>true</code> if this instance represents a system color - 
	 * 		<code>false</code> otherwise.
	 */
    public boolean isSystemColor() {
        return systemColorId != null;
    }

    /**
	 * Get the system color id.
	 * @return the system color id or <code>null</code> if this is not a system color
	 */
    public Integer getSystemColorId() {
        return systemColorId;
    }

    /**
	 * Get the rgb.
	 * @return the rgb or <code>null</code> if this is a system color.
	 */
    public RGB getRgb() {
        return rgb;
    }
}
