package game;

import javax.microedition.lcdui.Graphics;

/**
 * This interface specifies a Layer that the {@link ScrollingLayerManager} cam scroll over.
 * 
 * @author dirk
 */
public interface GameLayer {

    /**
     * Paints the layer on the specified graphics object with the specified width and height.
     * 
     * @param g the graphics object to paint on.
     * @param screenWidth the width to use.
     * @param screenHeight the height to use.
     */
    public void paint(Graphics g, int screenWidth, int screenHeight);

    /**
     * The total width of this layer.
     * @return the width.
     */
    public int sizeX();

    /**
     * The total height of this layer.
     * @return the height.
     */
    public int sizeY();
}
