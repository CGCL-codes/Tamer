package net.sourceforge.g2destiny.model.event;

import net.sourceforge.g2destiny.ui.g2d.DPoint;
import net.sourceforge.g2destiny.ui.g2d.DTraverser;

/**
 * Event model for a g2destiny entity.
 * 
 * @author Jeff D. Conrad
 * @since 02/21/2010
 */
public class G2DMovementEvent extends G2DEvent {

    private static final long serialVersionUID = 560684102425303754L;

    static final int G2DMOVEMENT_EVENT_ID = 20001;

    DPoint nextPoint;

    DPoint prevPoint;

    public G2DMovementEvent(DTraverser traverser) {
        super(traverser, G2DMOVEMENT_EVENT_ID);
    }

    public DPoint getPrevPoint() {
        return prevPoint;
    }

    public void setPrevPoint(DPoint aftPoint) {
        this.prevPoint = aftPoint;
    }

    public DPoint getNextPoint() {
        return nextPoint;
    }

    public void setNextPoint(DPoint forePoint) {
        this.nextPoint = forePoint;
    }

    public DTraverser getTraverser() {
        return (DTraverser) this.source;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("G2DEvent[");
        buffer.append("super = ").append(super.toString());
        buffer.append("forePoint = ").append(nextPoint);
        buffer.append("aftPoint = ").append(prevPoint);
        buffer.append("]");
        return buffer.toString();
    }
}
