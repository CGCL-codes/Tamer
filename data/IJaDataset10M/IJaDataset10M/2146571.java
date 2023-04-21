package com.migazzi.dm4j.satSolver.impl.journal.journalActions;

import com.migazzi.dm4j.context.Rule;

public class RuleActionDelete extends AbstractRuleAction<Integer> {

    int index;

    int value;

    public RuleActionDelete(Rule r, int index) {
        super(r);
        this.index = index;
    }

    @Override
    protected Integer doExecute() {
        value = rule.delete(index);
        return value;
    }

    @Override
    protected void doRollback() {
        rule.insert(value, index);
    }
}
