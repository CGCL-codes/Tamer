package org.clark.ace2.core.condact;

import org.clark.ace2.core.Context;

/**
 *
 * @author Andy
 */
public class DESC extends AbstractCondAct {

    DESC() {
        params = new Class[] {};
    }

    @Override
    protected CondactResponse process(Context ctx, Object... parameters) {
        ctx.getOutput().clearScreen();
        return CondactResponse.DESC;
    }
}
