package de.matthiasmann.twl.renderer;

/**
 * A cached text block interface
 * 
 * @author Matthias Mann
 */
public interface FontCache extends Resource {

    /**
     * Returns the width in pixels of the cached text block
     * @return the width in pixels of the cached text block
     */
    public int getWidth();

    /**
     * Returns the height in pixels of the cached text block
     * @return the height in pixels of the cached text block
     */
    public int getHeight();

    /**
     * Draw the cached text block at the given coordinates with the given color
     * @param as A time source for animation - may be null
     * @param x the left coordinate
     * @param y the top coordinate
     */
    public void draw(AnimationState as, int x, int y);
}
