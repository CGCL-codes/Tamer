package org.expasy.jpl.commons.base.cond.operator.impl;

import org.expasy.jpl.commons.base.cond.operator.api.AbstractOperator;

public final class OperatorOr extends AbstractOperator<Boolean, Boolean> {

    private static final OperatorOr INSTANCE = new OperatorOr();

    public static OperatorOr getInstance() {
        return INSTANCE;
    }

    private OperatorOr() {
        setCompatibleOperands(Boolean.class, Boolean.class);
    }

    public boolean eval(Boolean o1, Boolean o2) {
        return o1 || o2;
    }

    public String getToken() {
        return "|";
    }

    public String getName() {
        return "_or_";
    }
}
