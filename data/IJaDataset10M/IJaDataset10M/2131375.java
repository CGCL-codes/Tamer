package org.fernwood.jbasic.funcs;

import org.fernwood.jbasic.runtime.ArgumentList;
import org.fernwood.jbasic.runtime.JBasicException;
import org.fernwood.jbasic.runtime.SymbolTable;
import org.fernwood.jbasic.value.RecordStreamValue;
import org.fernwood.jbasic.value.Value;

/**
 * <b>DISTINCT()</b> JBasic Function
 * <p>
 * <table>
 * <tr><td><b>Description:</b></td><td>count of distinct values in the arg list</td></tr>
 * <tr><td><b>Invocation:</b></td><td><code>n = DISTINCT( ... )</code></td></tr>
 * <tr><td><b>Returns:</b></td><td>Integer</td></tr>
 * </table>
 * <p>
 * Calculates the number of distinct values in the argument list.  If the argument list is
 * an array, then counts the distinct values in the the array.
 * @author cole
 */
public class DistinctFunction extends JBasicFunction {

    /**
	 * Runtime execution of the function via _CALLF
	 * 
	 * @param arglist the function argument list and count already 
	 * popped from the runtime data stack
	 * @param symbols the currently active symbol table
	 * @return a Value containing the function result.
	 * @throws JBasicException if an argument or execution error occurs
	 */
    public Value run(final ArgumentList arglist, final SymbolTable symbols) throws JBasicException {
        Value set = new Value(Value.ARRAY, null);
        boolean fTable = false;
        for (int ix = 0; ix < arglist.size(); ix++) {
            Value v = arglist.element(ix);
            if (v.isType(Value.ARRAY) || v.isType(Value.TABLE)) {
                if (ix == 0 && arglist.size() == 1 && v.isType(Value.TABLE)) fTable = true;
                for (int vx = 1; vx <= v.size(); vx++) {
                    Value item = v.getElement(vx);
                    if (!set.contains(item)) {
                        set.addElement(item);
                    }
                }
            } else if (!set.contains(v)) {
                set.addElement(v);
            }
        }
        if (fTable) {
            RecordStreamValue v = (RecordStreamValue) arglist.element(0);
            RecordStreamValue r = new RecordStreamValue(v.columnNames());
            for (int ix = 1; ix <= set.size(); ix++) r.setElement(set.getElement(ix), ix);
            return r;
        }
        return set;
    }
}
