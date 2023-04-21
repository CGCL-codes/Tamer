package com.hsbc.hbfr.ccf.at.logreader.predicate;

public class NegatableStringLikePredicate extends PredicateStringLike implements NegatablePredicate {

    private boolean negated = false;

    public void setNegated(boolean b) {
        negated = b;
    }

    @Override
    public boolean evaluate(Object e) {
        return negated ^ super.evaluate(e);
    }
}
