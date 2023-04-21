package jlambda.microcode;

import java.math.BigDecimal;
import java.math.BigInteger;
import jlambda.term.ConstantExpressions;
import jlambda.term.LTerm;
import jlambda.term.LTermInteger;
import jlambda.term.LTermReal;
import jlambda.term.Symbols;

public class GreaterEqualpOperation extends BinaryOperation {

    private static GreaterEqualpOperation Instance = new GreaterEqualpOperation();

    public static GreaterEqualpOperation getInstance() {
        return Instance;
    }

    GreaterEqualpOperation() {
        super(Symbols.GREATEREQUALP);
        getOperationMap().put(this.getName(), this);
    }

    @Override
    public LTerm apply(LTerm arg1, LTerm arg2) {
        if ((arg1 instanceof LTermInteger) && (arg2 instanceof LTermInteger)) {
            BigInteger x = ((LTermInteger) arg1).getValue();
            BigInteger y = ((LTermInteger) arg2).getValue();
            if (x.compareTo(y) > -1) return ConstantExpressions.TRUE; else return ConstantExpressions.FALSE;
        } else if ((arg1 instanceof LTermReal) && (arg2 instanceof LTermReal)) {
            BigDecimal x = ((LTermReal) arg1).getValue();
            BigDecimal y = ((LTermReal) arg2).getValue();
            if (x.compareTo(y) > -1) return ConstantExpressions.TRUE; else return ConstantExpressions.FALSE;
        } else throw new MicrocodeException(">= not defined for " + arg1 + ", " + arg2);
    }
}
