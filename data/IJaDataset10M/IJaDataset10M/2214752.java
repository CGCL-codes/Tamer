package com.l2jserver.gameserver.skills.effects;

import com.l2jserver.gameserver.model.L2Effect;
import com.l2jserver.gameserver.skills.Env;
import com.l2jserver.gameserver.templates.effects.EffectTemplate;
import com.l2jserver.gameserver.templates.skills.L2EffectType;

/**
 * @author mkizub
 */
public class EffectBuff extends L2Effect {

    public EffectBuff(Env env, EffectTemplate template) {
        super(env, template);
    }

    public EffectBuff(Env env, L2Effect effect) {
        super(env, effect);
    }

    @Override
    public L2EffectType getEffectType() {
        return L2EffectType.BUFF;
    }

    @Override
    public boolean onActionTime() {
        return false;
    }
}
