package com.l2jserver.gameserver.skills.effects;

import com.l2jserver.gameserver.model.L2Effect;
import com.l2jserver.gameserver.skills.Env;
import com.l2jserver.gameserver.templates.effects.EffectTemplate;
import com.l2jserver.gameserver.templates.skills.L2EffectType;

/**
 * @author -Nemesiss-
 * 
 */
public class EffectPhysicalMute extends L2Effect {

    public EffectPhysicalMute(Env env, EffectTemplate template) {
        super(env, template);
    }

    /**
	 * 
	 * @see com.l2jserver.gameserver.model.L2Effect#getEffectType()
	 */
    @Override
    public L2EffectType getEffectType() {
        return L2EffectType.PHYSICAL_MUTE;
    }

    /**
	 * 
	 * @see com.l2jserver.gameserver.model.L2Effect#onStart()
	 */
    @Override
    public boolean onStart() {
        getEffected().startPsychicalMuted();
        return true;
    }

    /**
	 * 
	 * @see com.l2jserver.gameserver.model.L2Effect#onActionTime()
	 */
    @Override
    public boolean onActionTime() {
        return false;
    }

    /**
	 * 
	 * @see com.l2jserver.gameserver.model.L2Effect#onExit()
	 */
    @Override
    public void onExit() {
        getEffected().stopPsychicalMuted(this);
    }
}
