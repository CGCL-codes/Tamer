package net.sf.l2j.gameserver.skills.effects;

import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.skills.Env;

/**
 * @author demonia
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
final class EffectImobilePetBuff extends L2Effect {

    private L2Summon _pet;

    public EffectImobilePetBuff(Env env, EffectTemplate template) {
        super(env, template);
    }

    @Override
    public EffectType getEffectType() {
        return EffectType.BUFF;
    }

    /** Notify started */
    @Override
    public void onStart() {
        _pet = null;
        if (getEffected() instanceof L2Summon && getEffector() instanceof L2PcInstance && ((L2Summon) getEffected()).getOwner() == getEffector()) {
            _pet = (L2Summon) getEffected();
            _pet.setIsImobilised(true);
        }
    }

    /** Notify exited */
    @Override
    public void onExit() {
        if (_pet != null) _pet.setIsImobilised(false);
    }

    @Override
    public boolean onActionTime() {
        return false;
    }
}
