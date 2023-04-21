package ch.blackspirit.graphics;

/**
 * A display mode is a set of properties to control native fullscreen
 * resolution and color depth.
 * @author Markus Koller
 */
public interface DisplayMode {

    /**
	 * @return Height in pixels. 
	 */
    public int getHeight();

    /**
	 * @return Width in pixels. 
	 */
    public int getWidth();

    /**
	 * @return Color depth in bits. 
	 */
    public int getColorDepth();

    /**
	 * @return Color refresh rate in hz (1/s). 
	 */
    public int getRefreshRate();
}
