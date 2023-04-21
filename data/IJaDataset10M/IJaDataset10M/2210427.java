package org.xteam.sled.ast;

import org.xteam.parser.runtime.Span;

public class BitSpec extends Spec {

    protected IntegerLiteral zeroInt;

    protected Ident value;

    public BitSpec(Span span, IntegerLiteral zeroInt, Ident value) {
        super(span);
        this.zeroInt = zeroInt;
        this.value = value;
    }

    public IntegerLiteral getZeroInt() {
        return zeroInt;
    }

    public void setZeroInt(IntegerLiteral zeroInt) {
        this.zeroInt = zeroInt;
    }

    public Ident getValue() {
        return value;
    }

    public void setValue(Ident value) {
        this.value = value;
    }

    public void visit(ISledVisitor visitor) {
        visitor.visitBitSpec(this);
    }
}
