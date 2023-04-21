package org.fressia.actions.embedded;

import org.fressia.core.sbes.SbeEvaluator;
import org.fressia.core.sbes.SbeFactory;

/**
 * 
 * @author Alvaro Egana
 *
 */
public class ScriptEvaluator extends SbeEvaluator {

    public <T> ScriptEvaluator(T targetValue, Class<T> targetValueType, final SbeFactory newFactory) throws Exception {
        super(targetValue, targetValueType, newFactory);
    }

    public boolean evaluateEvents(String expression) throws Exception {
        return evaluateExpression(expression);
    }
}
