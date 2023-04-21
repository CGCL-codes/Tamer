package org.clark.ace2.core.condact;

import org.clark.ace2.core.Context;

/**
 *
 * @author andy
 */
public class EQ extends AbstractCondAct {

    public EQ() {
        params = new Class[] { String.class, Integer.class };
    }

    @Override
    protected CondactResponse process(Context ctx, Object... parameters) {
        int flag1 = ctx.getFlagManager().getFlag((String) parameters[0]);
        int value = (Integer) parameters[1];
        if (flag1 == value) return CondactResponse.OK;
        return CondactResponse.COND_EXIT;
    }
}
