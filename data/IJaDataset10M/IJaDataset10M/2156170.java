package org.xteam.cs.types;

import org.xteam.cs.runtime.Span;

public abstract class Type {

    protected Span span;

    public Type(Span span) {
        this.span = span;
    }

    public boolean isRepeatable() {
        return false;
    }

    public boolean isPrimitive() {
        return false;
    }

    public Span span() {
        return span;
    }

    public boolean isAssignableTo(Type type) {
        return false;
    }
}
