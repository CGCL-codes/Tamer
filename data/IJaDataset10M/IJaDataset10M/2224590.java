package net.sourceforge.piqle.strategies.impl;

import java.util.Collection;
import net.sourceforge.piqle.strategies.Strategy;

public class ChooseOnlyAvailableActionStrategy<TState, TAction> implements Strategy<TState, TAction> {

    public TAction chooseAction(TState state, Collection<TAction> actions) {
        if (actions.size() == 0) return null;
        if (actions.size() > 1) throw new RuntimeException(actions.size() + " actions given to ChooseOnlyAvailableActionStrategy, need exactly one"); else return actions.iterator().next();
    }
}
