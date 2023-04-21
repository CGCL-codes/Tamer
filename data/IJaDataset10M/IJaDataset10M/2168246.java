package org.xteam.sled.ast;

import org.xteam.parser.runtime.Span;

public class AnyPatternBinding extends PatternBinding {

    protected Ident name;

    protected org.xteam.parser.runtime.AstList<Ident> names;

    protected AbstractPattern pattern;

    public AnyPatternBinding(Span span, Ident name, org.xteam.parser.runtime.AstList<Ident> names, AbstractPattern pattern) {
        super(span);
        this.name = name;
        this.names = names;
        this.pattern = pattern;
    }

    public Ident getName() {
        return name;
    }

    public void setName(Ident name) {
        this.name = name;
    }

    public org.xteam.parser.runtime.AstList<Ident> getNames() {
        return names;
    }

    public void setNames(org.xteam.parser.runtime.AstList<Ident> names) {
        this.names = names;
    }

    public AbstractPattern getPattern() {
        return pattern;
    }

    public void setPattern(AbstractPattern pattern) {
        this.pattern = pattern;
    }

    public void visit(ISledVisitor visitor) {
        visitor.visitAnyPatternBinding(this);
    }
}
