package com.google.testing.webtestingexplorer.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.testing.webtestingexplorer.actions.Action;
import com.google.testing.webtestingexplorer.actions.ActionSequence;
import com.google.testing.webtestingexplorer.driver.WebDriverWrapper;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.Set;

/**
 * Filters out action sequences that have given actions consecutively but in
 * different order from existing action sequences still to be tested.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class OrderInsensitiveActionSequenceFilter implements ActionSequenceFilter {

    private Set<Action> orderInsensitiveActions;

    public OrderInsensitiveActionSequenceFilter(Set<Action> actions) {
        this.orderInsensitiveActions = actions;
    }

    @Override
    public boolean shouldExplore(ActionSequence actionSequence, Deque<ActionSequence> existingActionSequences) {
        Iterator<ActionSequence> existingActionSequenceIter = existingActionSequences.iterator();
        while (existingActionSequenceIter.hasNext()) {
            ActionSequence existingActionSequence = existingActionSequenceIter.next();
            if (hasRedundantOrdering(actionSequence, existingActionSequence)) {
                return false;
            }
        }
        return true;
    }

    /**
   * We need two things for redundancy:
   *   1. All the same actions must be in each sequence.
   *   2. The order-insensitive ones must be in the same order in both.
   */
    private boolean hasRedundantOrdering(ActionSequence actionSequence1, ActionSequence actionSequence2) {
        Set<Action> actions1Set = Sets.newHashSet(actionSequence1.getActions());
        Set<Action> actions2Set = Sets.newHashSet(actionSequence2.getActions());
        if (!actions1Set.equals(actions2Set)) {
            return false;
        }
        ArrayList<Action> actions1List = normalizeOrderInsensitiveActions(actionSequence1);
        ArrayList<Action> actions2List = normalizeOrderInsensitiveActions(actionSequence2);
        return actions1List.equals(actions2List);
    }

    /**
   * Creates an ordered list of actions from the given sequence with the order-
   * insensitive ones stubbed to something normalized.
   */
    private ArrayList<Action> normalizeOrderInsensitiveActions(ActionSequence actionSequence) {
        ArrayList<Action> actions = Lists.newArrayList(actionSequence.getActions());
        for (int index = 0; index < actions.size(); ++index) {
            Action action = actions.get(index);
            if (orderInsensitiveActions.contains(action)) {
                actions.set(index, new EquivalentAction());
            }
        }
        return actions;
    }

    /**
   * An action that is solely for the purposes of checking against other actions.
   */
    private static class EquivalentAction extends Action {

        @Override
        public void perform(WebDriverWrapper driver) {
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof EquivalentAction) {
                return true;
            }
            return false;
        }
    }
}
