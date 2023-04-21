package com.bbn.vessel.core.runtime.condition;

/**
 * 
 * This class provides runtime functionality for a graph node.
 * <p>
 * "true" if all the input conditions are "true".
 * <p>
 * 
 * <pre>
 * E.g. if we're:
 *   IsAnd, in_cd=cd_a, in_cd2=cd_b, out_cd=cd_and
 * and we see:
 *   cd_a=true    (wait until we've seen both cd_a &amp; cd_b)
 *   cd_a=false
 *   cd_a=true
 *   cd_b=false --&gt; cd_and=false   (we've seen cd_a &amp; cd_b)
 *   cd_b=true  --&gt; cd_and=true
 *   cd_a=false --&gt; cd_and=false
 *   cd_b=false
 * Trigger inputs are not accepted.  Instead, one can use an {@link IsAfter}
 * condition to convert a trigger into an always-on condition.
 * </pre>
 */
public final class IsAnd extends IsLogicalBase {

    @Override
    protected boolean compute(int i, int n) {
        return (i == n);
    }
}
