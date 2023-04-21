package com.l2jserver.gameserver.skills.conditions;

import com.l2jserver.gameserver.model.L2Effect;
import com.l2jserver.gameserver.skills.Env;

/**
 * The Class ConditionTargetActiveEffectId.
 */
public class ConditionTargetActiveEffectId extends Condition {

    private final int _effectId;

    private final int _effectLvl;

    /**
     * Instantiates a new condition target active effect id.
     *
     * @param effectId the effect id
     */
    public ConditionTargetActiveEffectId(int effectId) {
        _effectId = effectId;
        _effectLvl = -1;
    }

    /**
     * Instantiates a new condition target active effect id.
     *
     * @param effectId the effect id
     * @param effectLevel the effect level
     */
    public ConditionTargetActiveEffectId(int effectId, int effectLevel) {
        _effectId = effectId;
        _effectLvl = effectLevel;
    }

    @Override
    public boolean testImpl(Env env) {
        final L2Effect e = env.target.getFirstEffect(_effectId);
        if (e != null && (_effectLvl == -1 || _effectLvl <= e.getSkill().getLevel())) return true;
        return false;
    }
}
