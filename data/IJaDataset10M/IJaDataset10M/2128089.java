package de.tum.in.botl.heuristics.implementation;

import de.tum.in.botl.ruleSet.interfaces.RuleSetInterface;

public interface UpperBoundsConformance {

    public abstract boolean isUpperBoundsConform(RuleSetInterface rs);

    public abstract void setIndent(int indent);
}
