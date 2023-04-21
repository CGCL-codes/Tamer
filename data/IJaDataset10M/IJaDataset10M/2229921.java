package org.xteam.sled.model;

import java.util.Map;
import org.xteam.sled.semantic.GlobalState;
import org.xteam.sled.semantic.exp.Exp;
import org.xteam.sled.semantic.exp.ExpAdd;
import org.xteam.sled.semantic.exp.ExpConst;
import org.xteam.sled.semantic.exp.ExpPC;
import org.xteam.sled.semantic.exp.IExpRewriter;

public class ActualLabel extends Label implements ILabelledSequent {

    private String name;

    public ActualLabel(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    @Override
    public void addBinding(Map<String, Exp> bindings, int position) {
        if (bindings.containsKey(name)) throw new RuntimeException("label " + name + " appears twice in disjunct");
        bindings.put(name, new ExpAdd(new ExpPC(), new ExpConst(position / GlobalState.pcUnitBits)));
    }

    public static ActualLabel newLabel(String name) {
        return new ActualLabel(name);
    }

    public boolean isActual() {
        return true;
    }

    @Override
    public ISequent merge(ISequent other) {
        throw new RuntimeException("should not be called on LabelledSequent");
    }

    @Override
    public ISequent subs(IExpRewriter subs) {
        return this;
    }

    @Override
    public ActualLabel actualize(boolean actualsOk) {
        return this;
    }

    @Override
    public String toString() {
        return name + ":";
    }
}
