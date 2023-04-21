package net.sourceforge.circuitsmith.objects;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import net.sourceforge.circuitsmith.layers.EdaLayerList;
import net.sourceforge.circuitsmith.objects.EdaObject.DrawMode;

public interface EdaDrawable {

    /**
     * This renders the object onto the graphics context Note that many subclasses don't need to overide this. They just overide
     * <code>draw(g,t)</code>
     * @param graphics the graphics context to render onto
     * @param transform the transform to apply to the object before rendering
     * @param mode the mode to render in
     * @param layers the layers which are currently in effect.
     * @see #draw
     */
    public abstract void draw(Graphics2D graphics, AffineTransform transform, final DrawMode mode, final EdaLayerList layers);

    /**
     * Put the bounding rectangle of this object into the passed rectangle.
     * <p>
     * Note that it might want to return a <code>Shape</code> in the future.
     * @param scale TODO
     * @param An rectangle to fill with ones bounds.
     * @return true, if the rectangle contains valid bounds. false, if the corresponding object is invisible.
     */
    public abstract boolean getBounds(Rectangle2D.Float rectangle, float scale);
}
