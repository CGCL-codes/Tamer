package com.migazzi.dm4j.satSolver.impl.journal.journalActions;

import java.util.List;
import com.migazzi.dm4j.satSolver.impl.journal.JournaledRule;

public class ListActionSet extends AbstractListAction<JournaledRule> {

    private int index;

    private JournaledRule newRule;

    private JournaledRule oldRule;

    public ListActionSet(List<JournaledRule> rules, int i, JournaledRule r) {
        super(rules);
        index = i;
        newRule = r;
    }

    @Override
    protected JournaledRule doExecute() {
        return oldRule = rules.set(index, newRule);
    }

    @Override
    protected void doRollback() {
        rules.set(index, oldRule);
    }
}
