package com.l2jserver.gameserver.skills.conditions;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.skills.Env;

/**
 * The Class ConditionPlayerIsHero.
 */
public class ConditionPlayerIsHero extends Condition {

    private final boolean _val;

    /**
	 * Instantiates a new condition player is hero.
	 *
	 * @param val the val
	 */
    public ConditionPlayerIsHero(boolean val) {
        _val = val;
    }

    @Override
    public boolean testImpl(Env env) {
        if (!(env.player instanceof L2PcInstance)) return false;
        return (((L2PcInstance) env.player).isHero() == _val);
    }
}
