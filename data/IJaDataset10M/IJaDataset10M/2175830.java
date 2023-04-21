package org.sablecc.java.node;

import org.sablecc.java.analysis.*;

@SuppressWarnings("nls")
public final class AIfElseStatement extends PStatement {

    private PIfThenElseStatement _ifThenElseStatement_;

    public AIfElseStatement() {
    }

    public AIfElseStatement(@SuppressWarnings("hiding") PIfThenElseStatement _ifThenElseStatement_) {
        setIfThenElseStatement(_ifThenElseStatement_);
    }

    @Override
    public Object clone() {
        return new AIfElseStatement(cloneNode(this._ifThenElseStatement_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAIfElseStatement(this);
    }

    public PIfThenElseStatement getIfThenElseStatement() {
        return this._ifThenElseStatement_;
    }

    public void setIfThenElseStatement(PIfThenElseStatement node) {
        if (this._ifThenElseStatement_ != null) {
            this._ifThenElseStatement_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._ifThenElseStatement_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._ifThenElseStatement_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._ifThenElseStatement_ == child) {
            this._ifThenElseStatement_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._ifThenElseStatement_ == oldChild) {
            setIfThenElseStatement((PIfThenElseStatement) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
