package toxTree.ui.tree.actions;

import toxTree.core.IDecisionRule;

public interface IRuleAction {

    IDecisionRule getRule();

    void setRule(IDecisionRule rule);
}
