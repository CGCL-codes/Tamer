package net.nexttext.behaviour.standard;

import processing.core.PVector;
import net.nexttext.Locatable;
import net.nexttext.PLocatableVector;
import net.nexttext.TextObject;
import net.nexttext.behaviour.AbstractAction;
import net.nexttext.behaviour.TargetingAction;
import net.nexttext.property.NumberProperty;
import net.nexttext.property.PVectorProperty;

public class MoveTo extends AbstractAction implements TargetingAction {

    protected Locatable target;

    /**
     * Move a TextObject to a specified position.
     * @param x x target position
     * @param y y target position
     */
    public MoveTo(int x, int y) {
        this(x, y, Long.MAX_VALUE);
    }

    /**
     * Move a TextObject to a specified position at a certain speed.
     * @param x x target position
     * @param y x target position
     * @param speed moving speed
     */
    public MoveTo(int x, int y, long speed) {
        this(new PVector(x, y), speed);
    }

    /**
     * Move a TextObject to a target.
     * @param target locatable target
     */
    public MoveTo(Locatable target) {
        this(target, Long.MAX_VALUE);
    }

    /**
     * Move a TextObject to a target at a certain speed.
     * @param target locatable target
	 * @param speed The speed of the approach represented as the number of
	 * pixels to move in each frame.  Use a very large number for instant
	 * travel.
     */
    public MoveTo(Locatable target, long speed) {
        this.target = target;
        properties().init("Speed", new NumberProperty(speed));
    }

    /**
     * Move a TextObject to a target.
     * @param target locatable target
     */
    public MoveTo(PVector target) {
        this(target, Long.MAX_VALUE);
    }

    /**
     * Move a TextObject to a specified position.
     * @param target position to move to
	 * @param speed The speed of the approach represented as the number of
	 * pixels to move in each frame.  Use a very large number for instant
	 * travel.
     */
    public MoveTo(PVector target, long speed) {
        this.target = new PLocatableVector(target);
        properties().init("Speed", new NumberProperty(speed));
    }

    /**
     * Add a vector to the position to bring it closer to the target.
     *
     * <p>Result is complete if it has reached its target. </p>
     */
    public ActionResult behave(TextObject to) {
        float speed = ((NumberProperty) properties().get("Speed")).get();
        PVector pos = to.getPositionAbsolute();
        PVector newDir = target.getLocation();
        newDir.sub(pos);
        ActionResult result = new ActionResult(true, true, false);
        if (newDir.mag() > speed) {
            newDir.normalize();
            newDir.mult(speed);
            result.complete = false;
        }
        PVectorProperty posProp = getPosition(to);
        posProp.add(newDir);
        return result;
    }

    /**
     * Sets a target to approach.
     */
    public void setTarget(Locatable target) {
        this.target = target;
    }

    /**
     * Sets a target to approach.
     */
    public void setTarget(float x, float y) {
        setTarget(x, y, 0);
    }

    /**
     * Sets a target to approach.
     */
    public void setTarget(float x, float y, float z) {
        setTarget(new PLocatableVector(x, y, z));
    }

    /**
     * Sets a target to approach.
     */
    public void setTarget(PVector target) {
        setTarget(new PLocatableVector(target));
    }
}
