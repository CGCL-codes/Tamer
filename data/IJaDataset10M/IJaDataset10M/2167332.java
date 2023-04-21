package mscheme.values.functions;

import mscheme.exceptions.SchemeException;
import mscheme.machine.Registers;
import mscheme.util.Arity;
import mscheme.values.IList;

public abstract class TernaryFunction extends CheckedFunction {

    public static final String CVS_ID = "$Id: TernaryFunction.java 701 2004-05-18 12:57:44Z sielenk $";

    protected final Arity getArity() {
        return Arity.exactly(3);
    }

    protected final Object checkedCall(Registers state, IList arguments) throws SchemeException {
        return checkedCall(state, arguments.getHead(), arguments.getTail().getHead(), arguments.getTail().getTail().getHead());
    }

    protected abstract Object checkedCall(Registers state, Object fst, Object snd, Object trd) throws SchemeException;
}
