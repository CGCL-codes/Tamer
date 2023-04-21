package org.proxywars.engine.scene.shape;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Quad;
import com.jme.system.DisplaySystem;

/**
 * Convenience class for making a Rectangle in 3d space.
 * I named the class Rectangle3d as to not get confused with classes:
 * <ul> 
 * <li>math.jme.Rectangle</li>
 * <li>java.awt.geom.Rectangle2D</li>
 * </ul>
 * @author Steven DeVries
 */
public class Rectangle3d extends Quad {

    private static final long serialVersionUID = 1L;

    protected Vector2f origin = new Vector2f(0, 0);

    /**
     * Convenience constructor for child classes.
     * Should never be used in actual code.
     */
    public Rectangle3d() {
    }

    /**
     * Creates a white, 200x100 rectangle at (0,0).
     * 
     * @param name     Name of the rectangle.
     */
    public Rectangle3d(String name) {
        initialize(name, 0, 0, 200, 100);
    }

    /**
     * Creates a white rectangle of specified size at (0,0).
     * 
     * @param name     Name of the rectangle.
     * @param width     width of the rectangle.
     * @param height    height of the rectangle.
     */
    public Rectangle3d(String name, int width, int height) {
        initialize(name, 0, 0, width, height);
    }

    /**
     * Creates a white rectangle with the specified location and size.
     * 
     * @param name     Name of the rectangle.
     * @param x           x location of top left corner of the rectangle.
     * @param y            y location of the top left corner of the rectangle.
     * @param width     width of the rectangle.
     * @param height    height of the rectangle.
     */
    public Rectangle3d(String name, int x, int y, int width, int height) {
        initialize(name, x, y, width, height);
    }

    /**
     * Convenience function so that all the constructors just call one function.
     * Makes it easier to change/update or for children to initialize all the values as well.
     */
    protected void initialize(String name, int x, int y, int width, int height) {
        initialize((float) width, (float) height);
        setName(name);
        setLocation(x, y);
        setRenderQueueMode(Renderer.QUEUE_ORTHO);
        setLightCombineMode(Spatial.LightCombineMode.Off);
        setTextureCombineMode(Spatial.TextureCombineMode.CombineClosest);
        setColor(new ColorRGBA(1f, 1f, 1f, 1f));
    }

    /**
     * Returns the x position of the top left corner of the Rectangle 
     * in terms of a traditional (x,y) coordinates as opposed to OpenGL's
     * (u,v) coordinates.
     * 
     * @return the x position of the top left corner.
     */
    public int getX() {
        return (int) origin.x;
    }

    /**
     * Returns the y position of the top left corner of the Rectangle 
     * in terms of a traditional (x,y) coordinates as opposed to OpenGL's
     * (u,v) coordinates.
     * 
     * @return the y position of the top left corner.
     */
    public int getY() {
        return (int) origin.y;
    }

    /**
     * Sets the color of this Rectangle.
     */
    public void setColor(ColorRGBA color) {
        setDefaultColor(color);
        updateRenderState();
    }

    /**
     * Sets the location of the rectangle on the screen.
     * This function uses traditional (x, y) coordinates, (0,0) being top left corner.
     * The top, left corner of the rectangle will be at the specified point.
     * 
     * @param x     horizontal starting point.
     * @param y     vertical starting point.
     */
    public void setLocation(int x, int y) {
        origin.x = (float) x;
        origin.y = (float) y;
        float u = origin.x + width / 2;
        float v = (float) DisplaySystem.getDisplaySystem().getHeight() - (origin.y + height / 2);
        setLocalTranslation(new Vector3f(u, v, 0));
        updateRenderState();
    }

    /**
     * Sets the size of the rectangle in pixels on screen.
     * 
     * @param width     width of the rectangle.
     * @param height    height of the rectangle.
     */
    public void setSize(int width, int height) {
        resize((float) width, (float) height);
        updateRenderState();
    }
}
