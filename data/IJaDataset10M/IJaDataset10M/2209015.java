package org.jmlspecs.jir.ast;

public final class JmlSignalsClause<Expression, Type> extends JmlClause<Expression> {

    public static final int DESCRIPTOR = 13;

    protected LocalDeclaration<Expression, Type> arg;

    public JmlSignalsClause() {
    }

    public final LocalDeclaration<Expression, Type> getArg() {
        return this.arg;
    }

    @Override
    public final int getDescriptor() {
        return JmlSignalsClause.DESCRIPTOR;
    }

    public final void setArg(final LocalDeclaration<Expression, Type> arg) {
        this.arg = arg;
    }
}
