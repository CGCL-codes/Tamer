package org.jmlspecs.jir.ast;

public final class JirDurationClause<Expression> extends JirConditionedClause<Expression> {

    public static final int DESCRIPTOR = 27;

    public JirDurationClause() {
    }

    @Override
    public final int getDescriptor() {
        return JirDurationClause.DESCRIPTOR;
    }
}
