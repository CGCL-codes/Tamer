package edu.ou.mlfw.gui;

import java.awt.Graphics2D;
import java.util.*;
import edu.ou.utils.Vector2D;

/**
 * Shadow2D provides a way for clients or whatever else to draw
 * on a Shadow2DCanvas.  The main consideration for a Shadow2D is that the
 * Shadow2DCanvas be able to find its current "real" position as well as
 * set its current "draw" position.  A Shadow2D also needs to specify the width
 * and height of a bounding box.  This bounding box should but does not have to
 * be respected by the draw method; however, respecting this boundry box can
 * allow a specific implementation of Shadow2DCanvas to provide some convenient
 * handling for special-case drawing conditions (for instance, wrapping a
 * drawing around the borders of a toroidal environment).
 */
public abstract class Shadow2D {

    public static Iterable<Shadow2D> EMPTY_ITER = new Iterable<Shadow2D>() {

        private List<Shadow2D> list = new ArrayList<Shadow2D>();

        public Iterator<Shadow2D> iterator() {
            return list.iterator();
        }
    };

    protected Vector2D drawposition;

    protected Vector2D realposition;

    private final int width, halfwidth, height, halfheight;

    /**
     * Initialize the shadow with a bounding box, calculate the halfwidth and
     * halfheight
     *
     * @param width The width of the bounding box.
     * @param height The height of the bounding box.
     */
    public Shadow2D(final int width, final int height) {
        this.width = width;
        halfwidth = width / 2;
        this.height = height;
        halfheight = height / 2;
    }

    /**
     * @return The width of the shadow's bounding box.
     */
    public final int getWidth() {
        return width;
    }

    /**
     * @return The halfwidth of the shadow's bounding box.
     */
    public final int getHalfWidth() {
        return halfwidth;
    }

    /**
     * @return The height of the shadow's bounding box.
     */
    public final int getHeight() {
        return height;
    }

    /**
     * @return The halfheight of the shadow's bounding box
     */
    public final int getHalfHeight() {
        return halfheight;
    }

    /**
     * Centers the bounding box around a particular point for drawing.  This
     * should only need to be used by Shadow2DCanvav, but is available if you
     * want it for something else.  The draw position gets set back to
     * the real position after the draw routine has finished.
     *
     * @param pos The new drawing position.
     */
    public final void setDrawPosition(final Vector2D pos) {
        drawposition = pos;
    }

    /**
     * @return The current drawing position.
     */
    public final Vector2D getDrawPosition() {
        return drawposition;
    }

    /**
     * Reset the drawing position to the real position.
     */
    public final void resetDrawPosition() {
        drawposition = getRealPosition();
    }

    /**
     * JSpacewarComponent uses this method in conjunction with the bounding box
     * information to determine if the graphic needs to be redrawn to account
     * for wrapping.  Real position is the position that resetDrawPosition sets
     * the draw position to.
     *
     * @return The current "real" center position of the bounding box.
     */
    public abstract Vector2D getRealPosition();

    /**
     * Tell the Shadow2DCanvas to draw or not draw this shadow.  This does
     * not remove the shadow, it just keeps it from being drawn.
     *
     * @return Whether the shadow should be drawn or not.
     */
    public abstract boolean drawMe();

    /**
     * The actual drawing routine.
     */
    public abstract void draw(Graphics2D g);

    /**
     * A convenience method that gets called after all the drawings are
     * complete and the draw position is reset to the real position.
     */
    public abstract void cleanUp();
}
