package org.elascript.ast;

import org.elascript.symbol.*;

public abstract class ASTNode {

    private Scope scope;

    private int line;

    public ASTNode(int line) {
        this.line = line;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public Scope getScope() {
        return scope;
    }

    public int getLine() {
        return line;
    }

    public abstract <T, E extends Throwable> T accept(ASTVisitor<T, E> visitor) throws E;
}
