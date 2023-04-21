package org.sableccsupport.sccparser.node;

import org.sableccsupport.sccparser.analysis.*;

@SuppressWarnings("nls")
public final class AStateListTail extends PStateListTail {

    private TComma _comma_;

    private TId _id_;

    private PTransition _transition_;

    public AStateListTail() {
    }

    public AStateListTail(@SuppressWarnings("hiding") TComma _comma_, @SuppressWarnings("hiding") TId _id_, @SuppressWarnings("hiding") PTransition _transition_) {
        setComma(_comma_);
        setId(_id_);
        setTransition(_transition_);
    }

    @Override
    public Object clone() {
        return new AStateListTail(cloneNode(this._comma_), cloneNode(this._id_), cloneNode(this._transition_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAStateListTail(this);
    }

    public TComma getComma() {
        return this._comma_;
    }

    public void setComma(TComma node) {
        if (this._comma_ != null) {
            this._comma_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._comma_ = node;
    }

    public TId getId() {
        return this._id_;
    }

    public void setId(TId node) {
        if (this._id_ != null) {
            this._id_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._id_ = node;
    }

    public PTransition getTransition() {
        return this._transition_;
    }

    public void setTransition(PTransition node) {
        if (this._transition_ != null) {
            this._transition_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._transition_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._comma_) + toString(this._id_) + toString(this._transition_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._comma_ == child) {
            this._comma_ = null;
            return;
        }
        if (this._id_ == child) {
            this._id_ = null;
            return;
        }
        if (this._transition_ == child) {
            this._transition_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._comma_ == oldChild) {
            setComma((TComma) newChild);
            return;
        }
        if (this._id_ == oldChild) {
            setId((TId) newChild);
            return;
        }
        if (this._transition_ == oldChild) {
            setTransition((PTransition) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
