package com.hardcode.gdbms.engine.instruction;

import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;

/**
 * @author Fernando Gonz�lez Cort�s
 */
public class LValueElementAdapter extends Adapter {

    public Value evaluate(long rowIndex) throws EvaluationException {
        if (getEntity().first_token.image.toLowerCase().equals("null")) {
            return ValueFactory.createNullValue();
        } else {
            return ((Expression) getChilds()[0]).evaluate(rowIndex);
        }
    }
}
