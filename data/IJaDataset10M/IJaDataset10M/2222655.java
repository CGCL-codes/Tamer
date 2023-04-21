package org.xteam.sled.ast;

import org.xteam.parser.runtime.Span;

public class SledFile extends org.xteam.parser.runtime.AstNode {

    protected org.xteam.parser.runtime.AstList<Spec> specs;

    public SledFile(Span span, org.xteam.parser.runtime.AstList<Spec> specs) {
        super(span);
        this.specs = specs;
    }

    public org.xteam.parser.runtime.AstList<Spec> getSpecs() {
        return specs;
    }

    public void setSpecs(org.xteam.parser.runtime.AstList<Spec> specs) {
        this.specs = specs;
    }

    public void visit(ISledVisitor visitor) {
        visitor.visitSledFile(this);
    }
}
