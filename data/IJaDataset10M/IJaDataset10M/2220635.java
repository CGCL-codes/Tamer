package net.sf.l2j.gameserver.skills.conditions;

import net.sf.l2j.gameserver.skills.Env;

/**
 * @author mkizub TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code Templates
 */
public class ConditionTargetNone extends Condition {

    public ConditionTargetNone() {
    }

    @Override
    public boolean testImpl(Env env) {
        return env.target == null;
    }
}
