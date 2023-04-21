package org.norecess.nolatte.primitives.indexed;

import org.norecess.nolatte.ast.Datum;
import org.norecess.nolatte.ast.Primitive;
import org.norecess.nolatte.interpreters.IInterpreter;
import org.norecess.nolatte.system.NoLatteVariables;

public class LengthPrimitive extends Primitive {

    private static final long serialVersionUID = 4305576680638289867L;

    public LengthPrimitive() {
        getParameters().addPositional(NoLatteVariables.DATUM);
    }

    public Datum apply(IInterpreter interpreter) {
        return interpreter.getDatumFactory().createText(interpreter.getDataTypeFilter().getIndexedDatum(interpreter.getEnvironment().get(NoLatteVariables.DATUM)).getLength());
    }
}
