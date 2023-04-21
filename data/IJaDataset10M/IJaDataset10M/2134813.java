package net.nexttext.behaviour.dform;

import net.nexttext.TextObjectGlyph;
import net.nexttext.property.PVectorListProperty;
import net.nexttext.property.PVectorProperty;
import java.awt.Rectangle;
import java.util.Iterator;
import processing.core.PVector;

public class Scale extends DForm {

    private float scale;

    /**
     * @param scale is amount the object's size will increase, as a multiplier. 
     */
    public Scale(float scale) {
        this.scale = scale;
    }

    public ActionResult behave(TextObjectGlyph to) {
        PVector toAbsPos = to.getPositionAbsolute();
        Rectangle bb = to.getBoundingPolygon().getBounds();
        PVector center = new PVector((float) bb.getCenterX(), (float) bb.getCenterY());
        center.sub(toAbsPos);
        PVectorListProperty cPs = getControlPoints(to);
        Iterator<PVectorProperty> i = cPs.iterator();
        while (i.hasNext()) {
            PVectorProperty cP = i.next();
            PVector p = cP.get();
            p.sub(center);
            p.mult(scale);
            p.add(center);
            cP.set(p);
        }
        return new ActionResult(true, true, false);
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
