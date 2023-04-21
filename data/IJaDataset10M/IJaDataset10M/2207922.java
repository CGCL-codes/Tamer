package com.l2jserver.gameserver.model.actor.status;

import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2SiegeFlagInstance;

public class SiegeFlagStatus extends NpcStatus {

    public SiegeFlagStatus(L2SiegeFlagInstance activeChar) {
        super(activeChar);
    }

    @Override
    public void reduceHp(double value, L2Character attacker) {
        reduceHp(value, attacker, true, false, false);
    }

    @Override
    public void reduceHp(double value, L2Character attacker, boolean awake, boolean isDOT, boolean isHpConsumption) {
        if (getActiveChar().isAdvancedHeadquarter()) value /= 2.;
        super.reduceHp(value, attacker, awake, isDOT, isHpConsumption);
    }

    @Override
    public L2SiegeFlagInstance getActiveChar() {
        return (L2SiegeFlagInstance) super.getActiveChar();
    }
}
