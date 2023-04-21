package com.aionemu.gameserver.ai.desires.impl;

import com.aionemu.gameserver.ai.AI;
import com.aionemu.gameserver.ai.desires.AbstractDesire;
import com.aionemu.gameserver.ai.desires.MoveDesire;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;

/**
 * @author Pinguin, ATracer
 *
 */
public class MoveToTargetDesire extends AbstractDesire implements MoveDesire {

    private Npc owner;

    private Creature target;

    /**
	 * @param crt 
	 * @param desirePower
	 */
    public MoveToTargetDesire(Npc owner, Creature target, int desirePower) {
        super(desirePower);
        this.owner = owner;
        this.target = target;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean handleDesire(AI ai) {
        if (owner == null || owner.getLifeStats().isAlreadyDead()) return false;
        if (target == null || target.getLifeStats().isAlreadyDead()) return false;
        owner.getMoveController().setFollowTarget(true);
        if (!owner.getMoveController().isScheduled()) owner.getMoveController().schedule();
        double distance = owner.getMoveController().getDistanceToTarget();
        if (distance > 150) return false;
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MoveToTargetDesire)) return false;
        MoveToTargetDesire that = (MoveToTargetDesire) o;
        return target.equals(that.target);
    }

    /**
	 * @return the target
	 */
    public Creature getTarget() {
        return target;
    }

    @Override
    public int getExecutionInterval() {
        return 1;
    }

    @Override
    public void onClear() {
        owner.getMoveController().stop();
    }
}
