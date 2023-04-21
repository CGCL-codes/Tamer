package com.bbn.vessel.author.addStrategyWizard;

import com.bbn.vessel.author.util.wizard.Choice;
import com.bbn.vessel.core.model.ocm.Condition;

class ConditionChoice implements Choice {

    private final Condition condition;

    ConditionChoice(Condition condition) {
        this.condition = condition;
    }

    /**
     * @see com.bbn.vessel.author.util.wizard.Choice#getActionCommand()
     */
    @Override
    public String getActionCommand() {
        return condition.getFQName();
    }

    /**
     * @see com.bbn.vessel.author.util.wizard.Choice#getNotAvailableReason()
     */
    @Override
    public String getNotAvailableReason() {
        return null;
    }

    /**
     * @see com.bbn.vessel.author.util.wizard.Choice#getAnnotation()
     */
    @Override
    public String getAnnotation() {
        return "Add condition " + getActionCommand() + " to this situation";
    }
}
