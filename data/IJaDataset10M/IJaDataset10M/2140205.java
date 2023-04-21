package jlambda.microcode;

import jlambda.term.LTerm;
import jlambda.term.LTermInteger;
import jlambda.term.Symbols;

public class NthCdrOperation extends BinaryOperation {

    private static NthCdrOperation Instance = new NthCdrOperation();

    public static NthCdrOperation getInstance() {
        return Instance;
    }

    NthCdrOperation() {
        super(Symbols.NTHCDR);
        getOperationMap().put(this.getName(), this);
    }

    @Override
    public LTerm apply(LTerm arg1, LTerm arg2) {
        int n = ((LTermInteger) arg1).getValue().intValue();
        if (arg2.isAtom()) return arg2;
        while (n > 0) {
            arg2 = arg2.getTl();
            n--;
        }
        return arg2;
    }
}
