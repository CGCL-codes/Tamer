package mscheme.values.functions;

import mscheme.exceptions.SchemeException;
import mscheme.machine.Registers;
import mscheme.values.ListFactory;
import mscheme.values.ValueTraits;

public final class CallCCFunction extends UnaryFunction {

    public static final String CVS_ID = "$Id: CallCCFunction.java 740 2005-08-27 12:36:06Z sielenk $";

    public static final CallCCFunction INSTANCE = new CallCCFunction();

    private CallCCFunction() {
    }

    protected Object checkedCall(Registers state, Object argument) throws SchemeException, InterruptedException {
        return ValueTraits.apply(state, argument, ListFactory.create(new Subcontinuation(state.getStack().getContinuation())));
    }
}
