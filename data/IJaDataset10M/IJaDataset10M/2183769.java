package javax.media.ding3d.utils.pickfast.behaviors;

import javax.media.ding3d.utils.pickfast.*;
import javax.media.ding3d.internal.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.media.ding3d.*;
import javax.media.ding3d.vecmath.*;

/**
 * Base class that allows users to adding picking and mouse manipulation to
 * the scene graph (see PickDragBehavior for an example of how to extend
 * this base class). This class is useful for interactive apps.
 */
public abstract class PickMouseBehavior extends Behavior {

    protected PickCanvas pickCanvas;

    protected WakeupCriterion[] conditions;

    protected WakeupOr wakeupCondition;

    protected boolean buttonPress = false;

    protected TransformGroup currGrp;

    protected static final boolean debug = false;

    protected MouseEvent mevent;

    /**
     * Creates a PickMouseBehavior given current canvas, root of the tree to
     * operate on, and the bounds.
     */
    public PickMouseBehavior(Canvas3D canvas, BranchGroup root, Bounds bounds) {
        super();
        currGrp = new TransformGroup();
        currGrp.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        currGrp.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        root.addChild(currGrp);
        pickCanvas = new PickCanvas(canvas, root);
    }

    /**
     * Sets the pick mode
     * @see PickTool#setMode
     **/
    public void setMode(int pickMode) {
        pickCanvas.setMode(pickMode);
    }

    /**
     * Returns the pickMode
     * @see PickTool#getMode
     */
    public int getMode() {
        return pickCanvas.getMode();
    }

    /**
     * Sets the pick tolerance
     * @see PickCanvas#setTolerance
     */
    public void setTolerance(float tolerance) {
        pickCanvas.setTolerance(tolerance);
    }

    /**
     * Returns the pick tolerance
     * @see PickCanvas#getTolerance
     */
    public float getTolerance() {
        return pickCanvas.getTolerance();
    }

    public void initialize() {
        conditions = new WakeupCriterion[2];
        conditions[0] = new WakeupOnAWTEvent(Event.MOUSE_MOVE);
        conditions[1] = new WakeupOnAWTEvent(Event.MOUSE_DOWN);
        wakeupCondition = new WakeupOr(conditions);
        wakeupOn(wakeupCondition);
    }

    private void processMouseEvent(MouseEvent evt) {
        buttonPress = false;
        if (evt.getID() == MouseEvent.MOUSE_PRESSED | evt.getID() == MouseEvent.MOUSE_CLICKED) {
            buttonPress = true;
            return;
        } else if (evt.getID() == MouseEvent.MOUSE_MOVED) {
        }
    }

    public void processStimulus(Enumeration criteria) {
        WakeupCriterion wakeup;
        AWTEvent[] evt = null;
        int xpos = 0, ypos = 0;
        while (criteria.hasMoreElements()) {
            wakeup = (WakeupCriterion) criteria.nextElement();
            if (wakeup instanceof WakeupOnAWTEvent) evt = ((WakeupOnAWTEvent) wakeup).getAWTEvent();
        }
        if (evt[0] instanceof MouseEvent) {
            mevent = (MouseEvent) evt[0];
            if (debug) System.out.println("got mouse event");
            processMouseEvent((MouseEvent) evt[0]);
            xpos = mevent.getPoint().x;
            ypos = mevent.getPoint().y;
        }
        if (debug) System.out.println("mouse position " + xpos + " " + ypos);
        if (buttonPress) {
            updateScene(xpos, ypos);
        }
        wakeupOn(wakeupCondition);
    }

    /** Subclasses shall implement this update function
     */
    public abstract void updateScene(int xpos, int ypos);
}
