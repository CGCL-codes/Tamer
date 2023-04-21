package org.sablecc.java.node;

import org.sablecc.java.analysis.*;

@SuppressWarnings("nls")
public final class APercentAssignAssignmentOperator extends PAssignmentOperator {

    private TPercentAssign _percentAssign_;

    public APercentAssignAssignmentOperator() {
    }

    public APercentAssignAssignmentOperator(@SuppressWarnings("hiding") TPercentAssign _percentAssign_) {
        setPercentAssign(_percentAssign_);
    }

    @Override
    public Object clone() {
        return new APercentAssignAssignmentOperator(cloneNode(this._percentAssign_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAPercentAssignAssignmentOperator(this);
    }

    public TPercentAssign getPercentAssign() {
        return this._percentAssign_;
    }

    public void setPercentAssign(TPercentAssign node) {
        if (this._percentAssign_ != null) {
            this._percentAssign_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._percentAssign_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._percentAssign_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._percentAssign_ == child) {
            this._percentAssign_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._percentAssign_ == oldChild) {
            setPercentAssign((TPercentAssign) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
