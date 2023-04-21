package com.l2jserver.gameserver.skills.effects;

import com.l2jserver.gameserver.model.L2Effect;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.L2Playable;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import com.l2jserver.gameserver.skills.Env;
import com.l2jserver.gameserver.templates.effects.EffectTemplate;
import com.l2jserver.gameserver.templates.skills.L2EffectType;
import com.l2jserver.gameserver.templates.skills.L2SkillType;

public class EffectSilentMove extends L2Effect {

    public EffectSilentMove(Env env, EffectTemplate template) {
        super(env, template);
    }

    public EffectSilentMove(Env env, L2Effect effect) {
        super(env, effect);
    }

    /**
	 * 
	 * @see com.l2jserver.gameserver.model.L2Effect#onStart()
	 */
    @Override
    public boolean onStart() {
        super.onStart();
        L2Character effected = getEffected();
        if (effected instanceof L2Playable) ((L2Playable) effected).setSilentMoving(true);
        return true;
    }

    /**
	 * 
	 * @see com.l2jserver.gameserver.model.L2Effect#onExit()
	 */
    @Override
    public void onExit() {
        super.onExit();
        L2Character effected = getEffected();
        if (effected instanceof L2Playable) ((L2Playable) effected).setSilentMoving(false);
    }

    /**
	 * 
	 * @see com.l2jserver.gameserver.model.L2Effect#getEffectType()
	 */
    @Override
    public L2EffectType getEffectType() {
        return L2EffectType.SILENT_MOVE;
    }

    /**
	 * 
	 * @see com.l2jserver.gameserver.model.L2Effect#onActionTime()
	 */
    @Override
    public boolean onActionTime() {
        if (getSkill().getSkillType() != L2SkillType.CONT) return false;
        if (getEffected().isDead()) return false;
        double manaDam = calc();
        if (manaDam > getEffected().getCurrentMp()) {
            getEffected().sendPacket(new SystemMessage(SystemMessageId.SKILL_REMOVED_DUE_LACK_MP));
            return false;
        }
        getEffected().reduceCurrentMp(manaDam);
        return true;
    }
}
