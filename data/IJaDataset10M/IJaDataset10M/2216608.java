package net.sf.l2j.gameserver.skills.effects;

import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.skills.Env;

/**
 * @author mkizub TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code Templates
 */
final class EffectStun extends L2Effect {

    public EffectStun(Env env, EffectTemplate template) {
        super(env, template);
    }

    @Override
    public EffectType getEffectType() {
        return EffectType.STUN;
    }

    /** Notify started */
    @Override
    public void onStart() {
        getEffected().startStunning();
    }

    /** Notify exited */
    @Override
    public void onExit() {
        getEffected().stopStunning(this);
    }

    @Override
    public boolean onActionTime() {
        return false;
    }
}
