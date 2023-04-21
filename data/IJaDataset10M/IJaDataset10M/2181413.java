package org.xteam.sled.semantic.exp;

public class Tautology extends DefaultExpVisitor {

    private boolean value;

    public boolean test(Exp e) {
        value = false;
        e.visit(this);
        return value;
    }

    @Override
    public void visitCondition(ExpCondition expCond) {
        value = false;
    }
}
