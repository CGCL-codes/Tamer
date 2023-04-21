package de.enough.polish.ui;

import de.enough.polish.io.Serializable;
import de.enough.polish.util.RgbImage;

/**
 * <p>Provides an RGB filter that transforms RGB data in a specific way.</p>
 *
 * <p>Copyright Enough Software 2008</p>
 * @author Robert Virkus, j2mepolish@enough.de
 */
public abstract class RgbFilter implements Serializable {

    /**
	 * Creates a new filter
	 */
    public RgbFilter() {
    }

    /**
	 * Processes the given RGB input
	 * @param input the RgbImage input
	 * @return the RgbImage output
	 */
    public abstract RgbImage process(RgbImage input);

    /**
	 * Determines whether this filter is active.
	 * An opacity filter is for example not active, when the opacity value is 255
	 * @return true when this RGB filter is active.
	 */
    public abstract boolean isActive();

    /**
	 * Configures this filter
	 * @param style the style
	 * @param resetStyle true when default values should be assumed, may be ignored by subclasses
	 */
    public void setStyle(Style style, boolean resetStyle) {
    }

    /**
	 * Releases all memory intensive resources
	 */
    public void releaseResources() {
    }
}
