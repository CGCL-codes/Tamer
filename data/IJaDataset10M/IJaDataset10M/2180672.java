package org.jmlspecs.jir.internal.visitor.result;

import org.jmlspecs.jir.visitor.result.IResult;
import org.jmlspecs.jir.visitor.result.JIRType;

public class TypeResult implements IResult {

    private JIRType type;

    public TypeResult(JIRType type) {
        this.type = type;
    }

    public JIRType getType() {
        return this.type;
    }
}
