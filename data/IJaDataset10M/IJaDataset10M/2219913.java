package org.xith3d.ui.hud.layout;

/**
 * Most {@link LayoutManager}s can be used to define borders in the whole
 * container. This can be interpreted as a padding for the container.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public interface BorderSettableLayoutManager {

    /**
     * Sets the border (padding) width at the container's bottom edge.
     * 
     * @param border
     */
    public void setBorderBottom(float border);

    /**
     * @return the border (padding) width at the container's bottom edge.
     */
    public float getBorderBottom();

    /**
     * Sets the border (padding) width at the container's right edge.
     * 
     * @param border
     */
    public void setBorderRight(float border);

    /**
     * @return the border (padding) width at the container's right edge.
     */
    public float getBorderRight();

    /**
     * Sets the border (padding) width at the container's top edge.
     * 
     * @param border
     */
    public void setBorderTop(float border);

    /**
     * @return the border (padding) width at the container's top edge.
     */
    public float getBorderTop();

    /**
     * Sets the border (padding) width at the container's left edge.
     * 
     * @param border
     */
    public void setBorderLeft(float border);

    /**
     * @return the border (padding) width at the container's left edge.
     */
    public float getBorderLeft();
}
