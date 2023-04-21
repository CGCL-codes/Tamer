package nakayo.gameserver.ai.desires.impl;

import nakayo.gameserver.ai.AI;
import nakayo.gameserver.ai.desires.AbstractDesire;
import nakayo.gameserver.ai.desires.MoveDesire;
import nakayo.gameserver.ai.state.AIState;
import nakayo.gameserver.model.gameobjects.Creature;
import nakayo.gameserver.model.gameobjects.Npc;
import nakayo.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import nakayo.gameserver.utils.MathUtil;

/**
 * @author Pinguin, ATracer
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

    @Override
    public boolean handleDesire(AI<?> ai) {
        if (owner == null || owner.getLifeStats().isAlreadyDead()) return false;
        if (target == null || target.getLifeStats().isAlreadyDead()) return false;
        owner.getMoveController().setFollowTarget(true);
        if (!owner.getMoveController().isScheduled()) owner.getMoveController().schedule();
        double distance = owner.getMoveController().getDistanceToTarget();
        if (owner.getSpawn() != null) {
            double dist = MathUtil.getDistance(owner.getX(), owner.getY(), owner.getZ(), owner.getSpawn().getX(), owner.getSpawn().getY(), owner.getSpawn().getZ());
            if (dist > 100 && !owner.hasWalkRoutes()) {
                owner.getLifeStats().increaseHp(TYPE.NATURAL_HP, owner.getLifeStats().getMaxHp());
                ai.setAiState(AIState.MOVINGTOHOME);
                return false;
            }
        }
        if (distance > 125) return false;
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
