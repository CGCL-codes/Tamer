package de.tum.in.botl.heuristics.syntaxChecker;

import de.tum.in.botl.model.ModelException;
import de.tum.in.botl.ruleSet.Rule;
import de.tum.in.botl.ruleSet.RuleSet;

public interface SyntaxChecker {

    public abstract boolean checkSyntax(RuleSet rs) throws ModelException;

    public abstract boolean checkSyntax(Rule r) throws ModelException;

    public abstract void setIndent(int i);
}
