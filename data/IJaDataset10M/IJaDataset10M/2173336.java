package org.xteam.sled.ast;

import org.xteam.parser.runtime.Span;

public abstract class AbstractPattern extends org.xteam.parser.runtime.AstNode {

    public AbstractPattern(Span span) {
        super(span);
    }

    public abstract void visit(ISledVisitor visitor);
}
