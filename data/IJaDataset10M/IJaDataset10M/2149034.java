package org.xteam.sled.ast;

import org.xteam.parser.runtime.Span;

public class NeOp extends Relop {

    public NeOp(Span span) {
        super(span);
    }

    public void visit(ISledVisitor visitor) {
        visitor.visitNeOp(this);
    }
}
