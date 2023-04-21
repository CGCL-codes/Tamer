package com.android1.amarena2d.input;

import com.android1.amarena2d.engine.AmarenaCamera;
import com.android1.amarena2d.nodes.behavior.Touchable;

/**
 * isHit always true
 */
public class FullScreenTouchSpot extends BaseTouchSpot implements TouchSpot {

    protected final Touchable entity;

    protected AmarenaCamera camera;

    public FullScreenTouchSpot(Touchable entity, int priority) {
        this.entity = entity;
        this.priority = priority;
    }

    public FullScreenTouchSpot(Touchable entity) {
        this.entity = entity;
        this.priority = nextPriority();
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public AmarenaCamera getCamera() {
        if (camera == null) camera = entity.getCamera();
        return camera;
    }

    @Override
    public boolean isHit(float x, float y) {
        return true;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean isActive() {
        return active;
    }
}
