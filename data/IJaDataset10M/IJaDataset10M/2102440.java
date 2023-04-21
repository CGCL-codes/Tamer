package org.jslaughter.component.physics.motion;

import org.newdawn.slick.geom.Vector2f;
import org.nvframe.component.physics.state.PhysicsPosition;
import org.nvframe.entity.Entity;
import org.nvframe.event.EventService;
import org.nvframe.event.eventtype.UpdateEvent;
import org.nvframe.event.eventtype.UpdateListener;
import org.nvframe.exception.NVFrameException;
import org.nvframe.util.settings.SettingsObj;

/**
 * 
 * @author Nik Van Looy
 */
public class FlamethrowerFlameMotion extends BulletMotion implements UpdateListener {

    private float impulse;

    private float offset;

    private float lifeTime;

    private float currentLifeTimeMs = 0;

    private Vector2f startCoords = new Vector2f();

    private Vector2f velocity = new Vector2f();

    @SuppressWarnings("unused")
    private boolean moving = false;

    public FlamethrowerFlameMotion(String id, Entity owner, SettingsObj settings) throws NVFrameException {
        super(id, owner);
        impulse = settings.getFloat("impulse");
        offset = settings.getFloat("offset", 0);
        lifeTime = settings.getFloat("lifeTime", 1000);
    }

    /**
	 * 
	 * @param startCoords
	 * @param targetCoords
	 * @param offset
	 */
    public void fire(Vector2f startCoords, Vector2f targetCoords, float offset) {
        this.offset = offset;
        fire(startCoords.copy(), targetCoords.copy());
    }

    /**
	 * 
	 * @param startCoords
	 * @param targetCoords
	 */
    public void fire(Vector2f startCoords, Vector2f targetCoords) {
        this.startCoords.set(startCoords);
        PhysicsPosition positionComp = (PhysicsPosition) owner.getComponent(PhysicsPosition.class);
        float radDirection = (float) Math.atan2(targetCoords.getY() - startCoords.getY(), targetCoords.getX() - startCoords.getX());
        if (positionComp == null) return;
        positionComp.setDirection((float) Math.toDegrees(radDirection));
        positionComp.setXY(startCoords.getX() + (float) Math.cos(radDirection) * offset, startCoords.getY() + (float) Math.sin(radDirection) * offset);
        velocity.x = (float) Math.cos(radDirection) * impulse;
        velocity.y = (float) Math.sin(radDirection) * impulse;
        positionComp.applyImpulse(velocity.x, velocity.y);
        EventService.getInstance().addEventListener(this);
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        currentLifeTimeMs += event.getDelta();
        if (currentLifeTimeMs > lifeTime) owner.removed();
    }

    @Override
    public void removed() {
        EventService.getInstance().removeEventListener(this);
    }
}
