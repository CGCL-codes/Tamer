package com.aionemu.gameserver.model.gameobjects;

import com.aionemu.gameserver.controllers.CreatureController;
import com.aionemu.gameserver.world.WorldPosition;

/**
 * This class is representing movable objects, its base class for all in game objects that may move
 * 
 * @author -Nemesiss-
 * 
 */
public abstract class Creature extends VisibleObject {

    private Creature target;

    public Creature(int objId, CreatureController<? extends Creature> controller, WorldPosition position) {
        super(objId, controller, position);
    }

    public Creature getTarget() {
        return target;
    }

    public void setTarget(Creature creature) {
        target = creature;
    }

    /**
	 * Return CreatureController of this Creature object.
	 * 
	 * @return CreatureController.
	 */
    @SuppressWarnings("unchecked")
    @Override
    public CreatureController<? extends Creature> getController() {
        return (CreatureController<? extends Creature>) super.getController();
    }

    public abstract byte getLevel();
}
