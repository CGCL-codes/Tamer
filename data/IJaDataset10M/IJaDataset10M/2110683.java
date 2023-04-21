package org.sablecc.java.node;

import org.sablecc.java.analysis.*;

@SuppressWarnings("nls")
public final class AIfThenElseStatementNoShortIfStatementNoShortIf extends PStatementNoShortIf {

    private PIfThenElseStatementNoShortIf _ifThenElseStatementNoShortIf_;

    public AIfThenElseStatementNoShortIfStatementNoShortIf() {
    }

    public AIfThenElseStatementNoShortIfStatementNoShortIf(@SuppressWarnings("hiding") PIfThenElseStatementNoShortIf _ifThenElseStatementNoShortIf_) {
        setIfThenElseStatementNoShortIf(_ifThenElseStatementNoShortIf_);
    }

    @Override
    public Object clone() {
        return new AIfThenElseStatementNoShortIfStatementNoShortIf(cloneNode(this._ifThenElseStatementNoShortIf_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAIfThenElseStatementNoShortIfStatementNoShortIf(this);
    }

    public PIfThenElseStatementNoShortIf getIfThenElseStatementNoShortIf() {
        return this._ifThenElseStatementNoShortIf_;
    }

    public void setIfThenElseStatementNoShortIf(PIfThenElseStatementNoShortIf node) {
        if (this._ifThenElseStatementNoShortIf_ != null) {
            this._ifThenElseStatementNoShortIf_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._ifThenElseStatementNoShortIf_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._ifThenElseStatementNoShortIf_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._ifThenElseStatementNoShortIf_ == child) {
            this._ifThenElseStatementNoShortIf_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._ifThenElseStatementNoShortIf_ == oldChild) {
            setIfThenElseStatementNoShortIf((PIfThenElseStatementNoShortIf) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
