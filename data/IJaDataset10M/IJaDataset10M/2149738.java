package apollo.gui.genomemap;

/**
 * An interface representing a View object which has defined
 * extents eg a start and end base.
 */
public interface LinearViewI extends ViewI {

    /**
   * Sets the minimum and maximum limits for the extent
   */
    public void setLimits(int[] limits);

    /**
   * Sets the minimum limit for the extent
   */
    public void setMinimum(int min);

    /**
   * Sets the maximum limit for the extent
   */
    public void setMaximum(int max);

    /**
   * Gets the minimum and maximum limits
   */
    public int[] getLimits();

    /**
   * Get the minimum limit.
   */
    public int getMinimum();

    /**
   * Get the maximum limit.
   */
    public int getMaximum();

    /**
   * Set the centre position.
   */
    public void setCentre(int centre);

    /**
   * Get the maximum limit.
   */
    public int getCentre();

    /**
   * Set the ZoomFactor along the linear axis
   */
    public void setZoomFactor(double factor);

    /**
   * Get the range on the linear axis that is visible
   */
    public int[] getVisibleRange();

    public boolean areLimitsSet();

    public void setLimitsSet(boolean state);
}
