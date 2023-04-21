package mscheme.machine;

import mscheme.exceptions.SchemeException;
import mscheme.values.ListFactory;
import mscheme.values.ValueTraits;
import mscheme.values.functions.UnaryFunction;

/**
 * @author sielenk
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Controller extends UnaryFunction {

    public static final String id = "$Id: Controller.java 691 2004-01-16 21:52:46Z sielenk $";

    private final StackList.Mark _mark;

    Controller(StackList.Mark mark) {
        _mark = mark;
    }

    protected Object checkedCall(Registers state, Object argument) throws SchemeException {
        return ValueTraits.apply(state, argument, ListFactory.create(new Subcontinuation(_mark.cutSlice(state.getStack()))));
    }
}
