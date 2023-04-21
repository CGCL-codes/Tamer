package com.agentfactory.logic.update.lang;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import com.agentfactory.logic.update.interpreter.CoreUtilities;
import com.agentfactory.logic.update.reasoner.Bindings;

public class LessThan implements IFormula {

    private ITerm left;

    private ITerm right;

    public LessThan(ITerm left, ITerm right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public IFormula apply(Bindings set) {
        return new LessThan(left.apply(set), right.apply(set));
    }

    @Override
    public List<IFormula> asList() {
        List<IFormula> list = new LinkedList<IFormula>();
        list.add(this);
        return list;
    }

    @Override
    public Set<Variable> getVariables() {
        Set<Variable> vars = new HashSet<Variable>();
        vars.addAll(left.getVariables());
        vars.addAll(right.getVariables());
        return vars;
    }

    @Override
    public boolean isFalse() {
        try {
            int l = Integer.parseInt(CoreUtilities.presenter.toString(left));
            int r = Integer.parseInt(CoreUtilities.presenter.toString(right));
            return l >= r;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    @Override
    public boolean isTrue() {
        try {
            int l = Integer.parseInt(CoreUtilities.presenter.toString(left));
            int r = Integer.parseInt(CoreUtilities.presenter.toString(right));
            return l < r;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    @Override
    public boolean matches(IFormula formula) {
        if (LessThan.class.isInstance(formula)) {
            LessThan e = (LessThan) formula;
            return left.equals(e.left) && right.equals(e.right);
        }
        return false;
    }

    @Override
    public String type() {
        return "raw-logic";
    }

    public ITerm left() {
        return left;
    }

    public ITerm right() {
        return right;
    }
}
