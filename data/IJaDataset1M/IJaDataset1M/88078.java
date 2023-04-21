package org.xteam.sled.ast;

import org.xteam.parser.runtime.Span;

public class PlaceholderSpec extends Spec {

    protected Ident ident;

    protected AbstractPattern pattern;

    public PlaceholderSpec(Span span, Ident ident, AbstractPattern pattern) {
        super(span);
        this.ident = ident;
        this.pattern = pattern;
    }

    public Ident getIdent() {
        return ident;
    }

    public void setIdent(Ident ident) {
        this.ident = ident;
    }

    public AbstractPattern getPattern() {
        return pattern;
    }

    public void setPattern(AbstractPattern pattern) {
        this.pattern = pattern;
    }

    public void visit(ISledVisitor visitor) {
        visitor.visitPlaceholderSpec(this);
    }
}
