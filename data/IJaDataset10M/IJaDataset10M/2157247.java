package org.xteam.sled.ast;

import org.xteam.parser.runtime.Span;

public class ConstructorsSpec extends Spec {

    protected org.xteam.parser.runtime.AstList<Constructor> constructors;

    public ConstructorsSpec(Span span, org.xteam.parser.runtime.AstList<Constructor> constructors) {
        super(span);
        this.constructors = constructors;
    }

    public org.xteam.parser.runtime.AstList<Constructor> getConstructors() {
        return constructors;
    }

    public void setConstructors(org.xteam.parser.runtime.AstList<Constructor> constructors) {
        this.constructors = constructors;
    }

    public void visit(ISledVisitor visitor) {
        visitor.visitConstructorsSpec(this);
    }
}
