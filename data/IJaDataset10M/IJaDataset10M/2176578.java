package net.sf.refactorit.classmodel;

public final class BinVariableArityParameter extends BinParameter {

    public BinVariableArityParameter(String name, BinTypeRef typeRef, int modifiers) {
        super(name, typeRef, modifiers);
    }

    public boolean isVariableArity() {
        return true;
    }
}
