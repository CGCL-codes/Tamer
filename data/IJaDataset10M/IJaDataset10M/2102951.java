package org.ontospread.strategy;

import org.ontospread.restrictions.OntoSpreadRestriction;
import org.ontospread.state.OntoSpreadState;

public abstract class OntoSpreadStrategy {

    private OntoSpreadRestriction restriction;

    public OntoSpreadStrategy(OntoSpreadRestriction restriction) {
        this.restriction = restriction;
    }

    public OntoSpreadRestriction getRestriction() {
        return restriction;
    }

    public abstract OntoSpreadState strategy(OntoSpreadState state);
}
