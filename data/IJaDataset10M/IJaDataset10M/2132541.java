package org.pushingpixels.substance.internal.utils.border;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.border.Border;
import javax.swing.plaf.UIResource;

/**
 * Base class for button borders in <b>Substance </b> look-and-feel. This class
 * is <b>for internal use only</b>.
 * 
 * @author Kirill Grouchnikov
 */
public abstract class SubstanceButtonBorder implements Border, UIResource {

    /**
	 * The associated button shaper class.
	 */
    private Class<?> buttonShaperClass;

    /**
	 * Simple constructor.
	 * 
	 * @param buttonShaperClass
	 *            The associated button shaper class.
	 */
    public SubstanceButtonBorder(Class<?> buttonShaperClass) {
        this.buttonShaperClass = buttonShaperClass;
    }

    public boolean isBorderOpaque() {
        return false;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
    }

    /**
	 * Returns the associated button shaper class.
	 * 
	 * @return The associated button shaper class.
	 */
    public Class<?> getButtonShaperClass() {
        return this.buttonShaperClass;
    }
}
