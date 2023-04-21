package com.l2jserver.gameserver.skills.effects;

import com.l2jserver.gameserver.model.L2Effect;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.skills.Env;
import com.l2jserver.gameserver.templates.effects.EffectTemplate;
import com.l2jserver.gameserver.templates.skills.L2EffectType;

/**
 * 
 * @author nBd
 */
public class EffectDisarm extends L2Effect {

    public EffectDisarm(Env env, EffectTemplate template) {
        super(env, template);
    }

    /**
	 * 
	 * @see com.l2jserver.gameserver.model.L2Effect#getEffectType()
	 */
    @Override
    public L2EffectType getEffectType() {
        return L2EffectType.DISARM;
    }

    /**
	 * 
	 * @see com.l2jserver.gameserver.model.L2Effect#onStart()
	 */
    @Override
    public boolean onStart() {
        if (!(getEffected() instanceof L2PcInstance)) return false;
        ((L2PcInstance) getEffected()).disarmWeapons();
        getEffected().setIsDisarmed(true);
        return true;
    }

    /**
	 * 
	 * @see com.l2jserver.gameserver.model.L2Effect#onExit()
	 */
    @Override
    public void onExit() {
        getEffected().setIsDisarmed(false);
    }

    /**
	 * 
	 * @see com.l2jserver.gameserver.model.L2Effect#onActionTime()
	 */
    @Override
    public boolean onActionTime() {
        return false;
    }
}
