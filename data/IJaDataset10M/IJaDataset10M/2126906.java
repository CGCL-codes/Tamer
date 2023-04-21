package gr.konstant.transonto.interfaces.impl;

import gr.konstant.transonto.exception.*;
import gr.konstant.transonto.interfaces.Expr;
import gr.konstant.transonto.interfaces.KnowledgeBase;

public abstract class ExprImpl extends AnythingImpl implements Expr {

    protected final KnowledgeBase kb;

    protected ExprImpl(KnowledgeBase kb) {
        this.kb = kb;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public abstract boolean equals(Object o);

    @Override
    public KnowledgeBase livesIn() {
        return kb;
    }

    @Override
    public abstract Expr inKnowledgeBase(KnowledgeBase otherKB) throws BackendException, UnsupportedFeatureException;
}
