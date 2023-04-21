package com.agentfactory.clf.lang;

import com.agentfactory.clf.interpreter.CoreUtilities;
import com.agentfactory.clf.reasoner.Bindings;

public class LessThan extends Comparison {

    public LessThan(ITerm left, ITerm right) {
        super(left, right);
    }

    @Override
    public IFormula apply(Bindings set) {
        return new LessThan(left.apply(set), right.apply(set));
    }

    @Override
    public boolean isFalse() {
        return !isTrue();
    }

    @Override
    public boolean isTrue() {
        try {
            double l = Double.parseDouble(CoreUtilities.presenter.toString(left));
            double r = Double.parseDouble(CoreUtilities.presenter.toString(right));
            return l < r;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}
