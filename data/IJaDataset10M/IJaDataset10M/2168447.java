package com.bbn.vessel.core.runtime.trigger;

import com.bbn.vessel.core.runtime.condition.Condition;

/**
 * Publish a {@link Trigger} every time a {@link Condition} becomes false.
 * <p>
 * 
 * <pre>
 * E.g.:
 *   cd_foo(is_true=false) --&gt; tr_bar
 * </pre>
 * <p>
 * FIXME we <b>don't</b> count the first time we see the condition, e.g. if
 * we're "born" in roomX.
 * 
 * @see OnTrue true case
 * @see com.bbn.vessel.core.runtime.condition.IsIf combine triggers into a
 *      condition
 */
public final class OnFalse extends OnConditionBase {

    @Override
    protected boolean compute(boolean is_true) {
        return !is_true;
    }
}
