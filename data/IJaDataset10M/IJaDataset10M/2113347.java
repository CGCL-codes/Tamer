package java.awt;

/**
 * This encapsulates information about the display mode for a graphics
 * device configuration. They are device dependent, and may not always be
 * available.
 *
 * @author Eric Blake (ebb9@email.byu.edu)
 * @see GraphicsDevice
 * @since 1.4
 * @status updated to 1.4
 */
public final class DisplayMode {

    /**
   * Value of the bit depth if multiple depths are supported.
   *
   * @see #getBitDepth()
   */
    public static final int BIT_DEPTH_MULTI = -1;

    /**
   * Value of an unknown refresh rate.
   *
   * @see #getRefreshRate()
   */
    public static final int REFRESH_RATE_UNKNOWN = 0;

    /** The width. */
    private final int width;

    /** The height. */
    private final int height;

    /** The bit depth. */
    private final int bitDepth;

    /** The refresh rate. */
    private final int refreshRate;

    /**
   * Create a mode with the given parameters.
   *
   * @param width the width
   * @param height the height
   * @param bitDepth the bitDepth
   * @param refreshRate the refreshRate
   * @see #BIT_DEPTH_MULTI
   * @see #REFRESH_RATE_UNKNOWN
   */
    public DisplayMode(int width, int height, int bitDepth, int refreshRate) {
        this.width = width;
        this.height = height;
        this.bitDepth = bitDepth;
        this.refreshRate = refreshRate;
    }

    /**
   * Returns the height, in pixels.
   *
   * @return the height
   */
    public int getHeight() {
        return height;
    }

    /**
   * Returns the width, in pixels.
   *
   * @return the width
   */
    public int getWidth() {
        return width;
    }

    /**
   * Returns the bit depth, in bits per pixel. This may be BIT_DEPTH_MULTI.
   *
   * @return the bit depth
   * @see #BIT_DEPTH_MULTI
   */
    public int getBitDepth() {
        return bitDepth;
    }

    /**
   * Returns the refresh rate, in hertz. This may be REFRESH_RATE_UNKNOWN.
   *
   * @return the refresh rate
   * @see #REFRESH_RATE_UNKNOWN
   */
    public int getRefreshRate() {
        return refreshRate;
    }

    /**
   * Test for equality. This returns true for two modes with identical
   * parameters.
   *
   * @param dm The display mode to compare to
   *
   * @return true if it is equal
   */
    public boolean equals(DisplayMode dm) {
        return (width == dm.width && height == dm.height && bitDepth == dm.bitDepth && refreshRate == dm.refreshRate);
    }

    /**
   * Returns a hash code for the display mode.
   *
   * @return the hash code
   */
    public int hashCode() {
        return width + height + bitDepth + refreshRate;
    }
}
