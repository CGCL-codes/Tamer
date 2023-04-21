package mscheme.values.functions;

import mscheme.exceptions.SchemeException;
import mscheme.util.Arity;
import mscheme.values.IList;

public abstract class Thunk extends CheckedFunction {

    public static final String CVS_ID = "$Id: Thunk.java 701 2004-05-18 12:57:44Z sielenk $";

    protected final Arity getArity() {
        return Arity.exactly(0);
    }

    protected final Object checkedCall(mscheme.machine.Registers state, IList arguments) throws SchemeException {
        return checkedCall(state);
    }

    protected abstract Object checkedCall(mscheme.machine.Registers state) throws SchemeException;
}
