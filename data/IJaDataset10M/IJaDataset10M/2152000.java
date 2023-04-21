package iicm.vrml.pw;

import iicm.utils3d.Hitpoint;
import iicm.utils3d.Vec3f;

public class TouchSensor extends Sensor implements PointerSensor {

    public SFBool enabled;

    public SFVec3f hitNormal_changed;

    public SFVec3f hitPoint_changed;

    public SFVec2f hitTexCoord_changed;

    public SFBool isActive;

    public SFBool isOver;

    public SFTime touchTime;

    public String nodeName() {
        return NodeNames.NODE_TOUCHSENSOR;
    }

    public void traverse(Traverser t) {
        t.tTouchSensor(this);
    }

    TouchSensor() {
        addField("enabled", enabled = new SFBool(true), Field.F_EXPOSEDFIELD);
        addField("hitNormal_changed", hitNormal_changed = new SFVec3f(0.0f, 0.0f, 0.0f), Field.F_EVENTOUT);
        addField("hitPoint_changed", hitPoint_changed = new SFVec3f(0.0f, 0.0f, 0.0f), Field.F_EVENTOUT);
        addField("hitTexCoord_changed", hitTexCoord_changed = new SFVec2f(0.0f, 0.0f), Field.F_EVENTOUT);
        addField("isActive", isActive = new SFBool(false), Field.F_EVENTOUT);
        addField("isOver", isOver = new SFBool(false), Field.F_EVENTOUT);
        addField("touchTime", touchTime = new SFTime(0.0), Field.F_EVENTOUT);
    }

    /**
   * handle mouse move in interaction mode
   */
    public void mouseMove(float downx, float downy, Hitpoint hit, double timestamp) {
        if (!enabled.getValue()) return;
        if (!isOver.getValue()) {
            isOver.setValue(true);
            isOver.sendEvent(timestamp);
        }
        Vec3f hitoc = new Vec3f();
        hitoc.rayat(hit.raystartobj_.value_, hit.hittime_, hit.raydirobj_.value_);
        hitNormal_changed.setValue(hit.normalobj_.value_[0], hit.normalobj_.value_[1], hit.normalobj_.value_[2]);
        hitNormal_changed.sendEvent(timestamp);
        hitPoint_changed.setValue(hitoc.value_[0], hitoc.value_[1], hitoc.value_[2]);
        hitPoint_changed.sendEvent(timestamp);
    }

    /**
   * handle mouse drag in interaction mode
   */
    public void mouseDrag(float downx, float downy, Hitpoint hit, double timestamp) {
        if (!enabled.getValue()) return;
        if (isOver.getValue() != (hit != null)) {
            isOver.setValue(hit != null);
            isOver.sendEvent(timestamp);
        }
        if (hit == null) return;
        Vec3f hitoc = new Vec3f();
        hitoc.rayat(hit.raystartobj_.value_, hit.hittime_, hit.raydirobj_.value_);
        hitNormal_changed.setValue(hit.normalobj_.value_[0], hit.normalobj_.value_[1], hit.normalobj_.value_[2]);
        hitNormal_changed.sendEvent(timestamp);
        hitPoint_changed.setValue(hitoc.value_[0], hitoc.value_[1], hitoc.value_[2]);
        hitPoint_changed.sendEvent(timestamp);
    }

    /**
   * handle mouse down in interaction mode
   */
    public void mouseDown(float downx, float downy, Hitpoint hit, double timestamp) {
        if (!enabled.getValue()) return;
        if (!isOver.getValue()) {
            isOver.setValue(true);
            isOver.sendEvent(timestamp);
        }
        isActive.setValue(true);
        isActive.sendEvent(timestamp);
        Vec3f hitoc = new Vec3f();
        hitoc.rayat(hit.raystartobj_.value_, hit.hittime_, hit.raydirobj_.value_);
        hitNormal_changed.setValue(hit.normalobj_.value_[0], hit.normalobj_.value_[1], hit.normalobj_.value_[2]);
        hitNormal_changed.sendEvent(timestamp);
        hitPoint_changed.setValue(hitoc.value_[0], hitoc.value_[1], hitoc.value_[2]);
        hitPoint_changed.sendEvent(timestamp);
    }

    /**
   * handle mouse up in interaction mode
   */
    public void mouseUp(double timestamp) {
        if (!enabled.getValue()) return;
        if (isOver.getValue()) {
            touchTime.setValue(timestamp);
            touchTime.sendEvent(timestamp);
        }
        isActive.setValue(false);
        isActive.sendEvent(timestamp);
    }

    /**
   * handle mouse exit
   */
    public void mouseExit(double timestamp) {
        if (!enabled.getValue()) return;
        if (isOver.getValue()) {
            isOver.setValue(false);
            isOver.sendEvent(timestamp);
        }
    }
}
