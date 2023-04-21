package org.drarch.engine.stepEngine;

import java.util.Collections;
import java.util.Set;
import org.drarch.engine.ruleEngine.Suggest;

/**
 * @author
 * @author maldonadofacundo@gmail.com (Facundo Maldonado)
 * 
 */
public class InteractivePhase extends Phase {

    Set<Suggest> lastStepSuggests = Collections.emptySet();

    public InteractivePhase(IStep chain_head) {
        super(chain_head);
    }

    public void executePhase() {
        lastStepSuggests = getNextStep().execute();
    }

    public IPhase nextPhase() {
        if (hasNextStep()) return this;
        return super.nextPhase();
    }

    public Set<Suggest> getLastStepSuggests() {
        return lastStepSuggests;
    }

    public String getName() {
        return null;
    }
}
