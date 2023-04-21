package org.xteam.sled.ast;

import org.xteam.parser.runtime.Span;

public class IdentOperand extends Operand {

    protected Ident ident;

    protected Extend extend;

    public IdentOperand(Span span, Ident ident, Extend extend) {
        super(span);
        this.ident = ident;
        this.extend = extend;
    }

    public Ident getIdent() {
        return ident;
    }

    public void setIdent(Ident ident) {
        this.ident = ident;
    }

    public Extend getExtend() {
        return extend;
    }

    public void setExtend(Extend extend) {
        this.extend = extend;
    }

    public void visit(ISledVisitor visitor) {
        visitor.visitIdentOperand(this);
    }
}
